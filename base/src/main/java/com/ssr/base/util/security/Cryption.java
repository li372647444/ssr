package com.ssr.base.util.security;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

/**
 * 通用加密解密工具
 *
 * @author 
 */
public class Cryption {

    private final static String sKey  = "ssr_key";
    private final static String as3Key = "ssr_rtmp_as3_ky";
    private static Logger logger = Logger.getLogger(Cryption.class);

    /**
     * 初始化加密Cipher
     * @return
     * @throws Exception
     */
    public static Cipher initEnCipher() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(sKey.getBytes());
        keyGenerator.init(128, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] codeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(codeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher;
    }

    /**
     * 初始化加密Cipher
     * @return
     * @throws Exception
     */
    public static Cipher initDeCipher() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(sKey.getBytes());
        keyGenerator.init(128, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] codeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(codeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher;
    }

    /**
     * 加密输入流
     * @param iStream
     * @return
     */
    public static InputStream encryptStream(InputStream iStream) throws Exception {
        return new CipherInputStream(iStream, initEnCipher());
    }

    /**
     * 加密输出流
     * @param oStream
     * @return
     */
    public static OutputStream decryptStream(OutputStream oStream) throws Exception {
        return new CipherOutputStream(oStream, initDeCipher());
    }

    /**
     * 对字符串加密
     *
     * @param str
     * @return
     * @throws Exception
     */

    public static String enCrytor(String str) throws Exception {
        byte[] src = str.getBytes("UTF-8");
        byte[] cipherByte = initEnCipher().doFinal(src);
        return parseByte2HexStr(cipherByte);
    }
    
    /**
     * 对字符串解密
     *
     * @param str
     * @return
     * @throws Exception
     */

    public static String deCrytor(String str) throws Exception {
        byte[] src = parseHexStr2Byte(str);
        byte[] cipherByte = initDeCipher().doFinal(src);
        return new String(cipherByte, "UTF-8");
    }

    /**
     * 将二进制转换成16进制
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 生成传入字符串的MD5码
     * @param source
     * @return
     */
    public static String string2MD5(String source){
        MessageDigest md5 = null;
        StringBuilder sb = new StringBuilder("");
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            logger.error("获取MD5加密器失败", e);
            e.printStackTrace();
        }
        md5.update(source.getBytes());
        byte b[] = md5.digest();

        int i;
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0){
                i += 256;
            }
            if (i < 16) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(i));
        }
        return sb.toString();
    }
    
    /**
     * AppData加密
     * @param data
     * @return
     * @throws Exception
     */
    public static String enCrytorAppData(String data) throws Exception {
    	byte[] salt = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF };
    	PBEKeySpec myKeyspec = new PBEKeySpec(sKey.toCharArray(), salt, 10000, 128);
    	byte[] iv = { 0xA, 1, 0xB, 5, 4, 0xF, 7, 9, 0x17, 3, 1, 6, 8, 0xC, 0xD, 91 };
    	IvParameterSpec IV;
    	SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    	SecretKey sk = keyfactory.generateSecret(myKeyspec);
    	byte[] skAsByteArray = sk.getEncoded();
    	SecretKeySpec skforAES = new SecretKeySpec(skAsByteArray, "AES");
    	IV = new IvParameterSpec(iv);
    	Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    	c.init(Cipher.ENCRYPT_MODE, skforAES, IV);
    	byte[] re = c.doFinal(data.getBytes("UTF-8"));
    	return Base64.getEncoder().encodeToString(re);
    }
    
    /**
     * AppData解密
     * @param data
     * @return
     * @throws Exception
     */
    public static String deCrytorAppData(String data) throws Exception {
    	byte[] salt = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF };
    	PBEKeySpec myKeyspec = new PBEKeySpec(sKey.toCharArray(), salt, 10000, 128);
    	byte[] iv = { 0xA, 1, 0xB, 5, 4, 0xF, 7, 9, 0x17, 3, 1, 6, 8, 0xC, 0xD, 91 };
    	IvParameterSpec IV;
    	SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    	SecretKey sk = keyfactory.generateSecret(myKeyspec);
    	byte[] skAsByteArray = sk.getEncoded();
    	SecretKeySpec skforAES = new SecretKeySpec(skAsByteArray, "AES");
    	IV = new IvParameterSpec(iv);
    	Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    	c.init(Cipher.DECRYPT_MODE, skforAES, IV);
    	byte[] re = c.doFinal(Base64.getDecoder().decode(data));
    	return new String(re, "UTF-8");
    }
    
    /**
     * 建委能源监控上传数据专用加密KEY
     */
    public static String building_key = "aaaaaaaaaaaaaaaa";
    
	/**
	 * 建委能源监控上传数据专用加密
	 * 
	 * @param data
	 * @param mykey
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptBuildingAES(byte[] data) throws Exception {
		
		// AES加密方式
		KeyGenerator kg = KeyGenerator.getInstance("AES");
		// 128位密匙
		kg.init(128, new SecureRandom(building_key.getBytes()));
		SecretKey key = kg.generateKey();
		SecureRandom sr = new SecureRandom();
		// 采用AES的CBC模式加密 数据补足方式为PKCS5Padding
		Cipher cp = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec spec = new IvParameterSpec(key.getEncoded());
		cp.init(1, key, spec, sr);
		
		return cp.doFinal(data);
	}
	
	/**
	 * 建委能源监控上传数据专用解密
	 * 
	 * @param b
	 * @param mykey
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBuildingAES(byte[] data) throws Exception {
		
		// AES解密方式
		KeyGenerator kg = KeyGenerator.getInstance("AES");
		// 128位密匙
		kg.init(128, new SecureRandom(building_key.getBytes()));
		SecretKey key = kg.generateKey();

		SecureRandom sr = new SecureRandom();
		// 采用AES的CBC模式解密密 数据补足方式为PKCS5Padding
		Cipher cp = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec spec = new IvParameterSpec(key.getEncoded());
		cp.init(2, key, spec, sr);

		return cp.doFinal(data);
	}
	
	/**
	 * as3专用通讯加密
	 * @param sSrc
	 * @return
	 * @throws Exception
	 */
	public static String encryptAs3AES(String sSrc) throws Exception {
		byte[] raw = as3Key.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		// 算法/模式/补码方式
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		// 使用CBC模式，需要一个向量iv,可增加加密算法的强度
		IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes("UTF-8"));
		// 此处不使用BAES64做转码功能(BASE64可能出现特殊符号导致解密失败),使用16进制处理
		return parseByte2HexStr(encrypted);
	}
	
	/**
	 * as3专用通讯解密
	 * @param sSrc
	 * @return
	 * @throws Exception
	 */
	public static String decryptAs3AES(String sSrc) throws Exception {
		byte[] raw = as3Key.getBytes("ASCII");
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		// 此处不使用BAES64做转码功能(BASE64可能出现特殊符号导致解密失败),使用16进制处理
		byte[] encrypted1 = parseHexStr2Byte(sSrc);
		byte[] original = cipher.doFinal(encrypted1);
		String originalString = new String(original, "UTF-8");
		return originalString;
	}
}
