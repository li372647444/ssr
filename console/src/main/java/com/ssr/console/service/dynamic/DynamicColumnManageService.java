package com.ssr.console.service.dynamic;

import java.util.List;
import java.util.Map;

import com.ssr.base.service.BaseService;
import com.ssr.console.model.dynamic.DynamicColumnManage;

public interface DynamicColumnManageService extends BaseService{
	public DynamicColumnManage queryDynamicColumnManageById(Object id);
	public List<DynamicColumnManage> queryDynamicColumnManageByPage(DynamicColumnManage dynamicColumnManage, int page, int pageSize);
	public DynamicColumnManage queryByModel(DynamicColumnManage dynamicColumnManage);
	public List<DynamicColumnManage> queryDynamicColumnManageByMap(Map<String, Object> requestParameterAsMap, int page,int pageSize);
	
	public DynamicColumnManage updateDynamicColumnManage(DynamicColumnManage dynamicColumnManage);
	public DynamicColumnManage saveDynamicColumnManage(DynamicColumnManage dynamicColumnManage);
	public void deleteDynamicColumnManage(Integer id);
	
}
