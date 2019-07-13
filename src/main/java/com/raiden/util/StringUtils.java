package com.raiden.util;

public class StringUtils {

    public static final String EMPTY = "";

    /**
     * 工具类最好不要有构造方法
     */
    private StringUtils(){}

    public static boolean isBlank(String value){
        if (null == value || EMPTY.equals(value)){
            return true;
        }
        return false;
    }

    public static boolean isNotBlank(String value){
        return !isBlank(value);
    }

    public static String trim(String value,String start,String end){
        return value.substring(value.indexOf(start), value.lastIndexOf(end) + 1);
    }

}
