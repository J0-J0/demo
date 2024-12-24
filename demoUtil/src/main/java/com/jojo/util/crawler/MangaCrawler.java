package com.jojo.util.crawler;

import static com.jojo.util.http.HttpConstant.ATTR_href;
import static com.jojo.util.http.HttpConstant.TAG_a;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
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

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.jojo.util.FileUtil;
import com.jojo.util.RegexUtil;
import com.jojo.util.SeleniumUtil;

/**
 * 开心就好
 *
 * @author jojo
 */
public class MangaCrawler {

    public static final Function<Element, String> ELEMENT_TO_URL_FUNCTION = input -> input.attr("src");

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
        BASE_SAVE_DIRECTORY = "C:\\Workspace\\test\\hunter\\";

        String fileName = "C:\\Users\\flash.wg\\Desktop\\新文件 1.txt";
        List<String> list = IOUtils.readLines(new BufferedReader(new FileReader(fileName)));

        for (int i = 0; i < list.size(); i += 2) {
            String title = list.get(i);
            String baseUrl = list.get(i + 1);
            List<String> picImgUrlList = new ArrayList<>();
            for (int j = 1; j <= 2; j++) {
                String url = baseUrl + "?pn=" + j;
                Document document = Jsoup.connect(url).execute().parse();
                Elements imgTagList = document.getElementsByClass("BDE_Image");
                for (Element imgTag : imgTagList) {
                    if (!imgTag.tagName().equals("img")) {
                        continue;
                    }
                    picImgUrlList.add(imgTag.attr("src"));
                }
            }

            Map<String, List<String>> titlePicUrlMap = new HashMap<>();
            titlePicUrlMap.put(title, picImgUrlList);
            saveToLocal(titlePicUrlMap);
        }

    }


}
