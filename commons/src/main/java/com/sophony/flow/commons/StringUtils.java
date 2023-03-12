package com.sophony.flow.commons;

import java.util.Objects;

/**
 * StringUtils
 *
 * @author yzm
 * @version 1.0
 * @description
 * @date 2023/3/7 11:26
 */
public class StringUtils {


    public static  boolean isNotBlank(String str){
        return Objects.nonNull(str) && str.length() != 0;
    }
}
