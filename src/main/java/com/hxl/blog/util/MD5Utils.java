package com.hxl.blog.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hxl on 2021/7/8.
 */
public class MD5Utils {
    public static String string2MD5(String inStr){
        MessageDigest md5 = null;
        try{
            //引用  java.security.MessageDigest公共类
            // getInstance("MD5")方法 设置加密格式
            md5 = MessageDigest.getInstance("MD5");  //此句是核心
        }catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++){
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }

    /**
     * 加密解密算法[可逆] 执行一次加密，执行两次解密
     */
    public static String convertMD5(String inStr){

        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++){
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;

    }
    public static String convertMD52(String inStr){

        String md5 = convertMD5(convertMD5(inStr));
        return md5;

    }

    // 测试主函数
    public static void main(String args[]) {
        String s = new String("hxl.201507");
        System.out.println("原始：" + s);
        System.out.println("MD5后：" + string2MD5(s));
        System.out.println("加密的：" + convertMD5(s));
        System.out.println("解密的：" + convertMD5(convertMD5(s)));

    }

}
