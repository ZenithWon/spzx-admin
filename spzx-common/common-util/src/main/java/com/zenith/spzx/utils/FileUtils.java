package com.zenith.spzx.utils;

public class FileUtils {
    public static String getFileExtension(String originFileName){
        String extension = "";
        int i = originFileName.lastIndexOf('.');
        if (i > 0) {
            extension = originFileName.substring(i);
        }
        return extension;
    }

    public static String generateFileName(String originFileName){
        String extension = getFileExtension(originFileName);
        String res=UUIDUitils.generateKey()+extension;
        return res;
    }

    public static void main(String[] args) {
        System.out.println(generateFileName("1.png"));
    }
}
