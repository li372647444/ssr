package com.ssr.base.util.composite.jexcel.exceldb;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * 一个sheet的合并行列的缓存
 * 
 */
public class MarginCache {

	private Map<String, Integer> marginRows = new HashMap<String, Integer>();

	public MarginCache(HSSFSheet sheet) {
		for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
			CellRangeAddress r = sheet.getMergedRegion(i);
			// key:"起始行,起始列"，value:"终止行-终止列"
			marginRows.put(r.getFirstRow() + "," + r.getFirstColumn(), new Integer(r.getLastRow() - r.getFirstRow() + 1));
		}
	}

	public int getMarginRow(int row, short col) {
		Integer i = (Integer) marginRows.get(row + "," + col);
		if (i == null) {
			return -1;
		}
		return i.intValue();
	}
}