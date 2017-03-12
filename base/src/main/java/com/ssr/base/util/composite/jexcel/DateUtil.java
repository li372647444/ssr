package com.ssr.base.util.composite.jexcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static java.sql.Date str2SqlDate(String value,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		java.sql.Date date = null;
		try {
			Date d = sdf.parse(value);
			date = new java.sql.Date(d.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String date2Str(Date date, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
}