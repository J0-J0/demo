package com.jojo.util;

import com.alibaba.fastjson.JSONArray;

public class StackTraceUtil {

    public static final String getStackTraceString() {
        JSONArray stackTrace = new JSONArray();
        Throwable throwable = new Throwable();
        StackTraceElement[] stackTraceElementArr = throwable.getStackTrace();
        for (int i = 1; i < stackTraceElementArr.length; i++) {
            StringBuilder result = new StringBuilder();
            StackTraceElement stackTraceElement = stackTraceElementArr[i];
            result.append(stackTraceElement.getClassName()).append("#");
            result.append(stackTraceElement.getMethodName());
            result.append("(").append(stackTraceElement.getFileName()).append(":");
            result.append(stackTraceElement.getLineNumber()).append(")");
            stackTrace.add(result);
        }
        return stackTrace.toString();
    }
}
