package com.ssr.base.model.dynamic;

public class QueryCondition {
	private String colunmnName;
	private String conditionSymbol;
	private Object colunmnValue;
	
	public QueryCondition(String colunmnName,String conditionSymbol,Object colunmnValue){
		this.colunmnName = colunmnName;
		this.conditionSymbol = conditionSymbol;
		this.colunmnValue = colunmnValue;
	}
	
	public String getColunmnName() {
		return colunmnName;
	}
	public void setColunmnName(String colunmnName) {
		this.colunmnName = colunmnName;
	}
	public Object getColunmnValue() {
		return colunmnValue;
	}
	public void setColunmnValue(Object colunmnValue) {
		this.colunmnValue = colunmnValue;
	}
	public String getConditionSymbol() {
		return conditionSymbol;
	}
	public void setConditionSymbol(String conditionSymbol) {
		this.conditionSymbol = conditionSymbol;
	}
}
