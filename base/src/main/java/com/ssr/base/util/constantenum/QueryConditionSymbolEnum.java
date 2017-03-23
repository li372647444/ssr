package com.ssr.base.util.constantenum;

public enum QueryConditionSymbolEnum {
	
	EQUAL("EQUAL","EQUAL","=","等于"),
	GREATERTHAN("GREATERTHAN","GREATERTHAN",">","大于"),
	GREATEROREQUAL("GREATEROREQUAL","GREATEROREQUAL",">=","大于等于"),
	LESSTHAN("LESSTHAN","LESSTHAN","<","小于"),
	LESSTHANOREQUAL("LESSTHANOREQUAL","LESSTHANOREQUAL","<=","小于等于"),
	LIKE("LIKE","LIKE","like","模糊匹配"),
	LEFTLIKE("LEFTLIKE","LEFTLIKE","leftlike","左模糊匹配"),
	RIGHTLIKE("RIGHTLIKE","RIGHTLIKE","rightlike","右模糊匹配"),
	BETWEEN("BETWEEN","BETWEEN","between","...之前"),
	IN("IN","IN","in","之内"),
	ISNULL("ISNULL","ISNULL","isnull","为空"),
	ISNOTNULL("ISNOTNULL","ISNOTNULL","isnotnull","不为空");
	
	String index;
	String name;
	String symbol;
	String desc;
	
	private QueryConditionSymbolEnum(String index,String name,String symbol,String desc){
		this.index = index;
		this.name = name;
		this.symbol = symbol;
		this.desc = desc;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
