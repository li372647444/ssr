package com.ssr.base.util.composite.jexcel.exceldb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 一个sql对象，包括了原始sql，解析之后的sql，字段
 * 
 */
public class ExcelSql {

  /** 没有解析前的sql */
  private String orgSql;
  //解析完成的sql段如：insert into t (f1,f2) values (?,?)
  private String parsedSql;

  private List<ExcelSqlField> fields =new ArrayList<ExcelSqlField>();

  public String getOrgSql() {
    return orgSql;
  }
  public void setOrgSql(String orgSql) {
    this.orgSql = orgSql;
  }
  public String getParsedSql() {
    return parsedSql;
  }
  public void setParsedSql(String parsedSql) {
    this.parsedSql = parsedSql;
  }
  public Iterator<ExcelSqlField> getFieldsIterator() {
    return fields.iterator();
  }
  public void addField(ExcelSqlField field) {
    this.fields.add(field);
  }

}