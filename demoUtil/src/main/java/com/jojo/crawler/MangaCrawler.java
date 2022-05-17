package com.jojo.crawler;

import cn.hutool.http.HttpUtil;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.jojo.util.FileUtil;
import com.jojo.util.RegexUtil;
import com.jojo.util.SeleniumUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jojo.util.http.HttpConstant.ATTR_href;
import static com.jojo.util.http.HttpConstant.TAG_a;

/**
 * 开心就好
 *
 * @author jojo
 */
public class MangaCrawler {

    public static final Function<Element, String> ELEMENT_TO_URL_FUNCTION = new Function<Element, String>() {
        @Override
        public String apply(Element input) {
            return input.attr("src");
        }
    };

    /**
     * 某一话及该话的URL存放地
     */
    public static String PATH_OF_TITLE_AND_URL = "D:\\Workspace\\test\\manga\\titleAndUrl.txt";

    public static String BASE_SAVE_DIRECTORY = "D:\\Workspace\\test\\manga\\";

    private static Logger logger = LoggerFactory.getLogger(MangaCrawler.class);

    /**
     * 从文件中读标题与这一话的url
     *
     * @param filepath
     * @return
     * @throws IOException
     */
    public static Map<String, String> getTitleAndUrlFromFile(String filepath) throws IOException {
        Map<String, String> titleAndUrl = Maps.newHashMap();
        File file = new File(filepath);
        BufferedReader reader = Files.newReader(file, Charset.forName("UTF-8"));
        String temp = null;
        while ((temp = reader.readLine()) != null) {
            if (StringUtils.isBlank(temp)) {
                continue;
            }
            String[] arr = temp.split(";");
            titleAndUrl.put(arr[0], arr[1]);
        }
        // try你卵子
        reader.close();
        return titleAndUrl;
    }

    /**
     * 没有正则表达式，只是简单的用了下contains方法
     *
     * @param url
     * @param keyWord
     * @throws IOException
     */
    public static void saveTitleAndUrl(String url, String keyWord) throws IOException {
        // 前期准备
        File file = new File(PATH_OF_TITLE_AND_URL);
        if (!file.exists()) {
            Files.createParentDirs(file);
            file.createNewFile();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));

        WebDriver driver = SeleniumUtil.getWebDriver();
        driver.get(url);
        // 获取所有A标签
        List<WebElement> webElements = driver.findElements(By.tagName(TAG_a));
        for (WebElement webElement : webElements) {
            String text = webElement.getText();
            if (StringUtils.contains(text, keyWord)) {
                String titleAndUrl = webElement.getText() + ";" + webElement.getAttribute(ATTR_href);
                logger.error("找到{}", titleAndUrl);
                bufferedWriter.write(titleAndUrl);
                bufferedWriter.write("\r\n");
            }
        }

        driver.close();
        bufferedWriter.close();
    }

    /**
     * 保存至本地，文件名就用url里的
     *
     * @param allPicUrlMap
     * @throws Exception
     */
    public static void saveToLocalWithOriginName(Map<String, List<String>> allPicUrlMap) throws Exception {
        for (Map.Entry<String, List<String>> entry : allPicUrlMap.entrySet()) {
            String directory = BASE_SAVE_DIRECTORY + FileUtil.filterInvalidCharacter(entry.getKey()) + File.separator;
            for (String picUrl : entry.getValue()) {
                String fileName = directory + RegexUtil.getLastPartOfUrl(picUrl);
                FileUtil.createNewFileFromInternet(picUrl, fileName);
            }
        }
    }

    /**
     * 保存至本地，文件名不使用url里的，单纯按序命名
     *
     * @param allPicUrlMap
     * @throws Exception
     */
    public static void saveToLocal(Map<String, List<String>> allPicUrlMap) throws Exception {
        for (Map.Entry<String, List<String>> entry : allPicUrlMap.entrySet()) {
            String directory = BASE_SAVE_DIRECTORY + FileUtil.filterInvalidCharacter(entry.getKey()) + File.separator;
            int i = 1;
            for (String picUrl : entry.getValue()) {
                // 后缀名不能丢额
                String fileName = directory + (i++) + "." + RegexUtil.getSuffixFromUrl(picUrl);
                FileUtil.createNewFileFromInternet(picUrl, fileName);
            }
        }
    }

    public static Document getJsoupDocumentByWebDriver(String url) {
        WebDriver webDriver = SeleniumUtil.getWebDriver();
        webDriver.get(url);
        Document document = Jsoup.parse(webDriver.getPageSource());
        webDriver.close();
        return document;
    }

    public static Document getJsoupDocumentByFile(String path) {
        Document document = null;
        try {
            String html = Files.asCharSource(new File(path), Charsets.UTF_8).read();
            document = Jsoup.parse(html);
        } catch (IOException e) {
            logger.error("读取文件异常", e);
        }
        return document;
    }

    public static void main(String[] args) throws Exception {
        BASE_SAVE_DIRECTORY = "C:\\Workspace\\test\\";
        String htmlPath = "https://www.wnacg.org/search/?q=%E6%92%BF%E5%80%8B%E5%A5%B3%E5%B8%9D%E7%95%B6%E6%80%A7%E5%A5%B4&f=_all&s=create_time_DESC&syn=yes";
        String wnacgPrefix = "https://www.wnacg.org";
        Document document = getJsoupDocumentByWebDriver(htmlPath);

        WebDriver webDriver = SeleniumUtil.getWebDriver();
        // 抽取需要下载的url
        List<String> containsDownloadUrlList = Lists.newArrayList();
        Elements elementAList = document.getElementsByTag("a");
        for (Element elementA : elementAList) {
            if (!StringUtils.contains(elementA.text(), "撿個")) {
                continue;
            }
            String url = wnacgPrefix + elementA.attr("href");
            logger.info(elementA.text() + "====" + url);
            containsDownloadUrlList.add(url);
        }

        // 挨个处理，获取下载地址的页面
        List<String> downloadUrlList = Lists.newArrayList();
        downloadUrlList.clear();
        for (String url : containsDownloadUrlList) {
            webDriver.get(url);
            logger.info("driver访问网页，获取title===" + webDriver.getTitle());
            List<WebElement> aTagList = webDriver.findElements(By.tagName("a"));
            for (WebElement aTag : aTagList) {
                if (StringUtils.equals(aTag.getText(), "下載漫畫")) {
                    String href = aTag.getAttribute("href");
                    logger.info("下载的页面地址===" + href);
                    downloadUrlList.add(href);
                }
            }
        }

        Map<String, String> filenameAndDownUrlMap = new HashMap<>();
        for (String url : downloadUrlList) {
            webDriver.get(url);
            String downloadFilename = webDriver.findElement(By.className("download_filename")).getText();
            String href = webDriver.findElement(By.className("down_btn")).getAttribute("href");
            logger.info("drvier访问最终的下载地址==={} ：{}", downloadFilename, href);
            filenameAndDownUrlMap.put(downloadFilename, href);
        }

        webDriver.close();

        for (Map.Entry<String, String> entry : filenameAndDownUrlMap.entrySet()) {
            String filePath = BASE_SAVE_DIRECTORY + entry.getKey();
            logger.info("开始下载==={} : {}", entry.getKey(), filePath);
            HttpUtil.downloadFile(entry.getValue(), filePath);
            logger.info("下载完毕");
        }

    }


}
