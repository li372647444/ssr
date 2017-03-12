package com.ssr.base.util.img;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;
import com.ssr.base.util.SystemConfig;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Random;

/**
 * 验证码生成工具类
 *
 * @author PengLian
 */
public class VerifyCodeUtils {

    //去掉了1,0,i,o几个容易混淆的字符
    private static final String VERIFY_CODES = "0123456789";
    private  static Producer producer;

    /**
     * 初始化验证码生成器
     */
    private static Producer init(){
        if(producer == null){
            Properties properties = new Properties();
            properties.setProperty("kaptcha.image.height", SystemConfig.getValueByKey("kaptcha.image.height", "70"));
            properties.setProperty("kaptcha.image.width", SystemConfig.getValueByKey("kaptcha.image.width", "200"));
            properties.setProperty("kaptcha.border", SystemConfig.getValueByKey("kaptcha.border", "no"));
            properties.setProperty("kaptcha.textproducer.font.color",
                    SystemConfig.getValueByKey("kaptcha.textproducer.font.color", "black"));
            properties.setProperty("kaptcha.textproducer.char.space",
                    SystemConfig.getValueByKey("kaptcha.textproducer.char.space", "5"));
            producer = new Config(properties).getProducerImpl();
        }
        return producer;
    }

    /**
     * 使用系统默认字符源生成验证码
     * @param verifySize	验证码长度
     * @return s
     */
    public static String generateVerifyCode(int verifySize){
        int codesLen = VERIFY_CODES.length();
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder verifyCode = new StringBuilder(verifySize);
        for(int i = 0; i < verifySize; i++){
            verifyCode.append(VERIFY_CODES.charAt(rand.nextInt(codesLen-1)));
        }
        return verifyCode.toString();
    }

    /**
     * 输出指定验证码图片流
     * @param os 输出流
     * @param code 验证码
     * @throws IOException
     */
    public static void outputImage(OutputStream os, String code) throws IOException{
        ImageIO.write(init().createImage(code), "jpeg", os);
    }
}
