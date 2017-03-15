package com.ssr.console.mapper.dynamic;

import java.util.List;
import java.util.Map;

import com.ssr.console.model.dynamic.DynamicTableManage;
import tk.mybatis.mapper.common.Mapper;

public interface DynamicTableManageMapper extends Mapper<DynamicTableManage> {

	public List<DynamicTableManage> selectDynamicTableManageByMap(Map<String, Object> paraMap);
	
	public void createNewDynamicTable(Map<String, Object> paraMap);
}