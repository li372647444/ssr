package com.ssr.base.model.dynamic;

import java.util.List;

public class DynamicManage {
	
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 列   名
     */
    private List<String> colunmnNames;
    
    /**
     * 列   值
     */
    private List<Object> colunmnValues;
    
    /**
     * 判断 符号
     */
    private List<String> conditionSymbols;
    
    /**
     * 查询条件组装对象（列名 符号 值）
     */
    private List<QueryCondition> queryConditions;
    
    /**
     * 执行SQL
     */
    private String sql;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<String> getColunmnNames() {
		return colunmnNames;
	}

	public void setColunmnNames(List<String> colunmnNames) {
		this.colunmnNames = colunmnNames;
	}

	public List<Object> getColunmnValues() {
		return colunmnValues;
	}

	public void setColunmnValues(List<Object> colunmnValues) {
		this.colunmnValues = colunmnValues;
	}

	public List<String> getConditionSymbols() {
		return conditionSymbols;
	}

	public void setConditionSymbols(List<String> conditionSymbols) {
		this.conditionSymbols = conditionSymbols;
	}

	public List<QueryCondition> getQueryConditions() {
		return queryConditions;
	}

	public void setQueryConditions(List<QueryCondition> queryConditions) {
		this.queryConditions = queryConditions;
	}
}