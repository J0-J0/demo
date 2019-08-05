package com.jojo.util;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ClassUtil {
    /**
     * @param clazz
     */
    public static void showGettersAndSetters(Class<?> clazz, String name) {
        Field[] fields = clazz.getDeclaredFields();

        List<Field> list = Arrays.asList(fields);
        Collections.sort(list, new Comparator<Field>() {
            @Override
            public int compare(Field o1, Field o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        for (Field field : list) {
            StringBuffer sb = new StringBuffer();
            sb.append(name + ".set");
            sb.append(field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
            sb.append("(").append(" ").append(")");
            System.out.println(sb.toString());
        }
        System.out.println();
        System.out.println();
        System.out.println();
        for (Field field : list) {
            StringBuffer sb = new StringBuffer();
            sb.append(name + ".get");
            sb.append(field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
            sb.append("(").append(" ").append(")");
            System.out.println(sb.toString());
        }
    }

    /**
     * 假定两类字段差不多，需要get来set去
     *
     * @param targetClass
     * @param sourceClassName
     */
    public static final void showCopyPropertiesStatement(Class<?> targetClass, String sourceClassName) {
        Field[] fields = targetClass.getDeclaredFields();
        String className = StringUtils.uncapitalize(targetClass.getSimpleName());

        for (Field field : fields) {
            String camelFiledName = StringUtils.capitalize(field.getName());
            StringBuffer sb = new StringBuffer();
            sb.append(className).append(".set");
            sb.append(camelFiledName);
            sb.append("(").append(sourceClassName).append(".get").append(camelFiledName).append("());");
            System.out.println(sb.toString());
        }
    }
}
