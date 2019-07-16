package com.jojo.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class ObjectMemorySizeUtil {

    /**
     * 获取某对象在内存中占用的bit数
     *
     * @param object
     * @return
     * @throws IllegalAccessException
     */
    public static final int getObjectMemorySize(Object object) throws IllegalAccessException {
        if (object == null || object instanceof Class<?>) {
            return 0;
        }

        Class<?> objectClass = object.getClass();
        if (isPrimitive(objectClass)) {
            return getPrimitiveMemorySize(objectClass);
        }

        if (objectClass.isArray()) {
            return getArrayMemorySize(object);
        }

        Field[] fieldArr = objectClass.getDeclaredFields();
        if (ArrayUtils.isEmpty(fieldArr)) {
            return 0;
        }

        int result = 0;
        for (Field field : fieldArr) {
            field.setAccessible(true);
            result += getObjectMemorySize(field.get(object));
        }

        return result;
    }

    /**
     * 获取数据类型占用bit数
     *
     * @param object
     * @return
     * @throws IllegalAccessException
     */
    private static int getArrayMemorySize(Object object) throws IllegalAccessException {
        int length = Array.getLength(object);
        if (length == 0) {
            return 0;
        }

        int result = 0;
        for (int i = 0; i < length; i++) {
            Object obj = Array.get(object, i);
            result += getObjectMemorySize(obj); // 从头开始递归
        }

        return result;
    }

    /**
     * 是否原生类型
     *
     * @param objectClass Class类型
     * @return true / false
     */
    private static boolean isPrimitive(Class<?> objectClass) {
        String classSimpleName = objectClass.getSimpleName();
        String[] wrapperClassArr = new String[]{"Boolean", "Byte", "Character", "Short", "Integer", "Long", "Float", "Double"};
        return objectClass.isPrimitive() || StringUtils.equalsAny(classSimpleName, wrapperClassArr);
    }

    /**
     * 获取原生类型占用bit数
     *
     * @param objectClass
     * @return
     */
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
        ObjectMemorySizeUtil obj = new ObjectMemorySizeUtil();

        System.out.println(getObjectMemorySize(obj));
    }

    private int a;
    private char b;
    private int[] cArr = new int[4];
}
