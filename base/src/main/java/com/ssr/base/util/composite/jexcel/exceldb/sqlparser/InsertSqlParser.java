package com.ssr.base.util.composite.jexcel.exceldb.sqlparser;

import com.ssr.base.util.composite.jexcel.exceldb.ExcelSql;
import com.ssr.base.util.composite.jexcel.exceldb.ExcelSqlField;

public class InsertSqlParser extends ExcelSqlParser {
	private String orgSql;

	InsertSqlParser(String orgSql) {
		this.orgSql = orgSql;
	}

	public ExcelSql parse() throws Exception {
		ExcelSql excelSql = new ExcelSql();

		excelSql.setOrgSql(orgSql);

		StringBuffer sqlbuf = new StringBuffer();
		/**
		 * 解析第一个括号中的字符。
		 */
		int leftBracket1 = orgSql.indexOf("(");
		int rightBracket1 = orgSql.indexOf(")", leftBracket1);
		if (rightBracket1 == orgSql.length() - 1) {
			throw new Exception("sql语句的字段名必须指定：" + orgSql);
		}
		int leftBracket2 = orgSql.indexOf("(", rightBracket1 + 1);
		// 如果有函数，第二个反括弧不一定是sql语句结束
		int rightBracket2 = orgSql.indexOf(")", leftBracket2);

		if (!(leftBracket1 < rightBracket1 && rightBracket1 < leftBracket2 && leftBracket2 < rightBracket2)) {
			throw new Exception("sql语句不合法，是否括弧不匹配？：" + orgSql);
		}

		// 获取到第二个左括弧之前的sql字符串，不包括第２个左括弧
		sqlbuf.append(orgSql.substring(0, leftBracket2 + 1));
		String fieldStr = orgSql.substring(leftBracket1 + 1, rightBracket1);
		String valueStr = orgSql.substring(leftBracket2 + 1);

		String fields[] = fieldStr.split(",");
		// List valueList = new ArrayList();

		// 当前字符所处括弧层数
		int bracketLevel = 0;
		char vc[] = valueStr.toCharArray();
		for (int fieldIndex = 0, i = 0;; i++) {
			if (i >= vc.length) {
				throw new Exception("sql语句不合法，“values”关键字之后的sql语句缺少反括弧：" + orgSql);
			}

			char c = vc[i];
			/**
			 * 如果是[]之中的内容
			 */
			if (c == '[') {
				int tmp = valueStr.indexOf("]", i);
				if (tmp == -1) {
					throw new Exception("sql语句不合法，“[”括弧不匹配：" + orgSql);
				}
				if (fieldIndex >= fields.length) {
					throw new Exception("sql语句的字段名、值个数不能为0，或者字段名个数和值个数不一样：" + orgSql);
				}
				// 将中括弧添加进list中取
				// valueList.add(valueStr.substring(i+1,tmp));
				// 创建、添加sql字段配置对象
				excelSql.addField(new ExcelSqlField(fields[fieldIndex].trim(), valueStr.substring(i + 1, tmp).trim()));
				sqlbuf.append("?");
				// 将i移动到]所在的位置
				i = tmp;
				continue;
			}

			sqlbuf.append(c);

			if (c == '(') {
				bracketLevel++;
			} else if (c == ')') {
				bracketLevel--;
			}

			if (bracketLevel == 0 && c == ',') {
				// valueList=new ArrayList();
				fieldIndex++;
			}

			if (bracketLevel == -1) {
				if ((fieldIndex + 1) != fields.length) {
					throw new Exception("sql语句的字段名、值个数不能为0，或者字段名个数和值个数不一样：" + orgSql);
				}
				break;
			}
		}

		excelSql.setParsedSql(sqlbuf.toString().trim());

		return excelSql;
	}
}