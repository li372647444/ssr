package com.ssr.base.model.dynamic;

import java.util.List;
import java.util.Map;

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
     * 新增记录  （列名）
     */
    private List<String> colunmnNames;
    /**
     * 新增记录  （至）
     */
    private List<Object> colunmnValues;
    
    /**
     * 查询条件组装对象（列名 符号 值）
     */
    private List<QueryCondition> queryConditions;
    /**
     * 修改属性与值（  属性、值 ）
     */
    private Map<String,Object> updateValues;
    
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

	public List<QueryCondition> getQueryConditions() {
		return queryConditions;
	}

	public void setQueryConditions(List<QueryCondition> queryConditions) {
		this.queryConditions = queryConditions;
	}

	public Map<String, Object> getUpdateValues() {
		return updateValues;
	}

	public void setUpdateValues(Map<String, Object> updateValues) {
		this.updateValues = updateValues;
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
}