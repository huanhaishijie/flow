package com.sophony.flow.commons;

/**
 * MD5SingUtil
 *
 * @author yzm
 * @version 1.5.0
 * @description
 * @date 2023/3/12 11:09
 */
public class MD5SingUtil {


    public static String MD5(String val) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            byte[] array = md.digest(val.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }


    public static String MD5(Object obj){
        return MD5(obj.toString());
    }
}
