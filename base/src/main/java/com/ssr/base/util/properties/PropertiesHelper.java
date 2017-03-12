package com.ssr.base.util.properties;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Properties处理器
 *
 * @author PengLian
 */
public class PropertiesHelper {

    private Logger logger = Logger.getLogger(getClass());
    private Properties objProperties;

    public PropertiesHelper(String filePath){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = PropertiesHelper.class.getClassLoader();
        }
        try {
            InputStream is = classLoader.getResourceAsStream(filePath);
            objProperties = new Properties();
            objProperties.load(is);

        } catch (Exception e) {
            logger.error("加载属性文件" + filePath + "出错!");
            e.printStackTrace();
        }
    }

    /**
     * 保存属性到文件
     * @param pFileName
     */
    public void storefile(String pFileName){
        FileOutputStream outStream = null;
        try{
            File file = new File(pFileName + ".properties");
            outStream = new FileOutputStream(file);
            objProperties.store(outStream, "#properties");
        }catch(Exception e){
            logger.error("保存属性文件" + pFileName + "出错!");
            e.printStackTrace();
        }finally{
            try {
                outStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取配置文件中key对应的值
     * @param key
     * @return
     */
    public String getValue(String key){
        return objProperties.getProperty(key);
    }

    /**
     * 获取配置文件中key对应的值,值为空时使用默认值
     * @param key
     * @param defaultValue
     * @return
     */
    public String getValue(String key, String defaultValue){
        return objProperties.getProperty(key, defaultValue);
    }

    /**
     * 删除key对应的键值对
     * @param key
     */
    public void removeProperty(String key){
        objProperties.remove(key);
    }

    /**
     * 设置键值对,key不存在时为新增,否则为修改
     * @param key
     * @param value
     */
    public void setProperty(String key, String value){
        objProperties.setProperty(key, value);
    }

}
