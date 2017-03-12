package com.ssr.base.util.composite.jexcel.exceldb.sqlparser;

import com.ssr.base.util.composite.jexcel.exceldb.ExcelSql;

public abstract class ExcelSqlParser {

	public static ExcelSqlParser getInstance(String orgSql) throws Exception {
		if (orgSql == null || orgSql.trim().length() == 0) {
			throw new Exception("解析sql时没有发现需要解析的sql语句，请检查配置！");
		}
		orgSql = orgSql.trim();
		String head = orgSql.substring(0, orgSql.indexOf(" "));
		if (head.toLowerCase().equals("insert")) {
			return new InsertSqlParser(orgSql);
		} else if (head.toLowerCase().equals("update")) {
			return new SimpleSqlParser(orgSql);
		} else if (head.toLowerCase().equals("delete")) {
			return new SimpleSqlParser(orgSql);
		} else if (orgSql.indexOf("{") == 0) {
			// 存储过程
			return new SimpleSqlParser(orgSql);
		} else {
			throw new Exception("sql语句必须以insert、update、{、或者delete开头！");
		}
	}

	abstract public ExcelSql parse() throws Exception;
}
