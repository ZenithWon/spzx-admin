package com.zenith.spzx.utils;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

public class FileUtils {
    public static String getFileExtension(String originFileName){
        String extension = "";
        int i = originFileName.lastIndexOf('.');
        if (i > 0) {
            extension = originFileName.substring(i);
        }
        return extension;
    }

    public static String generateMinioFileName(String originFileName){
        String extension = getFileExtension(originFileName);
        String folder= DateUtil.format(new Date(),"yyyyMMdd");
        String res=UUIDUitils.generateKey()+extension;
        return folder+"/"+res;
    }

    public static void main(String[] args) {
        System.out.println(generateMinioFileName("1.png"));
    }
}
