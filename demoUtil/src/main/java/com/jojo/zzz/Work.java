package com.jojo.zzz;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

public class Work {

    public static int getObjectMemorySize(Object object) throws IllegalAccessException {
        if (object == null || object instanceof Class<?>) {
            return 0;
        }

        Class<?> objectClass = object.getClass();
        if (isPrimitive(objectClass)) {
            return getPrimitiveMemorySize(objectClass);
        }

        int result = 0;
        Field[] fieldArr = objectClass.getDeclaredFields();
        if (ArrayUtils.isEmpty(fieldArr)) {
            return result;
        }

        for (Field field : fieldArr) {
            field.setAccessible(true);
            result += getObjectMemorySize(field.get(object));
        }

        return result;
    }

    private static boolean isPrimitive(Class<?> objectClass) {
        String classSimpleName = objectClass.getSimpleName();
        String[] wrapperClassArr = {"Boolean", "Byte", "Character", "Short", "Integer", "Long", "Float", "Double"};
        return objectClass.isPrimitive() || StringUtils.equalsAny(classSimpleName, wrapperClassArr);
    }

    private static int getPrimitiveMemorySize(Class<?> objectClass) {
        String classSimpleName = objectClass.getSimpleName();
        if (StringUtils.equalsIgnoreCase(classSimpleName, "boolean")) {
            return 1;
        } else if (StringUtils.equalsIgnoreCase(classSimpleName, "byte")) {
            return 8;
        } else if (StringUtils.equalsAny(classSimpleName, "Character", "char")) {
            return 16;
        } else if (StringUtils.equalsIgnoreCase(classSimpleName, "short")) {
            return 16;
        } else if (StringUtils.equalsAny(classSimpleName, "Integer", "int")) {
            return 32;
        } else if (StringUtils.equalsIgnoreCase(classSimpleName, "long")) {
            return 64;
        } else if (StringUtils.equalsIgnoreCase(classSimpleName, "float")) {
            return 32;
        } else if (StringUtils.equalsIgnoreCase(classSimpleName, "double")) {
            return 64;
        }
        return 0;
    }

    public static void main(String[] args) throws IllegalAccessException {
        Work work = new Work();

        System.out.println(getObjectMemorySize(work));
    }

    private int a;
    private char b;
    private int[] cArr = new int[4];
}

