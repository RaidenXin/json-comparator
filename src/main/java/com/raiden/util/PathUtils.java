package com.raiden.util;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 13:33 2019/8/3
 * @Modified By:
 */
public class PathUtils {

    /**
     * 工具类最好不要有构造方法
     */
    private PathUtils(){}

    private static final String rootPath = PathUtils.class.getProtectionDomain().getCodeSource().getLocation().getFile();

    private static final String config = "config\\";

    public static String getRootPath(){
        return rootPath;
    }

    public static String getConfigPath(){
        return rootPath + config;
    }
}
