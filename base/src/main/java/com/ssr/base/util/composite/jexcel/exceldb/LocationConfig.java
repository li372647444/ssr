package com.ssr.base.util.composite.jexcel.exceldb;

public class LocationConfig {
	// id
	private String id;
	// sheet名称
	private String sheetName;
	// 开始查找行
	private int startRow;
	// 开始查找列
	private short startCol;
	// 开始查找字符串
	private String startStr;
	// 相对行位移
	private int relativeX;
	// 列号
	private short absCol;
	// 数据库数据类型
	private int type;
	// 数据库数据类型格式
	private String format = null;
	// 代码类型
	private String codeType;

	public String getStartStr() {
		return startStr;
	}

	public void setStartStr(String startStr) {
		this.startStr = startStr;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public short getStartCol() {
		return startCol;
	}

	public void setStartCol(short startCol) {
		this.startCol = startCol;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public int getRelativeX() {
		return relativeX;
	}

	public void setRelativeX(int relativeX) {
		this.relativeX = relativeX;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public short getAbsCol() {
		return absCol;
	}

	public void setAbsCol(short absCol) {
		this.absCol = absCol;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

}