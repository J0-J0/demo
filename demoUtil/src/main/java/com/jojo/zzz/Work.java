
package com.jojo.zzz;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Work {
    private static final Logger logger = LoggerFactory.getLogger(Work.class);

    static {
        val = 2;
    }

    private static int val = 1;


    public static void main(String[] args) throws Exception {
        String str = "       0";
        System.out.println(str);
        System.out.println(str.length());


        System.out.println(StringUtils.leftPad("val", 8, " "));
    }


}