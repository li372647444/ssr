package com.ssr.base.util.composite.jexcel.exceldb;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 根据名称取代码表的值
 * 
 */
public class InverCodeListManager {
	private Map<String, Object> codeGroup = new HashMap<String, Object>();

	@SuppressWarnings("unchecked")
	public String getCodeValue(javax.servlet.ServletContext context, String codeType, String text) throws Exception {

		Map<String, Object> codes = (Map<String, Object>) codeGroup.get(codeType);
		if (codes == null) {
			Map<String, Object> map = (Map<String, Object>) context.getAttribute(codeType);
			// 反转代码
			codes = invertCode(map, codeType);
			codeGroup.put(codeType, codes);
		}

		Object value = codes.get(text.trim());
		if (value == null) {
			throw new Exception("代码类型[" + codeType + "]对应的文本[" + text + "]不存在，无法转译！");
		}

		return value.toString();
	}

	/**
	 * 反转代码表，根据文本取代码
	 * 
	 * @param map
	 *            Map
	 * @param codeType
	 *            String
	 * @throws Exception
	 */
	private Map<String, Object> invertCode(Map<String, Object> map, String codeType) throws Exception {
		if (map == null) {
			throw new Exception("代码类型[" + codeType + "]对应的代码表不存在，无法转译！");
		}

		Map<String, Object> invertedMap = new HashMap<String, Object>();

		for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
			Object key = it.next();
			String text = (String) map.get(key);
			invertedMap.put(text, key);
		}

		return invertedMap;
	}

	public void clearAll() {
		codeGroup = null;
	}
}