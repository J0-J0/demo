package com.jojo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtil {

    public static final boolean horizontal = true;

    public static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    public static BufferedImage getBufferedImage(String fileUrl) throws IOException {
        File f = new File(fileUrl);
        return ImageIO.read(f);
    }

    private static void saveImage(BufferedImage buffImg, String savePath) throws IOException {
        File outFile = FileUtil.createNewFile(savePath);

        int temp = savePath.lastIndexOf(".") + 1;
        ImageIO.write(buffImg, savePath.substring(temp), outFile);
        logger.info("图片保存完毕，新路径为：{}", savePath);
    }

    public static BufferedImage mergeImage(BufferedImage img1, BufferedImage img2, boolean isHorizontal) throws IOException {
        int w1 = img1.getWidth(), h1 = img1.getHeight();
        int w2 = img2.getWidth(), h2 = img2.getHeight();

        // 从图片中读取RGB
        int[] imgArr1 = new int[w1 * h1];
        imgArr1 = img1.getRGB(0, 0, w1, h1, imgArr1, 0, w1); // 逐行扫描图像中各个像素的RGB到数组中
        int[] imgArr2 = new int[w2 * h2];
        imgArr2 = img2.getRGB(0, 0, w2, h2, imgArr2, 0, w2);

        // 生成新图片
        BufferedImage finalImage = null;
        if (isHorizontal) {
            // 水平方向合并
            int newHeight = h1 > h2 ? h1 : h2;
            finalImage = new BufferedImage(w1 + w2, newHeight, BufferedImage.TYPE_INT_RGB);
            finalImage.setRGB(0, 0, w1, h1, imgArr1, 0, w1); // 设置上半部分或左半部分的RGB
            finalImage.setRGB(w1, 0, w2, h2, imgArr2, 0, w2);
        } else {
            // 垂直方向合并
            int newWidth = w1 > w2 ? w1 : w2;
            finalImage = new BufferedImage(newWidth, h1 + h2, BufferedImage.TYPE_INT_RGB);
            finalImage.setRGB(0, 0, w1, h1, imgArr1, 0, w1); // 设置上半部分或左半部分的RGB
            finalImage.setRGB(0, h1, w2, h2, imgArr2, 0, w2); // 设置下半部分的RGB
        }

        return finalImage;
    }

    public static void mergeManga(String mangaPath) throws IOException {
        mergeManga(mangaPath, 0);
    }

    public static void mergeManga(String mangaPath, int startNo) throws IOException {
        File[] files = FileUtil.listFiles(mangaPath);

        // 定义好保存路径
        String parentName = files[0].getParentFile().getName();
        String parentDir = files[0].getParentFile().getParentFile().getAbsolutePath();
        String saveDir = parentDir + File.separator + parentName + "_merged";

        int endNo = (files.length % 2 == 0) ? files.length : files.length - 1;
        for (int i = startNo; i < endNo; i += 2) {
            File file1 = files[i], file2 = files[i + 1];
            BufferedImage image1 = ImageIO.read(file1), image2 = ImageIO.read(file2);
            logger.info("图片加载完毕，准备拼接");

            // 日本漫画的阅读习惯，从右往左
            BufferedImage finalImage = mergeImage(image2, image1, horizontal);
            String finalName = generateFileName(file1, file2, saveDir);
//            String finalName = saveDir + File.separator + i +"-"+(i+1)+".jpg";
            saveImage(finalImage, finalName);
        }

        // 判断最后一张图片是否需要迁移
        if (endNo < files.length) {
            files[endNo].renameTo(new File(saveDir + File.separator + files[endNo].getName()));
        }
    }

    private static String generateFileName(File file1, File file2, String saveDir) {
        String filename1 = file1.getName(), filename2 = file2.getName();
        String finalName = filename1.substring(0, filename1.indexOf('.'))
                + "-"
                + filename2.substring(0, filename2.indexOf('.'))
                + filename1.substring(filename1.indexOf('.'));

        return saveDir + File.separator + finalName;
    }

    public static void main(String[] args) throws IOException {
        String mangaPath = "D:\\workspace\\test\\7";
        mergeManga(mangaPath, 0);
    }
}
