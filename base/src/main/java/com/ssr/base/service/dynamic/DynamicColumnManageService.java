package com.ssr.base.service.dynamic;

import java.util.List;
import java.util.Map;

import com.ssr.base.model.dynamic.DynamicColumnManage;
import com.ssr.base.service.BaseService;

public interface DynamicColumnManageService extends BaseService{
	public DynamicColumnManage queryDynamicColumnManageById(Object id);
	public List<DynamicColumnManage> queryDynamicColumnManageByPage(DynamicColumnManage dynamicColumnManage, int page, int pageSize);
	public DynamicColumnManage queryByModel(DynamicColumnManage dynamicColumnManage);
	public List<DynamicColumnManage> queryDynamicColumnManageByMap(Map<String, Object> requestParameterAsMap, int page,int pageSize);
	
	public DynamicColumnManage updateDynamicColumnManage(DynamicColumnManage dynamicColumnManage);
	public DynamicColumnManage saveDynamicColumnManage(DynamicColumnManage dynamicColumnManage);
	public void deleteDynamicColumnManage(Integer id);
	public List<DynamicColumnManage> queryDynamicColumnManageByTableId(Integer tableId);
	
	/**
	 * 查找 新增列 默认字段序号
	 * @param tableId
	 * @return
	 */
	public int queryNextColumnFieldSerialNumber(int tableId);
	
}
