package com.raiden.util;

import com.raiden.base.Info;

public class StringUtil {

    public static final String EMPTY = "";

    /**
     * 工具类最好不要有构造方法
     */
    private StringUtil(){}

    public static boolean isBlank(String value){
        if (null == value || EMPTY.equals(value)){
            return true;
        }
        return false;
    }

    public static boolean isNotBlank(String value){
        return !isBlank(value);
    }

    public static boolean isNonBlank(String... values){
        for (String value : values){
            if (isBlank(value)){
                return false;
            }
        }
        return true;
    }

    public static String trim(String value,String start,String end){
        return value.substring(value.indexOf(start), value.lastIndexOf(end) + 1);
    }

    /**
     * 首字母小写
     * @param name
     * @return
     */
    public static Info firstLetterLowercase(String name){
        char[] chars = name.toCharArray();
        char c = chars[0];
        Info info = new Info();
        //如果是小写才替换
        if (c > 64 && c < 91){
            c += 32;
            chars[0] = c;
            info.setInsertAnnotation(true);
        }
        return info.setValue(String.valueOf(chars));
    }

    /**
     * 首字母大写的方法
     * @param name
     * @return
     */
    public static String firstLetterCapitalized(String name){
        char[] chars = name.toCharArray();
        char c = chars[0];
        //如果是小写才替换
        if (c > 96 && c < 123){
            c -= 32;
            chars[0] = c;

        }
        return String.valueOf(chars);
    }
}
