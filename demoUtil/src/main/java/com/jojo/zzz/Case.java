package com.jojo.zzz;

import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.jojo.util.FileUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Case {

    /**
     * 将《棚车少年》压缩包，读至TXT文档 <br>
     * 这是2017年最后一段代码，当前时间是21:44
     *
     * @author jojo
     */
    public static void createBoxCarTXT() throws Exception {
        // target
        File targetFile = new File("D:\\Workspace\\test\\box car children.txt");
        targetFile.createNewFile();

        // source
        File sourceFiles = new File("D:\\Workspace\\test\\BoxCarChildren");

        // tool
        BufferedWriter finalWriter = null;
        ZipFile zipFile = null;
        try {
            finalWriter = new BufferedWriter(new FileWriter(targetFile, true));

            int count = 0;
            for (File epub : sourceFiles.listFiles()) {
                System.out.println("=========正在处理第" + (++count) + "个文件：" + epub.getAbsolutePath());
                zipFile = new ZipFile(epub);
                Enumeration<? extends ZipEntry> enumeration = zipFile.entries();

                // sort
                List<ZipEntry> zipEntryList = new ArrayList<ZipEntry>();
                while (enumeration.hasMoreElements()) {
                    zipEntryList.add(enumeration.nextElement());
                }
                Collections.sort(zipEntryList, getZipEntryComparator());

                // write
                for (ZipEntry zipEntry : zipEntryList) {
                    if (zipEntry.getName().matches(".*html$")) {
                        System.out.println("即将把：" + zipEntry.getName() + "写入txt");
                        InputStream inputStream = zipFile.getInputStream(zipEntry);
                        SAXReader saxReader = new SAXReader();
                        Document document = saxReader.read(inputStream);
                        Element rootElement = document.getRootElement();
                        finalWriter.write(rootElement.getStringValue());
                        finalWriter.flush();
                    }
                }
                System.out.println("处理完毕，执行关闭操作");
                zipFile.close();
                System.out.println();
            }
        } finally {
            IOUtils.closeQuietly(finalWriter, zipFile);
        }
    }

    /**
     * @return
     */
    private static Comparator<ZipEntry> getZipEntryComparator() {
        return new Comparator<ZipEntry>() {
            @Override
            public int compare(ZipEntry o1, ZipEntry o2) {
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
            }
        };
    }

    /**
     * 2018-08-10
     *
     * @throws Exception
     */
    public static void createBoxCarTXT2() throws Exception {
        String baseSaveDir = "D:\\Workspace\\test\\";
        String sourceDir = "D:\\Workspace\\test\\BoxCarChildren\\";
        // source
        File sourceFiles = new File(sourceDir);
        int count = 0;
        for (File epub : sourceFiles.listFiles()) {
            System.out.println("=========正在处理第" + (++count) + "个文件：" + epub.getAbsolutePath());
            String fileName = epub.getAbsolutePath();
            if (!StringUtils.endsWith(fileName, "epub")) {
                System.out.println("非epub文件，退出");
                continue;
            }

            @SuppressWarnings("resource")
            ZipFile zipFile = new ZipFile(epub);
            Enumeration<? extends ZipEntry> enumeration = zipFile.entries();

            String txtName = baseSaveDir + zipFile.getName().replaceAll("D:\\\\Workspace\\\\test\\\\BoxCarChildren\\\\", "") + ".txt";
            File file = FileUtil.createNewFile(txtName);

            // sort
            List<ZipEntry> zipEntryList = new ArrayList<ZipEntry>();
            while (enumeration.hasMoreElements()) {
                zipEntryList.add(enumeration.nextElement());
            }
            Collections.sort(zipEntryList, getZipEntryComparator());

            // write
            for (ZipEntry zipEntry : zipEntryList) {
                System.out.println(zipEntry.getName());
                if (zipEntry.getName().matches(".*html$")) {
                    System.out.println("即将写入" + txtName);
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    SAXReader saxReader = new SAXReader();
                    Document document = saxReader.read(inputStream);
                    Element rootElement = document.getRootElement();
                    Files.asCharSink(file, Charset.forName("UTF-8"), FileWriteMode.APPEND).write(rootElement.getStringValue());
                }
            }
        }
    }


    @SuppressWarnings("resource")
    public static void springTransaction() {
        // 初始化spring容器，因为是在main方法里，所以只能自己读了
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                "classpath*:config/applicationContext-persistence.xml");
        // 获取mapper，也就是dao，获取service也行，只要是操作数据库的，这个mapper我瞎写的，只是用来举个栗子
//		GlobalParamMapper mappper = (GlobalParamMapper) ctx.getBean("globalParamMapper");
        // 获取spring的事务管理器
        DataSourceTransactionManager transactionManager = (DataSourceTransactionManager) ctx.getBean("transactionManager");

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // 事物隔离级别，开启新事务，这样会比较安全些。
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        // 这个不知道是什么
        def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        // 获得事务状态
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
//			GlobalParam globalParam = new GlobalParam();
//			globalParam.setId("fgujkfgihbijiokp");
//			globalParam.setValue("❄️Ivy🌸");
//			System.out.println(mappper.insert(globalParam));

//			List<GlobalParam> paramList = mappper.selectAll();

//			System.out.println(paramList);

            // 提交事务，效果和jdbc拿connection，然后commit是一样的
            transactionManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

    }
}
