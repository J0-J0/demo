
package com.jojo.zzz;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.omg.Messaging.SyncScopeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Work {
    private static final Logger logger = LoggerFactory.getLogger(Work.class);

    public static void main(String[] args) throws ParseException {
        List<String> list = Lists.newArrayList("2020-01-03", "2019-01-01", "2018-01-01");
        Collections.sort(list);
        System.out.println(list);
        System.out.println("=======================");
        System.out.println("2018-01-01".compareTo("2019-01-01"));
        List<String> pageData = Lists.newArrayList();
    }

    public static int toResverseBinaryIntValue(int n) {
        System.out.println(Integer.toBinaryString(n));
        StringBuilder resverseBinaryString = new StringBuilder();
        int differ = n / 2;
        resverseBinaryString.append(n % 2);
        while (differ != 0) {
            resverseBinaryString.append(differ % 2);
            differ /= 2;
        }
        System.out.println(resverseBinaryString);
        int resverseValue = 0;
        int length = resverseBinaryString.length();
        for (int i = length - 1, j = 0; i > -1; i--, j++) {
            char ch = resverseBinaryString.charAt(i);
            resverseValue += Math.pow(2, j);
        }
        // 这里反转
        return resverseValue;
    }
}