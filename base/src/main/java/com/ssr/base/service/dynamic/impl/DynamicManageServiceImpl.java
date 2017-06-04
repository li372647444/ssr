package com.ssr.base.service.dynamic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.ssr.base.exception.UserException;
import com.ssr.base.mapper.dynamic.DynamicColumnManageMapper;
import com.ssr.base.mapper.dynamic.DynamicTableManageMapper;
import com.ssr.base.model.dynamic.DynamicColumnManage;
import com.ssr.base.model.dynamic.DynamicManage;
import com.ssr.base.model.dynamic.DynamicTableManage;
import com.ssr.base.model.dynamic.QueryCondition;
import com.ssr.base.service.dynamic.DynamicManageService;
import com.ssr.base.service.impl.BaseServiceImpl;

@Service
public class DynamicManageServiceImpl extends BaseServiceImpl implements DynamicManageService {

	@Autowired
	private DynamicTableManageMapper dynamicTableManageMapper;
	@Autowired
	private DynamicColumnManageMapper dynamicColumnManageMapper;
	
	/**
	 * 根据表名查找表管理 记录
	 * @param tableName
	 * @return
	 */
	private DynamicTableManage selectByTableName(String tableName){
		DynamicTableManage query = new DynamicTableManage();
		query.setTableName(tableName);
		DynamicTableManage table = dynamicTableManageMapper.selectOne(query);
		if(table==null){
			throw new UserException("表记录不存在");
		}
		return table;
	}
	
	private List<DynamicColumnManage> selectByTableId(Integer tableId){
		DynamicColumnManage queryC = new DynamicColumnManage();
		queryC.setTableId(tableId);
		List<DynamicColumnManage> colunmns = dynamicColumnManageMapper.select(queryC);
		if(colunmns==null || colunmns.isEmpty()){
			throw new UserException("列记录不存在");
		}
		return colunmns;
	}
	
	@Override
	public Map<String, Object> saveDynamicManage(String tableName,Map<String, Object> reMap) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		DynamicTableManage table = selectByTableName(tableName);
		List<DynamicColumnManage> colunmns = selectByTableId(table.getId());
		//构造 动态 SQL 生成 对象
		DynamicManage dm = new DynamicManage();
		dm.setTableName(tableName);
		List<String> colunmnNames = new ArrayList<String>();
		List<Object> colunmnValues = new ArrayList<Object>();
		for(DynamicColumnManage c:colunmns){
			String key = c.getColumnName();
			if(reMap.containsKey(key)){
				colunmnNames.add(key);
				colunmnValues.add(reMap.get(key));
			}
		}
		dm.setColunmnNames(colunmnNames);
		dm.setColunmnValues(colunmnValues);
		if(colunmnNames.size()>0){
			dynamicTableManageMapper.executeInsertSql(dm);
			if(isDebugEnabled()){
				debug("=====>    insert id ="+dm.getId());
			}
			returnMap.put("id", dm.getId());
		} else {
			if(isDebugEnabled()){
				debug("insertColunmnNames is null ");
			}
		}
		return returnMap;
	}

	@Override
	public Map<String, Object> updateDynamicManage(String tableName,Integer id, Map<String, Object> reMap) {
		DynamicTableManage query = new DynamicTableManage();
		query.setTableName(tableName);
		DynamicTableManage table = dynamicTableManageMapper.selectOne(query);
		if(table==null){
			throw new UserException("表记录不存在");
		}
		DynamicColumnManage queryC = new DynamicColumnManage();
		queryC.setTableId(table.getId());
		List<DynamicColumnManage> colunmns = dynamicColumnManageMapper.select(queryC);
		if(colunmns==null || colunmns.isEmpty()){
			throw new UserException("列记录不存在");
		}
		StringBuilder updateSQL = new StringBuilder("UPDATE " + table.getTableName() +" SET ");
		for(DynamicColumnManage c:colunmns){
			String key = c.getColumnName();
			if(reMap.containsKey(key)){
				updateSQL.append(key+"="+reMap.get(key)+",");
			}
		}
		String whereSQL = " WHERE id = "+ id;
		String updateSQLStr = updateSQL.toString();
		updateSQLStr = updateSQLStr.substring(0,updateSQLStr.length()-1) + whereSQL;
		if(isDebugEnabled()){
			debug("updateSQL="+updateSQLStr);
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> queryByMap(String tableName,Map<String, Object> reMap,int page,int pageSize) {
		DynamicTableManage table = selectByTableName(tableName);
		List<DynamicColumnManage> colunmns = selectByTableId(table.getId());
		//构造 动态 SQL 生成 对象
		DynamicManage dm = new DynamicManage();
		dm.setTableName(tableName);
		List<String> colunmnNames = new ArrayList<String>();
		List<String> conditionSymbols = new ArrayList<String>();
		List<Object> colunmnValues = new ArrayList<Object>();
		List<QueryCondition> queryConditions = new ArrayList<QueryCondition>();
		for(DynamicColumnManage c:colunmns){
			String key = c.getColumnName();
			if(reMap.containsKey(key)){
				colunmnNames.add(key);
				conditionSymbols.add(c.getQueryConditionSymbol());
				colunmnValues.add(reMap.get(key));
				queryConditions.add(new QueryCondition(key, c.getQueryConditionSymbol(), reMap.get(key)));
			}
		}
		dm.setColunmnNames(colunmnNames);
		dm.setConditionSymbols(conditionSymbols);
		dm.setColunmnValues(colunmnValues);
		dm.setQueryConditions(queryConditions);
		//分页
		PageHelper.startPage(page, pageSize);//debug("Service分页日志:page=" + page + "pageSize=" + pageSize);
		return dynamicTableManageMapper.executeQuerySql(dm);
	}

	@Override
	public boolean deleteById(String tableName, int id) {
		DynamicTableManage query = new DynamicTableManage();
		query.setTableName(tableName);
		DynamicTableManage table = dynamicTableManageMapper.selectOne(query);
		if(table==null){
			throw new UserException("表记录不存在");
		}
		DynamicColumnManage queryC = new DynamicColumnManage();
		queryC.setTableId(table.getId());
		List<DynamicColumnManage> colunmns = dynamicColumnManageMapper.select(queryC);
		if(colunmns==null || colunmns.isEmpty()){
			throw new UserException("列记录不存在");
		}
		StringBuilder deleteSql = new StringBuilder("DELETE FROM ");
		deleteSql.append(tableName);
		deleteSql.append(" WHERE id=");
		deleteSql.append(id);
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("sql", deleteSql.toString());
		dynamicTableManageMapper.executeDMLSql(paraMap);
		return true;
	}
	
}
