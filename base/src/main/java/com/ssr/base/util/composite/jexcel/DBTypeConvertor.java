package com.ssr.base.util.composite.jexcel;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class DBTypeConvertor {
	private static Map<String, Object> typeBalance = new HashMap<String, Object>();
	static {
		typeBalance.put("string", new Integer(java.sql.Types.VARCHAR));
		typeBalance.put("varchar", new Integer(java.sql.Types.VARCHAR));
		typeBalance.put("varchar2", new Integer(java.sql.Types.VARCHAR));
		typeBalance.put("date", new Integer(java.sql.Types.DATE));
		typeBalance.put("double", new Integer(java.sql.Types.DOUBLE));
		typeBalance.put("float", new Integer(java.sql.Types.FLOAT));
		typeBalance.put("int", new Integer(java.sql.Types.INTEGER));
		typeBalance.put("integer", new Integer(java.sql.Types.INTEGER));
		typeBalance.put("long", new Integer(java.sql.Types.INTEGER));
		typeBalance.put("timestamp", new Integer(java.sql.Types.TIMESTAMP));
		typeBalance.put("boolean", new Integer(java.sql.Types.BOOLEAN));
	}

	/**
	 * 根据字符串获取类型
	 * 
	 * @param type
	 *            String
	 * @throws ZDKException
	 * @return int
	 */
	public static int getType(String typeStr) throws Exception {
		Integer t = (Integer) typeBalance.get(typeStr.trim().toLowerCase());

		if (t == null) {
			throw new Exception("不支持该种数据类型：" + typeStr);
		}

		return t.intValue();
	}

	public static Object convert(String value, int type) throws Exception {
		if (value == null) {
			return null;
		}

		switch (type) {
		case java.sql.Types.VARCHAR:
			return value;
		case java.sql.Types.DATE:
			// 日期使用yyyy-MM-dd转换
			return DateUtil.str2SqlDate(value, "yyyy-MM-dd");
		case java.sql.Types.DOUBLE:
			return Double.valueOf(value);
		case java.sql.Types.FLOAT:
			return Float.valueOf(value);
		case java.sql.Types.INTEGER:
			String v = value;
			int i = value.indexOf(".");
			if (i != -1) {
				v = value.substring(0, i);
			}
			return Integer.valueOf(v);
		case java.sql.Types.TIMESTAMP:
			// Timestamp 格式应改为 yyyy-MM-dd hh:mm:ss
			return Timestamp.valueOf(value);
		default:
			throw new Exception("值[" + value + "]的类型[" + type + "]不确定！");
		}
	}

	/**
	 * 根据格式转换日期
	 * 
	 * @param value
	 *            String
	 * @param format
	 *            String
	 * @throws ParseException
	 * @return Date
	 */
	public static java.sql.Date convertDate(String value, String format) throws ParseException {
		return DateUtil.str2SqlDate(value, format);
	}

}