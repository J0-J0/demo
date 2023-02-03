package com.jojo.crawler;

import cn.hutool.http.HttpUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.jojo.util.FileUtil;
import com.jojo.util.RegexUtil;
import com.jojo.util.SeleniumUtil;
import org.apache.commons.collections4.CollectionUtils;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.jojo.util.http.HttpConstant.ATTR_href;
import static com.jojo.util.http.HttpConstant.TAG_a;

public class MangaCrawlerCase extends MangaCrawler {

    private static final Logger logger = LoggerFactory.getLogger(MangaCrawlerCase.class);


    /**
     * feiwan 网图片地址url正则
     */
    public static String FEIWAN_PICURL_REGEX = "http(s?)://img.feiwan.net/[\\w\\d/]+\\.(jpg|png|jpeg)";

    /**
     * 动漫之家图片地址
     */
    public static String DMZJ_PICURL_REGEX = "images.dmzj.com/y/[%_\\w/]+/\\d+\\.(jpg|png|jpeg)";


    /**
     * @param domainName 这个是域名，因为feiwan网会为某一部漫画直接设置一个域名。<br>
     *                   url是restful风格，如果是漫画，那就域名后再跟一个/manhua<br>
     *                   第几话就后面数字加.html<br>
     * @return
     */
    @Deprecated
    public static List<String> getMangaUrlFromFeiWan(String domainName) throws Exception {
        List<String> urlList = Lists.newArrayList();

        String url = domainName + "/xiangguan/";
        String mangaRegex = domainName + "/\\d+\\.html";

        Document document = Jsoup.parse(new URL(url), 10 * 1000);
        Elements elements = document.getElementsByTag(TAG_a);

        for (Element element : elements) {
            String string = element.attr(ATTR_href);
            if (string.matches(mangaRegex)) {
                urlList.add(string);
            }
        }
        return urlList;
    }

    /**
     * 获取所有图片的url，feiwan网把所有的图片都放在<script>块内
     *
     * @param mangaUrlList
     * @return
     * @throws Exception
     */
    @Deprecated
    private static Map<String, List<String>> getAllPicUrlFromFeiWan(List<String> mangaUrlList) throws Exception {
        Map<String, List<String>> picUrlMap = Maps.newHashMap();

        for (String mangaUrl : mangaUrlList) {
            Document document = Jsoup.parse(new URL(mangaUrl), 10 * 1000);
            // 每一话的title
            String title = document.title();

            Pattern pattern = Pattern.compile(FEIWAN_PICURL_REGEX);
            Matcher matcher = pattern.matcher(document.toString());

            while (matcher.find()) {
                String picUrl = matcher.group();
                logger.error("找到：{}", picUrl);
                if (picUrlMap.get(title) == null) {
                    List<String> picUrlList = Lists.newArrayList(picUrl);
                    picUrlMap.put(title, picUrlList);
                } else {
                    picUrlMap.get(title).add(picUrl);
                }
            }
        }

        return picUrlMap;
    }

    /**
     * @param domainName
     * @throws Exception
     */
    @Deprecated
    public static void downMangaFromFeiWan(String domainName) throws Exception {
        List<String> mangaUrlList = getMangaUrlFromFeiWan(domainName);
        Map<String, List<String>> allPicUrlMap = getAllPicUrlFromFeiWan(mangaUrlList);
        saveToLocalWithOriginName(allPicUrlMap);
    }

    /**
     * 从动漫之家爬漫画
     *
     * @throws Exception
     * @throws FileNotFoundException
     */
    @Deprecated
    public static void downMangaFromDMZJ() throws Exception {
        String filepath = "C:\\Users\\Administrator\\Desktop\\新建文本文档.txt";
        Map<String, String> titleAndUrl = getTitleAndUrlFromFile(filepath);

        Map<String, List<String>> picUrlMap = Maps.newHashMap();
        WebDriver webDriver = SeleniumUtil.getWebDriver();

        for (Map.Entry<String, String> entry : titleAndUrl.entrySet()) {
            List<String> urlList = Lists.newArrayList();

            webDriver.get(entry.getValue());
            String html = webDriver.getPageSource();

            Pattern p = Pattern.compile(DMZJ_PICURL_REGEX);
            Matcher m = p.matcher(html);
            while (m.find()) {
                String str = m.group();
                System.out.println("找到目标" + str);
                urlList.add("https://" + str);
            }

            picUrlMap.put(entry.getKey(), urlList);
        }

        saveToLocalWithOriginName(picUrlMap);

    }

    /**
     * @param pageFlag 是否遍历分页
     * @throws Exception
     */
    @Deprecated
    public static void downMangaFromTieBa(boolean pageFlag) throws Exception {
        Map<String, String> titleAndUrl = getTitleAndUrlFromFile(PATH_OF_TITLE_AND_URL);

        Map<String, List<String>> picUrlMap = Maps.newHashMap();

        for (Map.Entry<String, String> entry : titleAndUrl.entrySet()) {
            String url = entry.getValue();
            List<String> urlList = getPicUrlFromTieBa(url, pageFlag);
            picUrlMap.put(entry.getKey(), urlList);
        }

        saveToLocal(picUrlMap);
    }

    /**
     * 贴吧有分页，这是比较麻烦的地方<br>
     * 如果“没有获取到图片”的次数，与“本次获取的图片数量与上次获取的数量一致”<br>
     * 的次数，相加超过某个数，那么，就判断读到末尾。<br>
     * 另外这个方法不具备通用性，截止至目前为止，我都是靠class来获取图片的
     *
     * @param url
     * @param pageFlag 是否遍历分页
     * @return
     * @throws IOException
     */
    @Deprecated
    private static List<String> getPicUrlFromTieBa(String url, boolean pageFlag) throws IOException {
        List<Element> elementList = Lists.newArrayList();
        int page = 1;// 起始页
        int shutdownCount = 0;// 终结次数
        int urlNumberOfOnePage = 0;// 某一页的图片数量
        try {
            while (true) {
                String urlWithPage = url;
                if (page != 1) {
                    urlWithPage += "?pn=" + page;
                }
                logger.error("开始访问：{}", urlWithPage);
                Document document = Jsoup.parse(new URL(urlWithPage), 10 * 1000);
                Elements imgElementList = document.getElementsByClass("BDE_Image");
                // 我不敢合并list，只能先写个size小于10
                if (CollectionUtils.isEmpty(imgElementList) || imgElementList.size() < 8) {
                    imgElementList = document.getElementsByClass("d_content_img");
                }

                if (CollectionUtils.isEmpty(imgElementList)) {
                    logger.error("毛都没获取到");
                    shutdownCount++;
                } else {
                    // 塞值
                    elementList.addAll(imgElementList);
                    // 跳出计数
                    if (urlNumberOfOnePage == imgElementList.size()) {
                        logger.error("本次获取数量与上次一致");
                        shutdownCount++;
                    } else {
                        urlNumberOfOnePage = imgElementList.size();
                        logger.error("本次获取数量：{}", urlNumberOfOnePage);
                    }
                }
                page++;
                // 这个9次，先将就着用吧
                if (shutdownCount > 6 || !pageFlag) {
                    break;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // 结果转换，搞不好会报空指针，不想管了
        List<String> urlList = elementList.stream().map(ELEMENT_TO_URL_FUNCTION).collect(Collectors.toList());

        // 去重
        logger.error("此时list长度为{}", urlList.size());
        LinkedHashSet<String> tempSet = Sets.newLinkedHashSet(urlList);
        urlList = Lists.newArrayList(tempSet);
        logger.error("去重后长度为{}", urlList.size());

        return urlList;
    }

    /**
     * 从第一会所拿图片，这个破网站会通过sessionid来确认登录信息，<br>
     * 所以只能把源代码保存至本地后，通过正则提取图片url（其实jsoup也可以，不过写正则方便点）<br>
     *
     * @param regex
     * @throws Exception
     */
    @Deprecated
    public static void sis01(String regex) throws Exception {
        String content = FileUtil.getContentFromFile(PATH_OF_TITLE_AND_URL);
        List<String> urlList = RegexUtil.getMatchedString(content, regex);

        // 去重
        Set<String> tempSet = Sets.newLinkedHashSet(urlList);

        urlList.clear();
        urlList.addAll(tempSet);

        Map<String, List<String>> allPicUrlMap = Maps.newHashMap();
        allPicUrlMap.put("sis01", urlList);
        saveToLocal(allPicUrlMap);
    }

    @Deprecated
    public static void manhuadbDownSingleChapter(String baseUrl, String baseDir, int pages) {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 1; i < pages; i++) {
            String url = "";
            if (i == 1) {
                url = baseUrl + ".html";
            } else {
                url = baseUrl + "_p" + i + ".html";
            }
            String finalUrl = url;
            int finalI = i;
            executorService.execute(() -> {
                try {
                    manhuadbDownSingleImg(finalUrl, finalI, baseDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
    }

    @Deprecated
    private static void manhuadbDownSingleImg(String url, int seq, String baseDir) throws IOException {
        logger.error("开始处理{}", url);
        Document document = Jsoup.parse(new URL(url), 60 * 1000);
        Elements elementList = document.getElementsByClass("img-fluid");

        String canonicalImgUrl = null;
        for (Element element : elementList) {
            canonicalImgUrl = element.attr("src");
            if (StringUtils.contains(canonicalImgUrl, "ccbaike")) {
                break;
            }
        }
        if (StringUtils.isBlank(canonicalImgUrl)) {
            logger.error("未获取到图片相对路径");
            return;
        }
        String imgUrl = "https://www.manhuadb.com" + canonicalImgUrl;
        String fileName = baseDir + seq + "." + RegexUtil.getSuffixFromUrl(imgUrl);
        FileUtil.createNewFileFromInternet(imgUrl, fileName);
    }

    /**
     * @param baseDirectory
     * @param url
     * @throws IOException
     */
    @Deprecated
    public static void downMangaFromOnePiece(String baseDirectory, String url) throws IOException {
        Document document = Jsoup.connect(url).execute().parse();

        Elements imgElementList = document.getElementsByTag("img");
        if (CollectionUtils.isEmpty(imgElementList)) {
            return;
        }
        int imgPageNO = 0;
        Map<String, String> imgUrlMap = Maps.newHashMap();
        for (Element imgElement : imgElementList) {
            String imgUrl = imgElement.attr("src");
            if (imgUrl.contains("logo")) {// 不要logo图片
                continue;
            }
            imgUrlMap.put((++imgPageNO) + "", imgUrl);
        }

        // 创建文件夹下载图片
        String chapterDir = baseDirectory + File.separator + "海贼";
        Set<Map.Entry<String, String>> imgUrlEntrySet = imgUrlMap.entrySet();
        for (Map.Entry<String, String> imgUrlEntry : imgUrlEntrySet) {
            String imgUrl = imgUrlEntry.getValue();
            String fileCanonicalName = imgUrlEntry.getKey();
            String fileSuffix = RegexUtil.getSuffixFromUrl(imgUrl);
            String fileAbsoluteName = chapterDir + File.separator + fileCanonicalName + "." + fileSuffix;
            FileUtil.createNewFileFromInternet(imgUrl, fileAbsoluteName);
        }
    }


    public static void wnacg() {
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
