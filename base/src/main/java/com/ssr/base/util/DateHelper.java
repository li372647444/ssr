package com.ssr.base.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateHelper {

	public static final long DAY_IN_MILLISECONDS = 86400000L;

	public static final long rollMonth(long date, int monthes) {
		if (monthes == 0) {
			return date;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);
		if (monthes > 0) {
			int month = calendar.get(2);
			for (int i = 0; i < monthes; i++) {
				if (month == 11) {
					calendar.roll(1, 1);
					month = -1;
				}
				month++;
			}
			calendar.roll(2, monthes);
			return calendar.getTimeInMillis();
		}
		for (int i = monthes; i < 0; i++) {
			if (calendar.get(2) == 0) {
				calendar.roll(1, -1);
			}
			calendar.roll(2, 1);
		}
		return calendar.getTimeInMillis();
	}
	
	private static Calendar calendar;
	private static Map<String, String> dates;
	public static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");


	/**
	 * 系统月份开始时间  与 结束时间
	 * @return 
	 */	
	public static Map<String, String> thisMonthly() {
		dates = new HashMap<String, String>();
		calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		dates.put("startTime", dateFormat.format(calendar.getTime()));
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MINUTE, 59);
		dates.put("endTime", dateFormat.format(calendar.getTime()));
		return dates;
	}

	/**
	 * 获取系统时间的上一个月 开始时间 与 结束时间
	 * @return
	 */
	public static Map<String, String> lastMonthly() {
		dates = new HashMap<String, String>();
		calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MONTH, -1);
		dates.put("startTime", dateFormat.format(calendar.getTime()));
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MINUTE, 59);
		dates.put("endTime", dateFormat.format(calendar.getTime()));
		return dates;
	}

	/**
	 * 获取本周开始时间 与 结束时间
	 * @return
	 */
	public static Map<String, String> thisWeeky() {
		dates = new HashMap<String, String>();
		calendar = Calendar.getInstance();
		int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
		if (day_of_week == 1) {
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MINUTE, 59);
			dates.put("endTime", dateFormat.format(calendar.getTime()));
			calendar.add(Calendar.WEEK_OF_MONTH, -1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			dates.put("startTime", dateFormat.format(calendar.getTime()));
		} else {
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			dates.put("startTime", dateFormat.format(calendar.getTime()));
			calendar.add(Calendar.WEEK_OF_MONTH, 1);
			calendar.set(Calendar.DAY_OF_WEEK, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MINUTE, 59);
			dates.put("endTime", dateFormat.format(calendar.getTime()));
		}
		return dates;
	}

	/**
	 * 获取上周 开始时间与结束时间
	 * @return
	 */
	public static Map<String, String> lastWeeky() {
		dates = new HashMap<String, String>();
		calendar = Calendar.getInstance();
		int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
		if (day_of_week == 1) {
			calendar.add(Calendar.WEEK_OF_YEAR, -1);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			dates.put("endTime", dateFormat.format(calendar.getTime()));
			calendar.add(Calendar.WEEK_OF_YEAR, -1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			dates.put("startTime", dateFormat.format(calendar.getTime()));
		} else {
			calendar.add(Calendar.WEEK_OF_MONTH, -1);
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			dates.put("startTime", dateFormat.format(calendar.getTime()));
			calendar.add(Calendar.WEEK_OF_MONTH, 1);
			calendar.set(Calendar.DAY_OF_WEEK, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MINUTE, 59);
			dates.put("endTime", dateFormat.format(calendar.getTime()));
		}

		return dates;
	}

	/**
	 * 获取今天的 开始时间 与 结束时间
	 * @return
	 */
	public static Map<String, String> thisDay() {
		dates = new HashMap<String, String>();
		calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		dates.put("startTime", dateFormat.format(calendar.getTime()));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MINUTE, 59);
		dates.put("endTime", dateFormat.format(calendar.getTime()));
		return dates;
	}

	/**
	 * 获取昨天的开始时间 与 结束时间
	 * @return
	 */
	public static Map<String, String> yesterDay() {
		dates = new HashMap<String, String>();
		calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		dates.put("startTime", dateFormat.format(calendar.getTime()));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MINUTE, 59);
		dates.put("endTime", dateFormat.format(calendar.getTime()));
		return dates;
	}

	/**
	 * 获取 当前年 开始时间与结束时间
	 * @return
	 */
	public static Map<String, String> thisYear() {
		dates = new HashMap<String, String>();
		calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.MONTH, 0);
		dates.put("startTime", dateFormat.format(calendar.getTime()));
		calendar.add(Calendar.YEAR, 1);
		calendar.add(Calendar.SECOND, -1);
		dates.put("endTime", dateFormat.format(calendar.getTime()));
		return dates;
	}

	/**
	 * 获取上年开始时间与结束时间
	 * @return
	 */
	public static Map<String, String> lastYear() {
		dates = new HashMap<String, String>();
		calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.add(Calendar.YEAR, -1);
		dates.put("startTime", dateFormat.format(calendar.getTime()));
		calendar.add(Calendar.YEAR, 1);
		calendar.add(Calendar.SECOND, -1);
		dates.put("endTime", dateFormat.format(calendar.getTime()));
		return dates;
	}

	/**
	 * 系统时间 + amount（天） 
	 * @param amount
	 * @return  返回系统时间加上传入天数 
	 * 			返回时分秒为不做处理
	 */
	public static Date todayAddAmount(int amount){
		calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, amount);
		return new Date(calendar.getTimeInMillis());
	}
	
	/**
	 * 系统时间 + amount（天） 
	 * @param amount
	 * @return 返回系统时间加上传入天数 
	 * 		        返回时分秒强制处理为：23:59:59
	 */
	public static Date todayAddAmount2(int amount){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, amount);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MINUTE, 59);
		return new Date(calendar.getTimeInMillis());
	}
	
	/**
	 * 根据身份证得到出生年月
	 * 
	 * @param cardID
	 * @return
	 */
	public static String getBirthday(String cardID) {
		StringBuffer tempStr = new StringBuffer("");
		if (cardID != null && cardID.trim().length() > 0) {
			if (cardID.trim().length() == 15) {
				tempStr.append(cardID.substring(6, 12));
				tempStr.insert(4, '-');
				tempStr.insert(2, '-');
				tempStr.insert(0, "19");
			} else if (cardID.trim().length() == 18) {
				tempStr = new StringBuffer(cardID.substring(6, 14));
				tempStr.insert(6, '-');
				tempStr.insert(4, '-');
			}
		}
		return tempStr.toString();
	}
	
	/**
	 * 时间比较
	 */
	  public static final boolean beforeDate(Date date1, Date date2)
	  {
	    if ((date1 == null) || (date2 == null)) {
	      return false;
	    }
	    Calendar calendar = Calendar.getInstance();
	    long time1 = getDate(calendar, date1);
	    long time2 = getDate(calendar, date2);
	    return time1 < time2;
	  }
	  private static final long getDate(Calendar calendar, Date date) {
		    calendar.setTimeInMillis(date.getTime());
		    calendar.set(10, 0);
		    calendar.set(12, 0);
		    calendar.set(13, 0);
		    calendar.set(14, 0);
		    return calendar.getTimeInMillis();
		  }
}
