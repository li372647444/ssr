package com.ssr.base.service.dynamic;

import java.util.List;
import java.util.Map;

import com.ssr.base.model.dynamic.DynamicTableManage;
import com.ssr.base.service.BaseService;

public interface DynamicTableManageService extends BaseService{
	public DynamicTableManage queryDynamicTableManageById(Object id);
	public List<DynamicTableManage> queryDynamicTableManageByPage(DynamicTableManage dynamicTableManage, int page, int pageSize);
	public DynamicTableManage saveDynamicTableManage(DynamicTableManage dynamicTableManage);
	public DynamicTableManage queryByModel(DynamicTableManage dynamicTableManage);
	public List<DynamicTableManage> queryDynamicTableManageByModel(DynamicTableManage dynamicTableManage);
	public List<DynamicTableManage> queryDynamicTableManageByMap(Map<String, Object> requestParameterAsMap, int page,int pageSize);
	
	/**
	 * 修改 动态表 记录 （修改表记录）   （修改表名、修改注释）
	 * @param dynamicTableManage
	 * @return
	 */
	public DynamicTableManage updateDynamicTableManage(DynamicTableManage dynamicTableManage);
	
	/**
	 *  删除 动态表记录 （删除表记录） 删除表
	 * @param dynamicTableManage
	 */
	public void deleteDynamicTableManage(Integer dynamicTableManage);
	
	/**
	 * 根据 表名 查找表信息
	 * @param name
	 * @return
	 */
	public DynamicTableManage queryByTableName(String tableName);
}
