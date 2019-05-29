package com.jojo.crawler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jojo.util.SeleniumUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.jojo.util.http.HttpConstant.TAG_a;
import static com.jojo.util.http.HttpConstant.TAG_img;

/**
 * 鼠绘的模块比较多，可以单拎出来了
 */
public class ShuHuiMangaCrawler extends MangaCrawler {

    private static final Logger logger = LoggerFactory.getLogger(ShuHuiMangaCrawler.class);

    /**
     * 代表一部漫画
     */
    public static final String SHU_HUI_PREFIX_CARTOON = "http://www.ishuhui.com/cartoon/book";

    /**
     * 一部漫画中的某一话
     */
    public static final String SHU_HUI_PREFIX_CARTOON_NUM = "http://www.ishuhui.com/cartoon/num";

    /**
     * 一部漫画中某一话的真实地址
     */
    public static final String SHU_HUI_PREFIX_CARTOON_NUM_DETAIL = "http://hanhuazu.cc/cartoon/post";

    /**
     * 一部漫画中某一话的真实地址
     */
    public static final String SHU_HUI_PREFIX_CARTOON_NUM_DETAIL_2 = "http://www.ishuhui.com/cartoon/post";

    /**
     * 图片地址
     */
    public static final String SHU_HUI_PREFIX_PIC = "http://pic";

    /**
     * 爬一整部漫画
     *
     * @param url
     * @throws Exception
     */
    public static void downMangaFromShuHui(String url) throws Exception {
//		List<String> hrefList = getMangaUrlFromShuHui(url);

        // 20180906，存在一部分漫画，可以直接获取单话地址，不用在转一下
        List<String> hrefList = Lists.newArrayList(url);

        List<String> realMangaUrlList = getRealMangaUrlFromShuHui(hrefList);
        Map<String, List<String>> allPicUrlMap = getAllPicUrlFromShuHui(realMangaUrlList);
        saveToLocalWithOriginName(allPicUrlMap);
    }

    /**
     * 爬取指定的某几话
     *
     * @param no
     * @throws Exception
     */
    public static void downMangaFromShuHui(int... no) throws Exception {
        List<String> urlList = Lists.newArrayList();
        for (int i : no) {
            String url = "http://hanhuazu.cc/cartoon/post?id=" + i;
            urlList.add(url);
        }
        Map<String, List<String>> allPicUrlMap = getAllPicUrlFromShuHui(urlList);
        saveToLocalWithOriginName(allPicUrlMap);
    }

    /**
     * 取一部漫画中，每一话的地址
     *
     * @param url
     * @return
     */
    public static List<String> getMangaUrlFromShuHui(String url) {
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
    public static List<String> getRealMangaUrlFromShuHui(List<String> hrefList) {
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
    public static Map<String, List<String>> getAllPicUrlFromShuHui(List<String> realMangaUrlList) {
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
}
