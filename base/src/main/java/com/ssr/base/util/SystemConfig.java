package com.ssr.base.util;

import com.ssr.base.util.properties.PropertiesHelper;

/**
 * 全局Properties配置读取工具
 *
 * @author PengLian
 */
public class SystemConfig {

    static PropertiesHelper help;
    static {
        help = new PropertiesHelper("config.properties");
    }

    /**
     * 获取properties对象
     * @return
     */
    public static PropertiesHelper buildConfig() {
        return help;
    }

    /**
     * 获取
     * @param key
     * @return
     */
    public static String getValueByKey(String key){
        return help.getValue(key);
    }

    /**
     * 获取
     * @param key
     * @return
     */
    public static String getValueByKey(String key, String defaultValue){
        return help.getValue(key, defaultValue);
    }
}
