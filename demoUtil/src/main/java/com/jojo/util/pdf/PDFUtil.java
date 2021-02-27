package com.jojo.util.pdf;

import com.alibaba.fastjson.JSON;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class PDFUtil {


    public static void main(String[] args) throws Exception {
        String path = "C:\\WorkSpace\\test\\01-29话.pdf";
        PdfReader pdfReader = new PdfReader(path);

        PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream("C:\\WorkSpace\\test\\out.pdf"));
        PdfWriter pdfWriter = pdfStamper.getWriter();

        Image img = Image.getInstance("C:\\WorkSpace\\test\\image.jpg");
        PdfDictionary page1 = pdfReader.getPageN(1);
        PdfDictionary page1Resources = (PdfDictionary) PdfReader.getPdfObject(page1.get(PdfName.RESOURCES));
        PdfDictionary page1XObject = (PdfDictionary) PdfReader.getPdfObject(page1Resources.get(PdfName.XOBJECT));
        if (page1XObject == null) {
            return;
        }

        Set<PdfName> page1XObjectKeys = page1XObject.getKeys();
        int i = 1;
        for (PdfName page1XObjectKey : page1XObjectKeys) {
            System.out.println(i++);
            PdfObject obj = page1XObject.get(page1XObjectKey);
            if (obj.isIndirect()) {
                PdfDictionary tg = (PdfDictionary) PdfReader.getPdfObject(obj);
                PdfName type = (PdfName) PdfReader.getPdfObject(tg.get(PdfName.SUBTYPE));
                if (PdfName.IMAGE.equals(type)) {
                    PdfReader.killIndirect(obj);
                    Image maskImage = img.getImageMask();
                    if (maskImage != null) {
                        pdfWriter.addDirectImageSimple(maskImage);
                    }
                    pdfWriter.addDirectImageSimple(img, (PRIndirectReference) obj);
                    break;
                }
            }
        }

        pdfStamper.close();
    }

}
