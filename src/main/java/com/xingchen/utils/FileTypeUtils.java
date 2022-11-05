package com.xingchen.utils;

/**
 * @author Li
 * @Date 2022/7/3 23:37
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FileTypeUtils {

    // 默认判断文件头前三个字节内容
    public static int default_check_length = 3;
    final static HashMap<String, String> fileTypeMap = new HashMap<>();

    static {
        fileTypeMap.put("ffd8ff", "jpg");
        fileTypeMap.put("89504e", "png");
        fileTypeMap.put("474946", "gif");
    }
    public static boolean fileType(String type, File localFile){
        try{
            InputStream inputStream=new FileInputStream(localFile);
            String fileType = getFileTypeByMagicNumber(inputStream);
            if(type.contains(fileType))
                return true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //获取文件类型
    public static String getFileTypeByMagicNumber(InputStream inputStream) {
        byte[] bytes = new byte[default_check_length];
        try {
            // 获取文件头前三位魔数的二进制
            inputStream.read(bytes, 0, bytes.length);
            // 文件头前三位魔数二进制转为16进制
            String code = bytesToHexString(bytes);
//            System.out.println(code);
            for (Map.Entry<String, String> item : fileTypeMap.entrySet()) {
                if (code.equalsIgnoreCase(item.getKey())) {
                    return item.getValue();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取字节流
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
