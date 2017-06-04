package com.ssr.base.mapper.dynamic;

import java.util.List;
import java.util.Map;

import com.ssr.base.model.dynamic.DynamicManage;
import com.ssr.base.model.dynamic.DynamicTableManage;

import tk.mybatis.mapper.common.Mapper;

public interface DynamicTableManageMapper extends Mapper<DynamicTableManage> {

	public List<DynamicTableManage> selectDynamicTableManageByMap(Map<String, Object> paraMap);
	
	public void executeDDLSql(Map<String, Object> paraMap);

	public List<Map<String, Object>> executeDMLSql(Map<String, Object> paraMap);

	/**
	 * 新增 (插入)
	 * @param dm
	 * @return
	 */
	public int executeInsertSql(DynamicManage dm);

	/**
	 * 查询
	 * @param dm
	 * @return
	 */
	public List<Map<String, Object>> executeQuerySql(DynamicManage dm);

	/**
	 * 修改
	 * @param dm
	 */
	public void executeUpdateSql(DynamicManage dm);
}