package com.jojo.demoMain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class MainTest {
    private static final Logger logger = LoggerFactory.getLogger(MainTest.class);


    public static void main(String[] args) throws Exception {
        // 存在整数溢出问题
        long a = 25 * 24 * 60 * 60 * 1000;
        System.out.println("存在溢出问题的结果: " + a);

        // 解决整数溢出问题
        long b = 25L * 24 * 60 * 60 * 1000;
        System.out.println("解决溢出问题后的结果: " + b);

    }


}