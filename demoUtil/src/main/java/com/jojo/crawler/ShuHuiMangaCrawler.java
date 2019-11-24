package com.jojo.crawler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.jojo.util.FileUtil;
import com.jojo.util.SeleniumUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.jojo.util.http.HttpConstant.*;

/**
 * 鼠绘的模块比较多，可以单拎出来了
 */
public class ShuHuiMangaCrawler extends MangaCrawler {

    private static final Logger logger = LoggerFactory.getLogger(ShuHuiMangaCrawler.class);

    /**
     * 代表一部漫画
     */
    @Deprecated
    public static final String SHU_HUI_PREFIX_CARTOON = "http://www.ishuhui.com/cartoon/book";

    /**
     * 一部漫画中的某一话
     */
    @Deprecated
    public static final String SHU_HUI_PREFIX_CARTOON_NUM = "http://www.ishuhui.com/cartoon/num";

    /**
     * 一部漫画中某一话的真实地址
     */
    @Deprecated
    public static final String SHU_HUI_PREFIX_CARTOON_NUM_DETAIL = "http://hanhuazu.cc/cartoon/post";

    /**
     * 一部漫画中某一话的真实地址
     */
    @Deprecated
    public static final String SHU_HUI_PREFIX_CARTOON_NUM_DETAIL_2 = "http://www.ishuhui.com/cartoon/post";

    /**
     * 图片地址
     */
    @Deprecated
    public static final String SHU_HUI_PREFIX_PIC = "http://pic";

    private static final String SHU_HUI_IMG_PAGE_NO_ATTR = "alt";


    /**
     * 爬一整部漫画
     *
     * @param url
     * @throws Exception
     */
    @Deprecated
    public static void downMangaFromShuHui2017(String url) throws Exception {
//		List<String> hrefList = getMangaUrlFromShuHui2018(url);

        // 20180906，存在一部分漫画，可以直接获取单话地址，不用在转一下
        List<String> hrefList = Lists.newArrayList(url);

        List<String> realMangaUrlList = getRealMangaUrlFromShuHui2017(hrefList);
        Map<String, List<String>> allPicUrlMap = getAllPicUrlFromShuHui2017(realMangaUrlList);
        saveToLocalWithOriginName(allPicUrlMap);
    }

    /**
     * 爬取指定的某几话
     *
     * @param no
     * @throws Exception
     */
    @Deprecated
    public static void downMangaFromShuHui2017(int... no) throws Exception {
        List<String> urlList = Lists.newArrayList();
        for (int i : no) {
            String url = "http://hanhuazu.cc/cartoon/post?id=" + i;
            urlList.add(url);
        }
        Map<String, List<String>> allPicUrlMap = getAllPicUrlFromShuHui2017(urlList);
        saveToLocalWithOriginName(allPicUrlMap);
    }

    /**
     * 取一部漫画中，每一话的地址
     *
     * @param url
     * @return
     */
    @Deprecated
    public static List<String> getMangaUrlFromShuHui2017(String url) {
        WebDriver webDriver = SeleniumUtil.getWebDriver();
        webDriver.get(url);

        List<String> hrefList = Lists.newArrayList();
        List<WebElement> aTagList = webDriver.findElements(By.tagName(TAG_a));
        logger.error("共获取到{}个a标签，下面开始过滤", aTagList.size());
        for (WebElement a : aTagList) {
            String href = a.getAttribute("href");
            if (StringUtils.startsWith(href, SHU_HUI_PREFIX_CARTOON_NUM)) {
                logger.error("当前链接为：{} 符合条件", href);
                hrefList.add(href);
            } else {
                logger.error("当前链接为：{} 不符合条件，剔除", href);
            }
        }
        logger.error("过滤后的数量为{}", hrefList.size());

        webDriver.close();
        return hrefList;
    }

    /**
     * 取某一话的真实地址
     *
     * @param hrefList
     * @return
     */
    @Deprecated
    public static List<String> getRealMangaUrlFromShuHui2017(List<String> hrefList) {
        WebDriver webDriver = SeleniumUtil.getWebDriver();
        List<String> realMangaUrlList = Lists.newArrayList();
        for (String href : hrefList) {
            logger.error("开始访问{}", href);
            webDriver.get(href);
            List<WebElement> tempList = webDriver.findElements(By.tagName(TAG_a));
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (WebElement temp : tempList) {
                String realUrl = temp.getAttribute("href");
                boolean flag = StringUtils.startsWith(realUrl, SHU_HUI_PREFIX_CARTOON_NUM_DETAIL);
                flag |= StringUtils.startsWith(realUrl, SHU_HUI_PREFIX_CARTOON_NUM_DETAIL_2);
                if (flag) {
                    logger.error("找到真正的漫画url：{}", realUrl);
                    realMangaUrlList.add(realUrl);
                }
            }
        }
        logger.error("真实漫画Url的个数：{}", realMangaUrlList.size());

        webDriver.close();
        return realMangaUrlList;
    }

    /**
     * 取所有图片的url，同时返回某一话的标题，方便生成目录
     *
     * @param realMangaUrlList
     * @return
     */
    @Deprecated
    public static Map<String, List<String>> getAllPicUrlFromShuHui2017(List<String> realMangaUrlList) {
        WebDriver webDriver = SeleniumUtil.getWebDriver();

        Map<String, List<String>> allPicUrlMap = Maps.newHashMap();
        for (String realMangaUrl : realMangaUrlList) {
            logger.error("准备访问url：{}", realMangaUrl);
            webDriver.get(realMangaUrl);

            List<WebElement> imgElementList = webDriver.findElements(By.tagName(TAG_img));
            List<String> srcList = Lists.newArrayList();
            for (WebElement img : imgElementList) {
                String src = img.getAttribute("src");
                // vue.js的懒加载，某些图片会放置在这个属性里，但这个属性是不会有http://前缀的
                String dataSrc = img.getAttribute("data-src");
                if (StringUtils.startsWith(src, SHU_HUI_PREFIX_PIC)) {
                    logger.error("当前链接为：{} 符合条件", src);
                    srcList.add(src);
                } else if (StringUtils.contains(dataSrc, "pic")) {
                    dataSrc = "http:" + dataSrc;
                    srcList.add(dataSrc);
                    logger.error("当前链接为：{} 符合条件", dataSrc);
                } else {
                    logger.error("此对象没有找到合适的图片地址：{}", img.toString());
                }
            }

            Document document = Jsoup.parse(webDriver.getPageSource());
            allPicUrlMap.put(document.title(), srcList);
        }

        webDriver.close();
        return allPicUrlMap;
    }

    /**
     * 半自动，任需要主动提供<br/>
     * http://www.hanhuazu.cc/comics/detail/11495<br/>
     * 这种格式的url
     *
     * @param url
     * @param baseDirectory
     * @throws IOException
     */
    public static void getOneChapterFrom2018UI(String url, String baseDirectory) throws IOException {
        WebDriver webDriver = SeleniumUtil.getWebDriver();
        webDriver.get(url);

        // 获取标题
        WebElement mangaTitleElement = webDriver.findElement(By.tagName("h1"));
        String mangaTitle = mangaTitleElement.getText();

        // 获取连页模式的按钮，并点击
        WebElement continousPageModeElement = webDriver.findElement(By.xpath("//*[@class='tips z-page']"));
        continousPageModeElement.click();

        // 模拟页面到底，加载全部url
        ((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        try {
            // 等待js加载html，后续考虑优化
            Thread.sleep(1 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 获取所有图片
        List<WebElement> imgElementList = webDriver.findElements(By.tagName("img"));
        if (CollectionUtils.isEmpty(imgElementList)) {
            throw new RuntimeException("没有获取到图片");
        }

        Map<String, String> imgUrlMap = Maps.newHashMap();
        for (WebElement webElement : imgElementList) {
            String imgUrl = webElement.getAttribute(ATTR_src);
            String imgPageNO = webElement.getAttribute(SHU_HUI_IMG_PAGE_NO_ATTR);
            if (imgPageNO == null) {
                continue;
            }
            imgUrlMap.put(imgPageNO, imgUrl);
        }
        if (imgUrlMap.isEmpty()) {
            throw new RuntimeException("没有获取到图片");
        }

        webDriver.close(); // 在这就可以关闭了，下面都用不到

        // 创建文件夹下载图片
        mangaTitle = mangaTitle.replaceAll("海賊王", "海賊王 第");
        String chapterDir = baseDirectory + File.separator + mangaTitle;
        Set<Map.Entry<String, String>> imgUrlEntrySet = imgUrlMap.entrySet();
        for (Map.Entry<String, String> imgUrlEntry : imgUrlEntrySet) {
            String imgUrl = imgUrlEntry.getValue();
            String fileCanonicalName = imgUrlEntry.getKey();
            String fileAbsoluteName = chapterDir + File.separator + fileCanonicalName;
            FileUtil.createNewFileFromInternet(imgUrl, fileAbsoluteName);
        }
    }

    public static void main(String[] args) throws Exception {
        String url = "http://www.hanhuazu.cc/comics/detail/11755";
        String baseDir = "C:\\迅雷下载";
        getOneChapterFrom2018UI(url, baseDir);
    }

}
