package com.ssr.base.util.composite.jexcel.exceldb;

import java.util.ArrayList;
import java.util.Iterator;

public class ExcelFLConfig {
	/** id */
	private String id;

	/** 开始搜索行 */
	private int startRow = 0;

	/** 开始搜索列 */
	private short startCol = 0;

	/** 开始字符串 */
	private String startStr;

	/** 结束列 */
	private short endCol = 0;

	/** 结束字符串 */
	private String endstr;

	/** 没有解析前的sql，包括多个sql，使用;号分隔开 */
	private String orgiSqls;

	/** 当前sql语句对应字段的配置 */

	/** 类型，form或者loop */
	private boolean form;
	// 是否是循环容器，循环容器不入库，只作循环。
	private boolean loopContenter = false;

	/** 子配置对象，也就是里面放置又放置ExcelDBConfig */
	private java.util.List<ExcelFLConfig> flConfigChildren = new ArrayList<ExcelFLConfig>();

	// 父对象
	private ExcelFLConfig parent;
	private java.util.List<ExcelSql> excelSqls = new ArrayList<ExcelSql>();

	// 读取的单元格数据是否只是保存在数据库中，并不存入数据库
	private boolean justMemory = false;

	// 原始存储过程
	private String orgProcedure;
	// 解析之后的存储过程
	private String parsedProcedure;

	/**
	 * 是否是子配置。是:true,否:false
	 * 
	 * @return boolean
	 */
	public boolean isRoot() {
		return parent == null;
	}

	/**
	 * 父对象是form还是loop，form:true，loop:fasle
	 * 
	 * @return boolean
	 */
	public boolean isParentForm() {
		return parent.isForm();
	}

	public Iterator<ExcelFLConfig> getFLChildrenIterator() {
		return flConfigChildren.iterator();
	}

	public void addFLConfigChild(ExcelFLConfig flc) {
		flConfigChildren.add(flc);
	}

	public java.util.Iterator<ExcelSql> getExcelSqlIterator() {
		return excelSqls.iterator();
	}

	public void addExcelSql(ExcelSql es) {
		excelSqls.add(es);
	}

	public boolean isForm() {
		return form;
	}

	public void setForm(boolean form) {
		this.form = form;
	}

	public short getEndCol() {
		return endCol;
	}

	public void setEndCol(short endCol) {
		this.endCol = endCol;
	}

	public String getEndstr() {
		return endstr;
	}

	public void setEndstr(String endstr) {
		this.endstr = endstr;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrgiSqls() {
		return orgiSqls;
	}

	public void setOrgiSqls(String orgiSqls) {
		this.orgiSqls = orgiSqls;
	}

	public short getStartCol() {
		return startCol;
	}

	public void setStartCol(short startCol) {
		this.startCol = startCol;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public String getStartStr() {
		return startStr;
	}

	public ExcelFLConfig getParent() {
		return parent;
	}

	public void setStartStr(String startStr) {
		this.startStr = startStr;
	}

	public void setParent(ExcelFLConfig parent) {
		this.parent = parent;
	}

	public boolean isLoopContenter() {
		return loopContenter;
	}

	public void setLoopContenter(boolean loopContenter) {
		this.loopContenter = loopContenter;
	}

	public boolean isJustMemory() {
		return justMemory;
	}

	public void setJustMemory(boolean justMemory) {
		this.justMemory = justMemory;
	}

	public String getParsedProcedure() {
		return parsedProcedure;
	}

	public void setParsedProcedure(String parsedProcedure) {
		this.parsedProcedure = parsedProcedure;
	}

	public String getOrgProcedure() {
		return orgProcedure;
	}

	public void setOrgProcedure(String orgProcedure) {
		this.orgProcedure = orgProcedure;
	}

}