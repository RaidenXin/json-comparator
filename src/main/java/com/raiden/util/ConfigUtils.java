package com.raiden.util;

import com.raiden.logs.Logger;

import java.io.*;
import java.util.Properties;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 13:50 2019/8/3
 * @Modified By:
 */
public class ConfigUtils {

    private ConfigUtils(){}
    private static final Logger logger = Logger.newInstance();

    public static Properties getProperties(String name){
        String configPath = PathUtils.getConfigPath();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(configPath + name));
            Properties p = new Properties();
            p.load(in);
            return p;
        } catch (FileNotFoundException e) {
            logger.error("配置文件不存在! url:" + configPath + name);
        } catch (IOException e) {
            logger.error(e);
        }
        return null;
    }
}
