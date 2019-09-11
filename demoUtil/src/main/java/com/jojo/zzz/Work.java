
package com.jojo.zzz;

import com.jojo.util.FileUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Work {
    public static void main(String[] args) throws Throwable {
        String filepath = "C:\\Users\\72669\\Desktop\\新建文本文档.txt";
        String contentFromFile = FileUtil.getContentFromFile(filepath);

        Document document = Jsoup.parse(contentFromFile);
        Elements elementList = document.getElementsByTag("h2");
//        System.out.println(elementList);

        Element postmessage_105213671Element = document.getElementById("postmessage_105213671");
        System.out.println(postmessage_105213671Element);
    }
}