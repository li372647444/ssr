package com.ssr.console.service.dynamic;

import java.util.List;
import java.util.Map;

import com.ssr.base.service.BaseService;
import com.ssr.console.model.dynamic.DynamicTableManage;

public interface DynamicTableManageService extends BaseService{
	public DynamicTableManage queryDynamicTableManageById(Object id);
	public List<DynamicTableManage> queryDynamicTableManageByPage(DynamicTableManage dynamicTableManage, int page, int pageSize);
	public DynamicTableManage saveDynamicTableManage(DynamicTableManage dynamicTableManage, boolean saveSelective);
	public DynamicTableManage queryByModel(DynamicTableManage dynamicTableManage);
	public List<DynamicTableManage> queryDynamicTableManageByMap(Map<String, Object> requestParameterAsMap, int page,int pageSize);
}
