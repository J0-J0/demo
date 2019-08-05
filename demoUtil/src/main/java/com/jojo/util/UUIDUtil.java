package com.jojo.util;

import java.util.UUID;

public class UUIDUtil {

    public static final String getRandomUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
