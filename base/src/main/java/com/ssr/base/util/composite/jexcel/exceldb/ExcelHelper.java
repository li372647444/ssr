package com.ssr.base.util.composite.jexcel.exceldb;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.ssr.base.util.composite.jexcel.DateUtil;

/**
 * excel帮助类
 * 
 */
public class ExcelHelper {
	private static NumberFormat nf = DecimalFormat.getInstance();
	private static InverCodeListManager iCodeListManager = new InverCodeListManager();
	static {
		nf.setGroupingUsed(false);
	}

	/**
	 * 将字符串转换为sql值对象
	 * 
	 * @param sValue
	 *            String
	 * @param sqlType
	 *            int
	 * @param format
	 *            String
	 * @param codeType
	 *            String
	 * @param servletContext
	 *            ServletContext
	 * @throws Exception
	 * @return Object
	 */
	public static Object str2sqlValue(String sValue, int sqlType, String format, String codeType,
			javax.servlet.ServletContext servletContext) throws Exception {
		if (sValue == null || sValue.trim().length() == 0) {
			return null;
		}

		if (format == null || format.trim().length() == 0) {
			format = "yyyy-MM-dd";
		}

		sValue = sValue.trim();

		// 代码表转换
		if (codeType != null && codeType.trim().length() != 0) {
			sValue = iCodeListManager.getCodeValue(servletContext, codeType, sValue);
		}

		switch (sqlType) {
		case java.sql.Types.INTEGER:
		case java.sql.Types.FLOAT:
		case java.sql.Types.DOUBLE:
			return new BigDecimal(sValue);
		case java.sql.Types.CHAR:
		case java.sql.Types.VARCHAR:
			return sValue;
		case java.sql.Types.DATE:
			return DateUtil.str2SqlDate(sValue, format);
		case java.sql.Types.TIMESTAMP:
			return java.sql.Timestamp.valueOf(sValue);
		case java.sql.Types.BOOLEAN:
			if (sValue == null || sValue.length() == 0) {
				sValue = "false";
			}
			return Boolean.valueOf(sValue);
		default:
			throw new Exception("值[" + sValue + "]不能从excel单元格的String类型转换为类型[" + sqlType + "]！");
		}
	}

	public static Object getCellObjectValue(HSSFRow row, short colIndex, int sqlType, String format, String codeType,
			javax.servlet.ServletContext servletContext) throws Exception {
		if (row == null) {
			return null;
		}

		HSSFCell cell = row.getCell(colIndex);

		try {
			return getCellObjectValue(cell, sqlType, format, codeType, servletContext);
		} catch (Exception ex) {
			throw new Exception("读取sheet单元格行[" + row.getRowNum() + "]列[" + colIndex + "]发生错误:" + ex.getMessage(), ex);
		}
	}

	/**
	 * 根据行列位置读取单元格值，自动以obj的形式返回，如果是date，如果是基本类型，则返回其包装类型
	 * 
	 * @param sheet
	 *            HSSFSheet
	 * @param rowIndex
	 *            int
	 * @param colIndex
	 *            short
	 * @param sqlType
	 *            int 对应的数据库类型
	 * @param format
	 *            String 对应的数据库字段格式
	 * @throws Exception
	 * @return Object
	 */
	public static Object getCellObjectValue(HSSFSheet sheet, int rowIndex, short colIndex, int sqlType, String format,
			String codeType, javax.servlet.ServletContext servletContext) throws Exception {
		HSSFRow row = sheet.getRow(rowIndex);
		if (row == null) {
			return null;
		}

		HSSFCell cell = row.getCell(colIndex);

		try {
			return getCellObjectValue(cell, sqlType, format, codeType, servletContext);
		} catch (Exception ex) {
			throw new Exception("读取sheet单元格行[" + rowIndex + "]列[" + colIndex + "]发生错误:" + ex.getMessage(), ex);
		}
	}

	/**
	 * 读取cell中的值，自动以obj的形式返回，如果是date，如果是基本类型，则返回其包装类型
	 * 
	 * @param cell
	 *            HSSFCell
	 * @param sqlType
	 *            int 对应的数据库类型
	 * @param formate
	 *            int 对应的数据库字段格式
	 * @param codeType
	 *            string 代码表翻译类型
	 * @throws Exception
	 * @return Object
	 */
	@SuppressWarnings("deprecation")
	private static Object getCellObjectValue(HSSFCell cell, int sqlType, String format, String codeType,
			javax.servlet.ServletContext servletContext) throws Exception {
		if (cell == null) {
			return null;
		}

		if (format == null) {
			format = "yyy-MM-dd";
		}

		/**
		 * 其他类型
		 */
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC:

			/**
			 * 如果cell单元格是日期型，是否是日期型必须要在numeric中才能判断
			 */
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				java.util.Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
				if (sqlType == java.sql.Types.DATE) {
					// 如果入库字段也是date型
					return new java.sql.Date(date.getTime());
				} else if (sqlType == java.sql.Types.TIMESTAMP) {
					// 如果入库字段是timestamp型
					return new java.sql.Timestamp(date.getTime());
				} else if (sqlType == java.sql.Types.VARCHAR) {
					// 如果入库字段是string型
					return DateUtil.date2Str(date, format);
				} else {
					throw new Exception("日期[" + date + "]不能从excel单元格的日期类型转换为类型[" + sqlType + "]！");
				}
			}

			/**
			 * 如果单元格只是number类型
			 */
			double dv = cell.getNumericCellValue();
			if (codeType != null && codeType.trim().length() != 0) {
				throw new Exception("值[" + dv + "]在excel单元格中为NUMERIC类型，不能指定它根据代码类型[" + codeType + "]进行转译！");
			}
			switch (sqlType) {
			case java.sql.Types.INTEGER:
			case java.sql.Types.FLOAT:
			case java.sql.Types.DOUBLE:
				return new BigDecimal(dv);
			case java.sql.Types.CHAR:
			case java.sql.Types.VARCHAR:
				return nf.format(dv);
			default:
				throw new Exception("值[" + dv + "]不能从excel单元格的NUMERIC转换为类型[" + sqlType + "]！");
			}
		case HSSFCell.CELL_TYPE_BOOLEAN:

			/**
			 * 如果单元格是boolean类型
			 */
			boolean bv = cell.getBooleanCellValue();
			if (codeType != null && codeType.trim().length() != 0) {
				throw new Exception("值[" + bv + "]在excel单元格中为boolean类型，不能指定它根据代码类型[" + codeType + "]进行转译！");
			}
			switch (sqlType) {
			case java.sql.Types.BOOLEAN:
				return new Boolean(bv);
			case java.sql.Types.CHAR:
			case java.sql.Types.VARCHAR:
				return String.valueOf(bv);
			default:
				throw new Exception("值[" + bv + "]不能从excel单元格的Boolan类型转换为类型[" + sqlType + "]！");
			}
		default:

			/**
			 * 其他全部作为string类型处理
			 */
			String sv = cell.getStringCellValue();
			return str2sqlValue(sv, sqlType, format, codeType, servletContext);
		}
	}

}