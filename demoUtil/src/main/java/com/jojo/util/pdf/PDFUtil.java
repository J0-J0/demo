package com.jojo.util.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;

public class PDFUtil {


    public static void main(String[] args) throws Exception {
        String filePath = "C:\\Users\\flash.wg\\Downloads\\上个世纪日本泡沫时代究竟是怎样的一副光景？到底繁荣到什么程度？ - 知乎.pdf";

        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PrintImageLocations printer = new PrintImageLocations();
            int pageNum = 0;
            for (PDPage page : document.getPages()) {
                pageNum++;
                System.out.println("Processing page: " + pageNum);
                printer.processPage(page);
            }
        }
    }

}
