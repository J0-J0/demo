package com.jojo.util.crawler;

import com.github.stuxuhai.jpinyin.ChineseHelper;
import com.jojo.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NovelCrawler {

    public static final Logger logger = LoggerFactory.getLogger(NovelCrawler.class);


    public static void sszzCrawler() throws Exception {
        String url = "https://www.2015txt.com/book/7410/";
        String sszzDir = "C:\\Workspace\\test\\新建文件夹";

        Document document = Jsoup.connect(url).execute().parse();
        Element allChapterEle = document.getElementById("all-chapter");
        Elements aList = allChapterEle.getElementsByTag("a");

        for (Element element : aList) {
            String singleChapterUrl = "https://www.2015txt.com" + element.attr("href");
            sszzCrawlerSingle(sszzDir, singleChapterUrl);
        }
    }

    private static void sszzCrawlerSingle(String sszzDir, String singleChapterUrl) throws Exception {
        Document document = Jsoup.connect(singleChapterUrl).execute().parse();
        // 解析title，并新建文件
        String chapterTitle = document.getElementsByClass("cont-title").first().text();
        logger.info("===准备写入==={}", chapterTitle);
        File singleChapterFile = FileUtil.createNewFile(sszzDir + File.separator + chapterTitle + ".txt");

        List<String> contentList = new ArrayList<>();
        contentList.add(chapterTitle + FileUtil.lineSeparator);
        // 解析html提取段落文字，并写入
        extractCharacter(document, contentList);
        // 剩余分页读取
        int pageSize = document.getElementsByClass("pagination pagination-sm").first().children().size();
        for (int i = 1; i < pageSize; i++) {
            String newUrl = singleChapterUrl.replaceFirst("\\.html", "_" + (i + 1) + ".html");
            extractCharacter(Jsoup.connect(newUrl).execute().parse(), contentList);
        }
        // 补上换行符
        contentList.add(FileUtil.lineSeparator + FileUtil.lineSeparator + FileUtil.lineSeparator);

        // 写入文件，写前转换成简体
        contentList = contentList.stream().map(ChineseHelper::convertToSimplifiedChinese).collect(Collectors.toList());
        FileUtil.appendContent(singleChapterFile, contentList);
        logger.info("==={}===写入完毕", chapterTitle);
    }

    private static void extractCharacter(Document document, List<String> contentList) {
        Elements p = document.getElementsByTag("p");
        for (Element element : p) {
            if (StringUtils.equals(element.className(), "hidden-xs")) {
                continue;
            } else if (StringUtils.startsWith(element.text(), "有能力者，請壹定訂閱和購買正版書籍支持作者")) {
                continue;
            }
            contentList.add(element.text());
        }
    }


    public static void main(String[] args) throws Exception {
        sszzCrawler();
    }

}
