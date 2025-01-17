package com.jojo.util;

import cn.hutool.http.HttpUtil;
import com.google.common.base.Charsets;
import com.google.common.io.CharSink;
import com.google.common.io.CharSource;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtil {

    public static final String lineSeparator = System.getProperties().getProperty("line.separator");

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 文件重命名，将符合正则表达式的内容，替换为给定的字符串
     *
     * @param fileDirectory 文件目录
     * @param regexArray    正则表达式数组
     * @author jojo
     */
    public static void fileRename(File fileDirectory, String replacement, String... regexArray) {
        if (ArrayUtils.isEmpty(regexArray) || fileDirectory == null) {
//			logger.error("参数不得为空");
            return;
        }

        // 先改名，然后递归
        String path = fileDirectory.getAbsolutePath();
        for (String regex : regexArray) {
            path = path.replaceAll(regex, replacement);
        }

        fileDirectory.renameTo(new File(path));

        if (fileDirectory.isDirectory()) {
            for (File file : fileDirectory.listFiles()) {
                fileRename(file, replacement, regexArray);
            }
        }
    }

    /**
     * 根据正则表达式，递归删除符合条件的文件
     *
     */
    public static void fileDelete(String filePath, String[] regexArr) {
        if (StringUtils.isBlank(filePath) || StringUtils.isAnyBlank(regexArr)) {
            logger.error("参数不得为空");
            return;
        }

        File file = new File(filePath);
        if (file.isDirectory()) {
            for (File tempFile : file.listFiles()) {
                fileDelete(tempFile.getAbsolutePath(), regexArr);
            }
        }

        for (String str : regexArr) {
            if (file.getAbsolutePath().matches(str)) {
                file.delete();
                break;
            }
        }
    }

    /**
     * 解压文件至目标文件夹
     *
     * @param filePath
     * @param destinationPath
     */
    public static void fileDecompress(String filePath, String destinationPath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            logger.error("要解压的文件不存在");
            return;
        } else if (StringUtils.isBlank(destinationPath) || !new File(destinationPath).isDirectory()) {
            logger.error("保存位置无效");
            return;
        }

        BufferedInputStream bufferedIn = null;
        BufferedOutputStream bufferedOut = null;
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
            while (enumeration.hasMoreElements()) {
                ZipEntry zipEntry = enumeration.nextElement();

                // 生成特定文件名
                String fileName = generateFileName(destinationPath, zipFile, zipEntry);

                // 根据生成的文件名，创建文件或目录
                File targetFile = createNewFile(fileName);
                if (targetFile.isDirectory()) {
                    continue;
                }

                // 读取内容
                bufferedIn = new BufferedInputStream(zipFile.getInputStream(zipEntry));
                bufferedOut = new BufferedOutputStream(new FileOutputStream(targetFile));

                byte[] container = new byte[10 * 1024];
                int lengthOfReadByte = 0;
                while ((lengthOfReadByte = bufferedIn.read(container)) != -1) {
                    bufferedOut.write(container, 0, lengthOfReadByte);
                }
                bufferedOut.flush();
            }
        } finally {
            IOUtils.closeQuietly(bufferedIn, bufferedOut, zipFile);
        }
    }

    /**
     * 生成特定文件名
     *
     * @param destinationPath
     * @param zipFile
     * @param zipEntry
     * @return
     */
    private static String generateFileName(String destinationPath, ZipFile zipFile, ZipEntry zipEntry) {
        StringBuffer result = new StringBuffer();
        result.append(destinationPath + File.separator);
        // 提取压缩文件名，并作为文件夹
        String zipFileName = zipFile.getName();
        int temp = zipFileName.lastIndexOf(File.separator);
        // 如果存在文件分隔符
        if (temp != -1) {
            result.append(zipFileName.substring(temp + 1).split("\\.")[0]);
        } else {
            result.append(zipFileName);
        }
        result.append(File.separator);
        result.append(zipEntry.getName());

        return result.toString();
    }

    /**
     * 在指定目录创建一个文件
     *
     * @param directory
     * @return
     * @throws IOException
     */
    public static File createFileNamedByDateTime(String directory, String suffix) throws IOException {
        String timeStr = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
        String filename = directory + timeStr + "." + suffix;
        File file = new File(filename);
        Files.createParentDirs(file);
        file.createNewFile();
        return file;
    }

    /**
     * 根据生成的文件名，创建文件或目录
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static File createNewFile(String fileName) throws IOException {
        if (StringUtils.isBlank(fileName)) {
            return null;
        }
        File file = new File(fileName);
        if (file.exists()) {
            return file;
        }
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        // 如果是目录，则不需要读取内容
        if (!fileName.matches(".+\\.\\w+$")) {
            if (!file.exists()) {
                file.mkdirs();
            }
        } else {
            file.createNewFile();
        }
        return file;
    }

    /**
     * 根据url，将数据保存至本地
     *
     * @param url
     * @param fileName
     * @throws IOException
     */
    public static void createNewFileFromInternet(String url, String fileName) throws IOException {
        if (StringUtils.startsWith(url, "//")) {
            url = "http:" + url;
        }

        try {
            // 创建文件
            File file = new File(fileName);
            Files.createParentDirs(file);
            file.createNewFile();
            logger.error("创建文件 url：{}，filename：{}", url, fileName);

            // 这段代码没办法处理https
//            byte[] arr = IOUtils.toByteArray(new URL(url));
//            Files.write(arr, file);

            HttpUtil.downloadFile(url, fileName);
            logger.error(fileName + "数据填充完毕");
        } catch (Exception e) {
            logger.error("创建文件失败", e);
        }
    }

    /**
     * 过滤windows环境下的无效字符，只支持文件名，不支持过滤完整目录
     *
     * @param fileName
     * @return
     */
    public static String filterInvalidCharacter(String fileName) {
        if (StringUtils.contains(fileName, "\\")) {
            fileName = fileName.replaceAll("\\\\", " ");
        }
        if (StringUtils.contains(fileName, "/")) {
            fileName = fileName.replaceAll("/", " ");
        }
        if (StringUtils.contains(fileName, ":")) {
            fileName = fileName.replaceAll(":", " ");
        }
        if (StringUtils.contains(fileName, "*")) {
            fileName = fileName.replaceAll("\\*", " ");
        }
        if (StringUtils.contains(fileName, "?")) {
            fileName = fileName.replaceAll("\\?", " ");
        }
        if (StringUtils.contains(fileName, "\"")) {
            fileName = fileName.replaceAll("\"", " ");
        }
        if (StringUtils.contains(fileName, "<")) {
            fileName = fileName.replaceAll("<", " ");
        }
        if (StringUtils.contains(fileName, ">")) {
            fileName = fileName.replaceAll(">", " ");
        }
        if (StringUtils.contains(fileName, "|")) {
            fileName = fileName.replaceAll("\\|", " ");
        }

        return StringUtils.trim(fileName);
    }

    public static String filterInvalidCharacterRefactor(String fileName) {
        String[] invalidCharacterArr = {};
        for (int i = 0; i < invalidCharacterArr.length; i++) {
            fileName = fileName.replaceAll(invalidCharacterArr[i], " ");
        }
        return fileName;
    }


    /**
     * 读取
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String getContentFromFile(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            logger.error("指定文件不存在");
            return null;
        }
        BufferedReader reader = Files.newReader(file, Charsets.UTF_8);
        return IOUtils.toString(reader);
    }

    /**
     * @param filepath
     * @return
     */
    public static String getSuffixName(String filepath) {
        if (StringUtils.isBlank(filepath)) {
            return null;
        }
        return filepath.substring(filepath.lastIndexOf(".") + 1);
    }

    /**
     * @param baseDir
     * @param targetFilePath
     * @throws IOException
     */
    public static final void assembleAllTextToOneFile(String baseDir, String targetFilePath) throws IOException {
        File baseDirFile = new File(baseDir);
        if (!baseDirFile.isDirectory()) {
            return;
        }

        File targetFile = createNewFile(targetFilePath);

        List<File> fileList = Arrays.asList(baseDirFile.listFiles());
        Collections.sort(fileList, (o1, o2) -> {
            char[] o1Name = o1.getName().toCharArray();
            char[] o2Name = o2.getName().toCharArray();
            int i = 0, j = 0;
            while (i < o1Name.length && j < o2Name.length) {
                if (Character.isDigit(o1Name[i]) && Character.isDigit(o2Name[j])) {
                    String s1 = "", s2 = "";
                    while (i < o1Name.length && Character.isDigit(o1Name[i])) {
                        s1 += o1Name[i];
                        i++;
                    }
                    while (j < o2Name.length && Character.isDigit(o2Name[j])) {
                        s2 += o2Name[j];
                        j++;
                    }
                    if (Integer.parseInt(s1) > Integer.parseInt(s2)) {
                        return 1;
                    } else if (Integer.parseInt(s1) < Integer.parseInt(s2)) {
                        return -1;
                    }

                } else {
                    if (o1Name[i] > o2Name[j]) {
                        return 1;
                    } else if (o1Name[i] < o2Name[j]) {
                        return -1;
                    } else {
                        i++;
                        j++;
                    }
                }
            }
            if (o1Name.length == o2Name.length) {
                return 0;
            } else {
                return o1Name.length > o2Name.length ? 1 : -1;
            }
        });

        CharSink charSink = Files.asCharSink(targetFile, Charsets.UTF_8, FileWriteMode.APPEND);
        for (File sourceFile : fileList) {
            CharSource charSource = Files.asCharSource(sourceFile, Charsets.UTF_8);
            charSource.copyTo(charSink);
        }

    }

    public static final void appendContent(File targetFile, String content) throws IOException {
        if (StringUtils.isBlank(content)) {
            return;
        }

        CharSink charSink = Files.asCharSink(targetFile, Charsets.UTF_8, FileWriteMode.APPEND);
        charSink.write(content + lineSeparator);
    }

    public static final void appendContent(File targetFile, List<String> contentList) throws IOException {
        if (CollectionUtils.isEmpty(contentList)) {
            return;
        }
        CharSink charSink = Files.asCharSink(targetFile, Charsets.UTF_8, FileWriteMode.APPEND);
        charSink.writeLines(contentList);
    }

    public static File[] listFiles(String path) {
        File file = new File(path);
        return file.listFiles();
    }

    public static void main(String[] args) throws Exception {
        File fileDirectory = new File("E:\\看的\\漫画\\猎人");
        fileRename(fileDirectory, "猎人", "猎人 第 ");


    }
}
