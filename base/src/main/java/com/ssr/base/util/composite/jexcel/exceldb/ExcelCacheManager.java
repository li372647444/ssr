package com.ssr.base.util.composite.jexcel.exceldb;

import java.util.HashMap;
import java.util.Map;

/**
 * 整个类没有做为null判断，需要在程序里依次调用newCache和newRecord，
 * 这样有助于帮助检查配置是否正确。如果外层配置调用里层则会抛出空指针异常。
 * 
 */
public class ExcelCacheManager {
	// 该cache只保存当前sheet的数据。
	// 当前sheet的location数据
	private Map<String, Object> locations = null;
	// 存储每条数据
	private Map<String, Object> records = null;

	/**
	 * 每次往新记录中填数据之前，必须先new。
	 * 
	 * @param id
	 *            String 记录id
	 */
	public void newRecord(String id) {
		records.put(id, new HashMap<String, Object>());
	}

	@SuppressWarnings("unchecked")
	public void putField(String id, String fieldName, Object value) {
		Map<String, Object> r = (HashMap<String, Object>) records.get(id);
		r.put(fieldName, value);
	}

	@SuppressWarnings("unchecked")
	public Object getFields(String id, String fieldName) throws Exception {
		Map<String, Object> r = (HashMap<String, Object>) records.get(id);
		if (r == null) {
			throw new Exception("没有id为[" + id + "]的form或者loop的缓存，请检查id是否配置错误！");
		}
		return r.get(fieldName);
	}

	public void putLocation(String id, Object value) {
		locations.put(id, value);
	}

	public Object getLocation(String id) {
		return locations.get(id);
	}

	public void newSheetCache() {
		locations = new HashMap<String, Object>();
		records = new HashMap<String, Object>();
	}

	public void clearCache() {
		locations = null;
		records = null;
	}
}