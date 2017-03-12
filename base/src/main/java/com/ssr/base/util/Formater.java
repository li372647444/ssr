package com.ssr.base.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formater {
	public static final DecimalFormat RATE_FORMAT = new DecimalFormat("#0.00");

	public static final DecimalFormat RATE_PERCENT_FORMAT = new DecimalFormat("#0.00%");

	public static final DecimalFormat PROESS_FORMAT = new DecimalFormat("#0%");

	public static final DecimalFormat AMOUNT_SPLIT_FORMAT = new DecimalFormat("#,##,##0.00");

	public static final DecimalFormat AMOUNT_FORMAT = new DecimalFormat("#####0.00");

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

	public static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static final SimpleDateFormat DATE_FORMAT_YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
	
	/**
	 * 日期字符串转化为时间戳
	 * @param date
	 * @return
	 */
	public static long dateStrToTimeStamp(String date){
		long v = 0;
		try {
			v = DATETIME_FORMAT.parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return v;
	}
	
	/**
	 * 时间戳转化为时间字符串
	 * @param time
	 * @return
	 */
	public static String timeStampToDateStr(long time){
		return DATETIME_FORMAT.format(new Date(time));
	}
	
	/**
	 * 通用将日期转换为固定格式
	 * @param date 日期
	 * @param format 格式
	 * @return
	 */
	public static String formatToDate(Date date, String format) {
		if (date == null) {
			return "";
		}
		return new SimpleDateFormat(format).format(date);
	}
	
	public static String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		return DATE_FORMAT.format(date);
	}
	
	public static String formatDateYYYYMMDD(Date date) {
		if (date == null) {
			return "";
		}
		return DATE_FORMAT_YYYYMMDD.format(date);
	}

	public static String formatTime(Date date) {
		if (date == null) {
			return "";
		}
		return TIME_FORMAT.format(date);
	}

	public static String formatDateTime(Date date) {
		if (date == null) {
			return "";
		}
		return DATETIME_FORMAT.format(date);
	}

	public static String formatRate(float number) {
		return RATE_PERCENT_FORMAT.format(number);
	}

	public static String formatRate(double number) {
		return RATE_PERCENT_FORMAT.format(number);
	}

	public static String formatRate(BigDecimal number) {
		return RATE_PERCENT_FORMAT.format(number);
	}

	public static String formatRate(float number, boolean includePercent) {
		return includePercent ? RATE_PERCENT_FORMAT.format(number) : RATE_FORMAT.format(number * 100.0F);
	}

	public static String formatRate(double number, boolean includePercent) {
		return includePercent ? RATE_PERCENT_FORMAT.format(number) : RATE_FORMAT.format(number * 100.0D);
	}

	public static String formatRate(BigDecimal number, boolean includePercent) {
		return includePercent ? RATE_PERCENT_FORMAT.format(number)
				: RATE_FORMAT.format(number.multiply(new BigDecimal(100)));
	}

	public static int roundProgress(double number) {
		double v = number * 100.0D;
		if (v <= 0.0D) {
			return 0;
		}
		if (v <= 1.0D) {
			return 1;
		}
		return (int) (Math.floor(number * 100.0D) / 100.0D);
	}

	public static int roundProgress(float number) {
		double v = number * 100.0F;
		if (v <= 0.0D) {
			return 0;
		}
		if (v <= 1.0D) {
			return 1;
		}
		return (int) (Math.floor(number * 100.0F) / 100.0D);
	}

	public static String formatProgress(double number) {
		double v = number * 100.0D;
		if (v <= 0.0D) {
			return "0%";
		}
		if (v <= 1.0D) {
			return "1%";
		}
		return PROESS_FORMAT.format(Math.floor(number * 100.0D) / 100.0D);
	}

	public static String formatProgress(float number) {
		double v = number * 100.0F;
		if (v <= 0.0D) {
			return "0%";
		}
		if (v <= 1.0D) {
			return "1%";
		}
		return PROESS_FORMAT.format(Math.floor(number * 100.0F) / 100.0D);
	}

	public static String formatAmount(int number) {
		return AMOUNT_SPLIT_FORMAT.format(number);
	}

	public static String formatAmount(long number) {
		return AMOUNT_SPLIT_FORMAT.format(number);
	}

	public static String formatAmount(float number) {
		return AMOUNT_SPLIT_FORMAT.format(number);
	}

	public static String formatAmount(double number) {
		return AMOUNT_SPLIT_FORMAT.format(number);
	}

	public static String formatAmount(BigDecimal number) {
		if (number == null) {
			return "";
		}
		return AMOUNT_SPLIT_FORMAT.format(number);
	}

	public static String formatAmount(BigInteger number) {
		if (number == null) {
			return "";
		}
		return AMOUNT_SPLIT_FORMAT.format(number);
	}

	public static String formatAmount(int number, boolean split) {
		return split ? AMOUNT_SPLIT_FORMAT.format(number) : AMOUNT_FORMAT.format(number);
	}

	public static String formatAmount(long number, boolean split) {
		return split ? AMOUNT_SPLIT_FORMAT.format(number) : AMOUNT_FORMAT.format(number);
	}

	public static String formatAmount(float number, boolean split) {
		return split ? AMOUNT_SPLIT_FORMAT.format(number) : AMOUNT_FORMAT.format(number);
	}

	public static String formatAmount(double number, boolean split) {
		return split ? AMOUNT_SPLIT_FORMAT.format(number) : AMOUNT_FORMAT.format(number);
	}

	public static String formatAmount(BigDecimal number, boolean split) {
		if (number == null) {
			return "";
		}
		return split ? AMOUNT_SPLIT_FORMAT.format(number) : AMOUNT_FORMAT.format(number);
	}

	public static String formatAmount(BigInteger number, boolean split) {
		if (number == null) {
			return "";
		}
		return split ? AMOUNT_SPLIT_FORMAT.format(number) : AMOUNT_FORMAT.format(number);
	}
}
