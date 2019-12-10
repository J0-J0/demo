
package com.jojo.zzz;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Work {
    private static final Logger logger = LoggerFactory.getLogger(Work.class);

    public static void main(String[] args) throws ParseException {

        Date beginDate = DateUtils.addDays(new Date(), -1);
        beginDate = DateUtils.truncate(beginDate, Calendar.DAY_OF_MONTH);
        Date endDate = org.apache.commons.lang3.time.DateUtils.addDays(new Date(), 1);
        endDate = org.apache.commons.lang3.time.DateUtils.truncate(endDate, Calendar.DAY_OF_MONTH);


        System.out.println(DateFormatUtils.format(beginDate, "yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateFormatUtils.format(endDate, "yyyy-MM-dd HH:mm:ss"));
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