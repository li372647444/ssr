package com.ssr.base.service.dynamic;

import java.util.List;
import java.util.Map;

import com.ssr.base.service.BaseService;

public interface DynamicManageService extends BaseService{

	public Map<String, Object> saveDynamicManage(String tableName,Map<String, Object> reMap);

	public Map<String, Object> updateDynamicManage(String dynamicTableName,Integer id, Map<String, Object> reMap);

	public List<Map<String, Object>> queryByMap(String tableName,Map<String, Object> reMap,int page,int pageSize);

	public boolean deleteById(String dynamicTableName, int id);
}
