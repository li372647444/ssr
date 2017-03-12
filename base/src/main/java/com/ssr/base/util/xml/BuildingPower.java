package com.ssr.base.util.xml;

import java.util.List;
import java.util.Map;

public class BuildingPower {
	
	private String building;
	
	private String gateway;
	
	private String type;
	
	private String sequence;
	
	private String md5;
	
	private String result;
	
	private String period;
	
	private String time;
	
	private String parser;
	
	private String total;
	
	private String current;
	
	private List<Map<String, String>> meters;

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getParser() {
		return parser;
	}

	public void setParser(String parser) {
		this.parser = parser;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getCurrent() {
		return current;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	public List<Map<String, String>> getMeters() {
		return meters;
	}

	public void setMeters(List<Map<String, String>> meters) {
		this.meters = meters;
	}
	
}
