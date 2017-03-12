package com.ssr.base.util.composite.jexcel.exceldb;

import com.ssr.base.util.composite.jexcel.DBTypeConvertor;

public class ExcelSqlField {

	// 字段名称
	private String fieldName;

	// 字段类型
	private int fieldType;

	// id，如果是loop,form,location则有id
	private String id;

	// 序列名称
	private String seqName;

	// 列号
	private short absCol;

	// 行相对位移
	private int relativeX;

	// 字段格式
	private String format;

	// 从内存中取数据的字段名称
	private String memoryFieldName;

	// 要根据代码表将名称翻译成值的代码类型
	private String codeTypeName;

	/**
	 * 字段从excel中的取值方式： 1-序列取关键字[sequences.seqName:string]
	 * 2-form的excel单元格读取[relativeX.absCol:string]
	 * 3-loop中的excel单元格读取[absCol:date:yyyy-MM-dd]
	 * 4-location的内存中读取[location.id:string]
	 * 5-form的内存中读取[form.id.fieldName:float] 6-loop的内存中读取[loop.id.fieldName:int]
	 */
	private int retrieveTyp;

	public static final int SEQUENCES = 1;
	public static final int FORM_EXCEL = 2;
	public static final int LOOP_EXCEL = 3;
	public static final int LOCATION_MOMORY = 4;
	public static final int FORM_MEMORY = 5;
	public static final int LOOP_MEMORY = 6;
	// 从参数中取
	public static final int PARAMETER = 7;

	/**
	 * 构造函数，根据fieldName，配置的值字符串，生成sql字段
	 * 
	 * @param fieldName
	 *            String
	 * @param valueStr
	 *            String
	 */
	public ExcelSqlField(String fieldName, String valueStr) throws Exception {
		this.fieldName = fieldName;
		// 去掉方括号[]
		String tmpe = valueStr;

		/**
		 * 根据冒号拆分
		 */
		String vs[] = tmpe.split(":");
		if (vs.length == 3) {
			// 如果分割为3段，第三段为字段类型的格式，设置它，如日期的yyyy-MM-dd
			this.format = vs[2];
		} else if (vs.length != 2) {
			throw new Exception("[" + fieldName + "]字段的值" + valueStr + "的配置格式不正确，应该由一个或者两个冒号分隔开！");
		}

		/**
		 * 冒号分割后的第2段，为字段类型，并有可能包括代码标转换类型，设置他们
		 */
		String tmps[] = vs[1].split("@");
		if (!(tmps.length == 1 || tmps.length == 2)) {
			throw new Exception("[" + fieldName + "]字段的值" + valueStr + "的配置格式不正确，请检查是否写了两个以上(包括2个)‘@’符号！");
		}
		if (tmps.length == 2) {
			// 设置代码表转换类型
			this.setCodeTypeName(tmps[1].trim());
		}
		// 设置字段类型
		this.fieldType = DBTypeConvertor.getType(tmps[0]);

		/**
		 * 冒号分割后的第1段，为字段的取值方式，解析取值方式
		 */
		// 首先根据@判断是否是从参数获取数据。
		if (vs[0].indexOf("@") == 0) {
			// 有@符号，从参数中取
			this.retrieveTyp = ExcelSqlField.PARAMETER;
			this.memoryFieldName = vs[0].substring(1);
		} else {
			// 从excel中取
			// 按‘点’分割
			String s[] = vs[0].split("[.]");
			s[0] = s[0].trim().toLowerCase();
			try {
				if (s[0].equals("sequences")) {
					// 是序列类型
					this.retrieveTyp = SEQUENCES;
					this.seqName = s[1];
				} else if (s[0].equals("location")) {
					// 是location
					this.retrieveTyp = ExcelSqlField.LOCATION_MOMORY;
					this.id = s[1];
				} else if (s[0].equals("loop")) {
					// 是loop-memory
					this.retrieveTyp = ExcelSqlField.LOOP_MEMORY;
					this.id = s[1];
					this.memoryFieldName = s[2];
				} else if (s[0].equals("form")) {
					// 是form-memory
					this.retrieveTyp = ExcelSqlField.FORM_MEMORY;
					this.id = s[1];
					this.memoryFieldName = s[2];
				} else if (s.length == 1) {
					// 是loop-excel
					this.retrieveTyp = ExcelSqlField.LOOP_EXCEL;
					this.absCol = Short.parseShort(s[0]);
				} else if (s.length == 2) {
					// 是form-excel
					this.retrieveTyp = ExcelSqlField.FORM_EXCEL;
					this.relativeX = Integer.parseInt(s[0]);
					this.absCol = Short.parseShort(s[1]);
				} else {
					throw new Exception("[" + fieldName + "]字段对应的值" + valueStr + "的配置格式不正确，取值方式不是规定的！");
				}
			} catch (Exception ex) {
				if (!(ex instanceof Exception)) {
					throw new Exception("[" + fieldName + "]字段对应的值的" + valueStr + "配置格式不正确，请检查配置！");
				}
			}
		}

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

	public int getFieldType() {
		return fieldType;
	}

	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public short getAbsCol() {
		return absCol;
	}

	public void setAbsCol(short absCol) {
		this.absCol = absCol;
	}

	public String getSeqName() {
		return seqName;
	}

	public void setSeqName(String seqName) {
		this.seqName = seqName;
	}

	public int getRetrieveTyp() {
		return retrieveTyp;
	}

	public void setRetrieveTyp(int retrieveTyp) {
		this.retrieveTyp = retrieveTyp;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getMemoryFieldName() {
		return memoryFieldName;
	}

	public void setMemoryFieldName(String memoryFieldName) {
		this.memoryFieldName = memoryFieldName;
	}

	public String getCodeTypeName() {
		return codeTypeName;
	}

	public void setCodeTypeName(String codeTypeName) {
		this.codeTypeName = codeTypeName;
	}

}