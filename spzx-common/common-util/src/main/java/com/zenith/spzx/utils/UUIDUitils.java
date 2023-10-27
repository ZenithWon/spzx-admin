package com.zenith.spzx.utils;

import java.util.UUID;

public class UUIDUitils {
    public static String generateKey(){
        return UUID.randomUUID().toString().replace("-","")+System.currentTimeMillis();
    }
}
