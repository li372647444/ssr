package com.ssr.base.util;

import java.util.Map;

/**
 * Map工具类,根据key获取对应类型的值
 *
 * @author 
 */
public class Dto {
	
	public Map<String, ? extends Object> inMap;
	
	public Dto(Map<String, ? extends Object> map){
		inMap = map;
	}

	public Map<String, ? extends Object> getInMap() {
		return inMap;
	}

	public void setInMap(Map<String, ? extends Object> inMap) {
		this.inMap = inMap;
	}
	
	public String getValueAsString(String key){
		return (String)inMap.get(key);
	}
	
	public int getValueAsInt(String key){
		String s = (String)inMap.get(key);
		return Integer.parseInt(s);
	}
	
	public Long getValueAsLong(String key){
		String s = (String)inMap.get(key);
		return Long.parseLong(s);
	}
	
}
