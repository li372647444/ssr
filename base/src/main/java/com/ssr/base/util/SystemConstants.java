package com.ssr.base.util;

import java.util.LinkedHashMap;
import java.util.Map;

import com.ssr.base.model.system.Function;
import com.ssr.base.model.system.Module;

public class SystemConstants {
	public static final String SESSION_FUNCTION = "session_function";
	public static final String SESSION_MENU = "session_menu";
	public static final String SESSION_USER = "session_user";
	public static final String SESSION_CUSTOMER = "session_customer_id";
	public static Map<String, Module> MODULE_MAP = new LinkedHashMap<String, Module>();
	public static Map<String, Function> FUNCTION_MAP = new LinkedHashMap<String, Function>();
	public static final String SESSION_VCODE_TIME = "session_vcode";
	public static final long SESSION_VCODE_EXPIRE = 5 * 60 * 1000;
	public static final int LOGIN_MAX_ERROR_TIMES = 5;
}
