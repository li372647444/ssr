package com.ssr.base.model.system;

import java.io.Serializable;
import java.util.List;

public class Module implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	private String desc;
	private int order;
	private List<Function> funList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public List<Function> getFunList() {
		return funList;
	}

	public void setFunList(List<Function> funList) {
		this.funList = funList;
	}

}
