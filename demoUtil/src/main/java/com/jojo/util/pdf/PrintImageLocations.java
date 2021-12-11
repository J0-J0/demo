package com.jojo.util.pdf;

import com.jojo.util.FileUtil;
import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.apache.pdfbox.contentstream.operator.DrawObject;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.contentstream.operator.OperatorName;
import org.apache.pdfbox.contentstream.operator.state.*;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PrintImageLocations extends PDFStreamEngine {

    private static int counter = 0;

    /**
     * Default constructor.
     *
     * @throws IOException If there is an error loading text stripper properties.
     */
    public PrintImageLocations() throws IOException {
        addOperator(new Concatenate());
        addOperator(new DrawObject());
        addOperator(new SetGraphicsStateParameters());
        addOperator(new Save());
        addOperator(new Restore());
        addOperator(new SetMatrix());
    }


    /**
     * This is used to handle an operation.
     *
     * @param operator The operation to perform.
     * @param operands The list of arguments.
     * @throws IOException If there is an error processing the operation.
     */
    @Override
    protected void processOperator(Operator operator, List<COSBase> operands) throws IOException {
        String operation = operator.getName();
        if (OperatorName.DRAW_OBJECT.equals(operation)) {
            COSName objectName = (COSName) operands.get(0);
            PDXObject xobject = getResources().getXObject(objectName);
            if (xobject instanceof PDImageXObject) {
                PDImageXObject image = (PDImageXObject) xobject;
                BufferedImage bufferedImage = image.getImage();

                String picName = "C:\\Workspace\\test\\" + (++counter) + ".jpg";
                FileUtil.createNewFile(picName);
                System.out.println("准备生成图片" + picName);
                ImageIO.write(bufferedImage, "JPEG", new FileOutputStream(picName));// 保存图片 JPEG表示保存格式


//                int imageWidth = image.getWidth();
//                int imageHeight = image.getHeight();
//                System.out.println("*******************************************************************");
//                System.out.println("Found image [" + objectName.getName() + "]");
//
//                Matrix ctmNew = getGraphicsState().getCurrentTransformationMatrix();
//                float imageXScale = ctmNew.getScalingFactorX();
//                float imageYScale = ctmNew.getScalingFactorY();
//
//                // position in user space units. 1 unit = 1/72 inch at 72 dpi
//                System.out.println("position in PDF = " + ctmNew.getTranslateX() + ", " + ctmNew.getTranslateY() + " in user space units");
//                // raw size in pixels
//                System.out.println("raw image size  = " + imageWidth + ", " + imageHeight + " in pixels");
//                // displayed size in user space units
//                System.out.println("displayed size  = " + imageXScale + ", " + imageYScale + " in user space units");
//                // displayed size in inches at 72 dpi rendering
//                imageXScale /= 72;
//                imageYScale /= 72;
//                System.out.println("displayed size  = " + imageXScale + ", " + imageYScale + " in inches at 72 dpi rendering");
//                // displayed size in millimeters at 72 dpi rendering
//                imageXScale *= 25.4;
//                imageYScale *= 25.4;
//                System.out.println("displayed size  = " + imageXScale + ", " + imageYScale + " in millimeters at 72 dpi rendering");
//                System.out.println();
            }
        } else {
            super.processOperator(operator, operands);
        }
    }

}
