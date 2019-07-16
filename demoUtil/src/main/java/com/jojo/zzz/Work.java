package com.jojo.zzz;

import java.io.File;

public class Work {

    public static void main(String[] args) {
        String path = "C:\\WorkSpace\\mine\\软著代码\\客户版";
        File file = new File(path);
        File[] fileArr = file.listFiles();
    }
}

