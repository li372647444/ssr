package com.ssr.base.util.constantenum;

public enum MysqlColumnTypeEnum {
	
	INTEGER("integer","integer","java.lang.Integer"),
	VARCHAR("varchar","varchar","java.lang.String"),
	DATETIME("datetime","datetime","java.util.Date"),
	DATE("date","date","java.util.Date"),
	ENUM("enum","enum","java.lang.String"),
	DOUBLE("double","double","java.lang.Double"),
	DECIMAL("decimal","decimal","java.math.BigDecimal"),
	TEXT("text","text","java.lang.String"),
	BLOB("blob","blob","java.lang.String");
	
	String index;
	String name;
	String javaClass;
	
	private MysqlColumnTypeEnum(String index,String name,String javaClass){
		this.index = index;
		this.name = name;
		this.javaClass = javaClass;
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

	public String getJavaClass() {
		return javaClass;
	}

	public void setJavaClass(String javaClass) {
		this.javaClass = javaClass;
	}
}
