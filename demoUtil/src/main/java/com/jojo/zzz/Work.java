package com.jojo.zzz;

import com.jojo.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Work {
    private static final Logger logger = LoggerFactory.getLogger(Work.class);

    public static void main(String[] args) {
        int a = 1;
        try {
            if (a == 1) {
                return;
            }
        } finally {
            System.out.println("awesfg");
        }
    }

    static void funcA(List<Integer> list) {
        list.add(1);
    }

    private static void move(File file, String newPath, Map<String, Integer> filenameCountMap) {
        if (!file.isDirectory()) {
            String filename = file.getName();
            String suffixName = FileUtil.getSuffixName(filename);
            if (StringUtils.equals(suffixName, "TXT")) {
                Integer nameCount = filenameCountMap.getOrDefault(filename, 0);
                if (nameCount == 0) {// 为1，直接move
                    String pathname = newPath + File.separator + filename;
                    System.out.println(file.getAbsolutePath() + "===迁移===" + pathname);
                    file.renameTo(new File(pathname));
                } else {// 已存在，修改filename再move
                    String pathname = newPath + File.separator + filename.split("\\.")[0] + "-" + (nameCount + 1) + "." + suffixName;
                    System.out.println(file.getAbsolutePath() + "===迁移===" + pathname);
                    file.renameTo(new File(pathname));
                }
                filenameCountMap.put(filename, ++nameCount);
            }
            return;
        }

        for (File listFile : file.listFiles()) {
            move(listFile, newPath, filenameCountMap);
        }
    }

    static void delete(File file) {
        if (!file.isDirectory()) {
            return;
        }

        File[] files = file.listFiles();
        if (files.length == 0) {
            file.delete();
            return;
        }

        for (File listFile : files) {
            delete(listFile);
        }
    }
}