package com.raiden.util;

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
    public static String firstLetterLowercase(String name){
        char[] chars = name.toCharArray();
        StringBuilder builder = new StringBuilder();
        char c = chars[0];
        //如果是小写才替换
        if (c > 64 && c < 91){
            c += 32;
            chars[0] = c;
        }
        builder.append(chars);
        return builder.toString();
    }

}
