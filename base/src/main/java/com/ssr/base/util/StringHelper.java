package com.ssr.base.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {
	
	private static final Pattern REPLACE_VARIABLE_PATTERN = Pattern.compile("\\$\\{\\s*(\\w|\\.|-|_|\\$)+\\s*\\}", Pattern.CASE_INSENSITIVE);
	
	public static String trim(String value) {
		return value == null ? null : value.trim();
	}

	public static boolean isEmpty(String value) {
		int length;
		if ((value == null) || ((length = value.length()) == 0))
			return true;
		for (int index = 0; index < length; index++) {
			char ch = value.charAt(index);
			if ((ch != ' ') && (!Character.isISOControl(ch))) {
				return false;
			}
		}
		return true;
	}

	public static String truncation(String string, int maxLength) {
		if (isEmpty(string))
			return "";
		try {
			StringBuilder out = new StringBuilder();
			truncation(out, string, maxLength, null);
			return out.toString();
		} catch (IOException e) {
		}
		return "";
	}

	public static String truncation(String string, int maxLength, String replace) {
		if (isEmpty(string))
			return "";
		try {
			StringBuilder out = new StringBuilder();
			truncation(out, string, maxLength, replace);
			return out.toString();
		} catch (IOException e) {
		}
		return "";
	}

	public static void truncation(Appendable out, String string, int maxLength) throws IOException {
		truncation(out, string, maxLength, null);
	}

	public static void truncation(Appendable out, String string, int maxLength, String replace) throws IOException {
		if ((isEmpty(string)) || (maxLength <= 0)) {
			return;
		}
		if (isEmpty(replace)) {
			replace = "...";
		}
		int index = 0;
		int end = Math.min(string.length(), maxLength);
		for (; index < end; index++) {
			out.append(string.charAt(index));
		}
		if (string.length() > maxLength)
			out.append(replace);
	}
	
	public static String format(String pattern, Map<String, String> v){
		Matcher matcher = REPLACE_VARIABLE_PATTERN.matcher(pattern);
		String keyStr = "";
		String key = "";
		String value = "";
		String re = pattern;
		while(matcher.find()){
			keyStr = matcher.group();
			key = keyStr.substring(2, keyStr.length() - 1);
			value = v.get(key);
			if(value == null){
				value = "";
			}
			re = re.replace(keyStr, value);
		}
		return re;
	}
	
	public static void main(String args[]){
		Map<String, String> m = new HashMap<String, String>();
		m.put("datetime", "测试");
		m.put("title", "测试");
		m.put("site", "测试");
		String pattern = "您好，您于${datetime}发布的借款“${title}”的筹款进度已经成功。您可以通过“我的${site}”（个人中心）-“借款申请查询”查看相关信息${pl}。";
		String re = StringHelper.format(pattern, m);
		System.out.println(pattern);
		System.out.println(re);
	}
}
