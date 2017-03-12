package com.ssr.base.util.composite.jexcel.exceldb;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 配置管理
 * 
 */
public class ExcelConfigManager {
	// 默认数据库连接获取，关闭实现类
	private String connectionSuperviseClass = "com.ssr.base.util.composite.jexcel.exceldb.ConnectionSuperviseImpl";
	private Map<String, ExcelSheetConfig> sheetConfigs = new HashMap<String, ExcelSheetConfig>();

	// 获取sheet配置
	public ExcelSheetConfig getExcelSheetConfig(String sheetName) {
		return (ExcelSheetConfig) sheetConfigs.get(sheetName);
	}

	// 获取第一个ExcelSheetConfig的值
	public ExcelSheetConfig getFirstExcelSheetConfig() {
		Iterator<ExcelSheetConfig> it = sheetConfigs.values().iterator();
		if (it.hasNext()) {
			return (ExcelSheetConfig) it.next();
		}
		return null;// (ExcelSheetConfig)sheetConfigs.get(sheetName);
	}

	// 设置sheet配置
	public void putExcelSheetConfig(String sheetName, ExcelSheetConfig sheetConfig) {
		sheetConfigs.put(sheetName, sheetConfig);
	}

	public String getConnectionSuperviseClass() {
		return connectionSuperviseClass;
	}

	public void setConnectionSuperviseClass(String connectionSuperviseClass) {
		this.connectionSuperviseClass = connectionSuperviseClass;
	}

}