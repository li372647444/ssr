package com.ssr.base.util.composite.jexcel.exceldb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExcelSheetConfig {
	private String sheetName;
	private Map<String, LocationConfig> locations = new HashMap<String, LocationConfig>();
	private List<ExcelFLConfig> ExcelFLConfigs = new ArrayList<ExcelFLConfig>();

	public Iterator<LocationConfig> getLocationIterator() {
		return locations.values().iterator();
	}

	public void putLocation(String id, LocationConfig lc) {
		locations.put(id, lc);
	}

	public Iterator<ExcelFLConfig> getFLRootIterator() {
		return ExcelFLConfigs.iterator();
	}

	public void addFLConfig(ExcelFLConfig flc) {
		ExcelFLConfigs.add(flc);
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

}