package com.ssr.base.util.composite.jexcel.exceldb.sqlparser;

import com.ssr.base.util.composite.jexcel.exceldb.ExcelSql;
import com.ssr.base.util.composite.jexcel.exceldb.ExcelSqlField;

public class SimpleSqlParser extends ExcelSqlParser {
	private String orgSql;

	public SimpleSqlParser(String orgSql) {
		this.orgSql = orgSql;
	}

	/**
	 * 只解析[]
	 * 
	 * @throws Exception
	 * @return ExcelSql
	 */
	public ExcelSql parse() throws Exception {
		ExcelSql excelSql = new ExcelSql();

		excelSql.setOrgSql(orgSql);

		StringBuffer sqlbuf = new StringBuffer();

		String valueStr = orgSql;

		char vc[] = valueStr.toCharArray();
		for (int i = 0; i < vc.length; i++) {

			char c = vc[i];
			/**
			 * 如果是[]之中的内容
			 */
			if (c == '[') {
				int tmp = valueStr.indexOf("]", i);
				if (tmp == -1) {
					throw new Exception("sql语句不合法，“[”括弧不匹配：" + orgSql);
				}

				// 创建、添加sql字段配置对象
				excelSql.addField(new ExcelSqlField("noneMemoryFieldName", valueStr.substring(i + 1, tmp)));
				sqlbuf.append("?");
				// 将i移动到]所在的位置
				i = tmp;
				continue;
			}

			sqlbuf.append(c);

		}

		excelSql.setParsedSql(sqlbuf.toString().trim());

		return excelSql;

	}

	// /**
	// * 解析字段名的方式（为完成，为解析where之后）
	// * @throws Exception
	// * @return ExcelSql
	// */
	// public ExcelSql parse() throws Exception{
	// ExcelSql excelSql=new ExcelSql();
	//
	// excelSql.setOrgSql(orgSql);
	//
	// StringBuffer sqlbuf = new StringBuffer();
	// int headEnd=orgSql.indexOf(" set ")+5;
	// String head=orgSql.substring(0,headEnd);
	// sqlbuf.append(head);
	// int whereBegin=orgSql.indexOf(" where ",headEnd);
	// String whereStr=null;
	// if (whereBegin!=-1){
	// whereStr = orgSql.substring(whereBegin);
	// }
	//
	// String valueStr=null;
	// if (whereBegin!=-1){
	// valueStr = orgSql.substring(headEnd, whereBegin);
	// }else{
	// valueStr = orgSql.substring(headEnd);
	// }
	//
	// int bracketLevel=0;
	// char vc[]=valueStr.toCharArray();
	// String fieldName=null;
	// for (int i=0;i<vc.length;i++){
	//
	// char c=vc[i];
	// /**
	// * 如果是[]之中的内容
	// */
	// if (c=='['){
	// int tmp=valueStr.indexOf("]",i);
	// if (tmp==-1){
	// throw new Exception("sql语句不合法，“[”括弧不匹配：" + orgSql);
	// }
	//
	// if (fieldName==null){
	// throw new
	// Exception("sql语句不合法，没找到值“"+valueStr.substring(i+1,tmp)+"”对应的字段名称:"+orgSql);
	// }
	// //创建、添加sql字段配置对象
	// excelSql.addField(new ExcelSqlField(fieldName,
	// valueStr.substring(i+1,tmp)));
	// fieldName=null;
	// sqlbuf.append("?");
	// //将i移动到]所在的位置
	// i=tmp;
	// continue;
	// }
	//
	// sqlbuf.append(c);
	//
	// if (c=='('){
	// bracketLevel++;
	// }else if (c==')'){
	// bracketLevel--;
	// }
	//
	// if (bracketLevel==0 && c==','){
	// int tmp=valueStr.indexOf("=",i);
	// if (tmp==-1){
	// throw new Exception("sql语句不合法，缺少“=”号：" + orgSql);
	// }
	// fieldName=valueStr.substring(i+1,tmp);
	// sqlbuf.append(fieldName).append("=");
	// i=tmp;
	// }
	//
	// }
	//
	// if (bracketLevel!=0){
	// throw new Exception("sql语句不合法，正反括弧数目不匹配：" + orgSql);
	// }
	//
	// if (whereStr!=null ){
	// sqlbuf.append(whereStr);
	// }
	// excelSql.setParsedSql(sqlbuf.toString());
	//
	// return excelSql;
	//
	// }
}
