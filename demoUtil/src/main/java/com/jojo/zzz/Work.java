
package com.jojo.zzz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Work {
    private static final Logger logger = LoggerFactory.getLogger(Work.class);

    public static void main(String[] args) throws ParseException {
        int num = 43261596;
        System.out.println(toResverseBinaryIntValue(num));
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