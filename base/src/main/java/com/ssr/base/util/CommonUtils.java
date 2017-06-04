package com.ssr.base.util;

import java.net.URLDecoder;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * 全局工具,可根据需要自行添加
 *
 * @author 
 */
public class CommonUtils {

	private static Logger logger = Logger.getLogger(CommonUtils.class);

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat sdfShortDate = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
	private static SimpleDateFormat sdfCn = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
	private static SimpleDateFormat sdfShortCn = new SimpleDateFormat("yyyy年MM月dd日");

	// 参数解码格式
	protected final static String charSet = "UTF-8"; // 参数解码格式
	// H5 session的cookie返回
	public static final String JSESSIONID = "JSESSIONID";

	/**
	 * 判断传入对象是否为空 String:null||"" Collection:null||size()=0 Map:null||size()=0
	 *
	 * @param pObj
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object pObj) {
		if (pObj == null)
			return true;
		if (pObj == "")
			return true;
		if (pObj instanceof String) {
			if (((String) pObj).length() == 0) {
				return true;
			}
		} else if (pObj instanceof Collection) {
			if (((Collection) pObj).size() == 0) {
				return true;
			}
		} else if (pObj instanceof Map) {
			if (((Map) pObj).size() == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断传入对象是否不为空 String:!null&&!"" Collection:!null&&!size()=0
	 * Map:!null&&size()!=0
	 *
	 * @param pObj
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isNotEmpty(Object pObj) {
		if (pObj == null)
			return false;
		if (pObj == "")
			return false;
		if (pObj instanceof String) {
			if (((String) pObj).length() == 0) {
				return false;
			}
		} else if (pObj instanceof Collection) {
			if (((Collection) pObj).size() == 0) {
				return false;
			}
		} else if (pObj instanceof Map) {
			if (((Map) pObj).size() == 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取当前时间yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getDateStr() {
		return sdf.format(new Date());
	}

	/**
	 * 获取当前时间yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getShortDateStr() {
		return sdfShortDate.format(new Date());
	}

	/**
	 * 获取当前时间HH:mm:ss
	 * 
	 * @return
	 */
	public static String getTimeStr() {
		return sdfTime.format(new Date());
	}

	/**
	 * 获取当前时间yyyy年MM月dd日 HH时mm分ss秒
	 * 
	 * @return
	 */
	public static String getCnDateStr() {
		return sdfCn.format(new Date());
	}

	/**
	 * 获取当前时间yyyy年MM月dd日
	 * 
	 * @return
	 */
	public static String getCnShortDateStr() {
		return sdfShortCn.format(new Date());
	}

	/**
	 * 得到当前年
	 *
	 * @return
	 */
	public static int getYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	/**
	 * 得到当前月
	 *
	 * @return
	 */
	public static int getMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取当前日
	 * 
	 * @return
	 */
	public static int getDay() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DATE);
	}

	/**
	 * 通过JDK工具生成UUID
	 * 
	 * @return
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 通过JDK工具生成32位UUID
	 * 
	 * @return
	 */
	public static String getUUID32() {
		return getUUID().replace("-", "");
	}

	/**
	 * 生成字符串的Base64编码
	 * 
	 * @param source
	 * @return
	 */
	public static String string2Base64(String source) {
		return new String(Base64.getEncoder().encodeToString(source.getBytes()));
	}

	/**
	 * 生成字符串的UrlBase64编码
	 * 
	 * @param source
	 * @return
	 */
	public static String string2UrlBase64(String source) {
		return new String(Base64.getUrlEncoder().encodeToString(source.getBytes()));
	}

	/**
	 * 将字符串首字母大写
	 * 
	 * @param name
	 * @return
	 */
	public static String upperCaseFirstLetter(String name) {
		char[] cs = name.toCharArray();
		cs[0] -= 32;
		return String.valueOf(cs);
	}

	/**
	 * 将字符串首字母小写
	 * 
	 * @param name
	 * @return
	 */
	public static String lowerCaseFirstLetter(String name) {
		char[] cs = name.toCharArray();
		cs[0] += 32;
		return String.valueOf(cs);
	}


	/**
	 * 获取访问路径中的服务部分
	 * 
	 * @param req
	 * @return
	 */
	public static String getUrlFromReq(HttpServletRequest req) {
		StringBuffer url = new StringBuffer();
		String scheme = req.getScheme();
		int port = req.getServerPort();
		url.append(scheme);
		url.append("://");
		url.append(req.getServerName());
		if ((scheme.equals("http") && port != 80) || (scheme.equals("https") && port != 443)) {
			url.append(':');
			url.append(req.getServerPort());
		}
		return url.toString();
	}

	/**
	 * 拼装HTTP参数(自动过滤掉为空的参数)
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String concatUrl(String url, Map<String, String> params) {
		StringBuilder buffer = new StringBuilder(url);
		int i = 0;
		if (params != null) {
			String value = null;
			for (Map.Entry<String, String> entry : params.entrySet()) {
				value = entry.getValue();
				if (value == null || "".equals(value)) {
					continue;
				}
				++i;
				if (i == 1) {
					buffer.append("?");
					buffer.append(entry.getKey());
					buffer.append("=");
					buffer.append(entry.getValue());
				} else {
					buffer.append("&");
					buffer.append(entry.getKey());
					buffer.append("=");
					buffer.append(entry.getValue());
				}
			}
		}
		logger.debug("拼接完成的URL为:" + buffer);
		return buffer.toString();
	}

	/**
	 * 手机号验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) {
		if (isEmpty(str))
			return false;
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	/**
	 * 生成邀请码
	 */
	public static String getCode() {
		char[] chs = { 'a', 'b', 'c', '1', '2', '3', '4', '5', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
				'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '6', '7', '8', '9' };
		SecureRandom random = new SecureRandom();
		final char[] value = new char[6];
		for (int i = 0; i < 6; i++) {
			value[i] = chs[random.nextInt(chs.length)];
		}
		final String code = new String(value);
		return code;
	}

	public static String urlDecoder(String str) throws Exception {
		if (!StringHelper.isEmpty(str)) {
			return URLDecoder.decode(str, charSet);
		}
		return null;
	}

	/**
	 * 传入一个参数，返回带*的数据<br>
	 * 传入为空，返回null<br>
	 * 传入值str长度为1，返回值（str**）<br>
	 * 
	 * @param str
	 *            传入值
	 * @param start
	 *            加*号起始位置
	 * @param end
	 *            加*号结束位置
	 * @return
	 */
	public static String getReplaceAll(String str, int start, int end) {
		if (CommonUtils.isEmpty(str)) {
			return null;
		}

		if (str.length() == 1) {
			return str + "**";
		}

		if (start > end) {
			int temp = start;
			start = end;
			end = temp;
		}
		if (str.length() < start) {
			start = str.length();
		}
		if (str.length() < end) {
			end = str.length();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(str.substring(0, start));
		for (int i = start; i < end; i++) {
			sb.append("*");
		}
		sb.append(str.substring(end, str.length()));
		return sb.toString();
	}

	/**
	 * 获取用户真实IP
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String getIpAddr(HttpServletRequest request) throws Exception {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	   /**
     * 验证邮箱
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        boolean flag = false;
        try{
                String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(email);
                flag = matcher.matches();
            }catch(Exception e){
                flag = false;
            }
        return flag;
    }
    
    /**
     * byte数组转化为数字
     * @param bRefArr
     * @return
     */
    public static int byteArrayToInt(byte[] bRefArr){
		int iOutcome = 0;
	    byte bLoop;
	    int l = bRefArr.length;
	    for (int i = 0; i < l; i++) {
	        bLoop = bRefArr[i];
	        iOutcome += (bLoop & 0xFF) << (8 * (l - 1 -i));
	    }
	    return iOutcome;
	}
    
    /**
     * 获取url中的Host
     * @param url
     * @return
     */
    public static String getHostFromUrl(String url){
    	String re = "";
    	re = url.substring(url.indexOf("//") + 2);
    	re = re.substring(0, re.indexOf("/"));
    	return re;
    }
    
    /**
     * 强判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 判断字符串是否为数字
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if (isBlank(str)) {
            return false;
        }
        char[] chars = str.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        // deal with any possible sign up front
        int start = (chars[0] == '-') ? 1 : 0;
        if (sz > start + 1) {
            if (chars[start] == '0' && chars[start + 1] == 'x') {
                int i = start + 2;
                if (i == sz) {
                    return false; // str == "0x"
                }
                // checking hex (it can't be anything else)
                for (; i < chars.length; i++) {
                    if ((chars[i] < '0' || chars[i] > '9')
                        && (chars[i] < 'a' || chars[i] > 'f')
                        && (chars[i] < 'A' || chars[i] > 'F')) {
                        return false;
                    }
                }
                return true;
            }
        }
        sz--; // don't want to loop to the last char, check it afterwords
              // for type qualifiers
        int i = start;
        // loop to the next to last char or to the last char if we need another digit to
        // make a valid number (e.g. chars[0..5] = "1234E")
        while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;
                allowSigns = false;

            } else if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent   
                    return false;
                }
                hasDecPoint = true;
            } else if (chars[i] == 'e' || chars[i] == 'E') {
                // we've already taken care of hex.
                if (hasExp) {
                    // two E's
                    return false;
                }
                if (!foundDigit) {
                    return false;
                }
                hasExp = true;
                allowSigns = true;
            } else if (chars[i] == '+' || chars[i] == '-') {
                if (!allowSigns) {
                    return false;
                }
                allowSigns = false;
                foundDigit = false; // we need a digit after the E
            } else {
                return false;
            }
            i++;
        }
        if (i < chars.length) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                // no type qualifier, OK
                return true;
            }
            if (chars[i] == 'e' || chars[i] == 'E') {
                // can't have an E at the last byte
                return false;
            }
            if (!allowSigns
                && (chars[i] == 'd'
                    || chars[i] == 'D'
                    || chars[i] == 'f'
                    || chars[i] == 'F')) {
                return foundDigit;
            }
            if (chars[i] == 'l'
                || chars[i] == 'L') {
                // not allowing L with an exponent
                return foundDigit && !hasExp;
            }
            // last character is illegal
            return false;
        }
        // allowSigns is true iff the val ends in 'E'
        // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
        return !allowSigns && foundDigit;
    }
    
    /**
     * 字符串转数字
     * @param str
     * @param defaultValue
     * @return
     */
    public static int toInt(String str, int defaultValue) {
        if(str == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

}
