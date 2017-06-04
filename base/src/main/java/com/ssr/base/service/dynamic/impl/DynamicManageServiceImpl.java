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
import com.ssr.base.util.constantenum.MysqlColumnTypeEnum;
import com.ssr.base.util.constantenum.QueryConditionSymbolEnum;

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
		Map<String,Object> returnMap = new HashMap<String,Object>();
		DynamicTableManage table = selectByTableName(tableName);
		List<DynamicColumnManage> colunmns = selectByTableId(table.getId());
		//构造 动态 SQL 生成 对象
		Map<String,Object> updateValues = new HashMap<String, Object>();
		for(DynamicColumnManage c:colunmns){
			String key = c.getColumnName();
			if(c.getIsAllowUpdate() && reMap.containsKey(key)){
				if((c.getTypeForMysql().equals(MysqlColumnTypeEnum.DATE.getName())
					|| c.getTypeForMysql().equals(MysqlColumnTypeEnum.DATETIME.getName())
					)
					&& "".equals(reMap.get(key))){
					updateValues.put(key, null);
				} else {
					updateValues.put(key, reMap.get(key));
				}
			}
		}
		if(updateValues!=null && updateValues.size()>0){
			DynamicManage dm = new DynamicManage();
			dm.setTableName(tableName);
			dm.setId(id);
			dm.setUpdateValues(updateValues);
			dynamicTableManageMapper.executeUpdateSql(dm);
			returnMap.put("id", dm.getId());
		} else {
			if(isDebugEnabled()){
				debug("updateColunmnNames is null ");
			}
		}
		return returnMap;
	}

	@Override
	public List<Map<String, Object>> queryByMap(String tableName,Map<String, Object> reMap,int page,int pageSize) {
		DynamicTableManage table = selectByTableName(tableName);
		List<DynamicColumnManage> colunmns = selectByTableId(table.getId());
		//构造 动态 SQL 生成 对象
		DynamicManage dm = new DynamicManage();
		dm.setTableName(tableName);
		List<QueryCondition> queryConditions = new ArrayList<QueryCondition>();
		for(DynamicColumnManage c:colunmns){
			String key = c.getColumnName();
			if(reMap.containsKey(key)){
				queryConditions.add(new QueryCondition(key, c.getQueryConditionSymbol(), reMap.get(key)));
			}
		}
		dm.setQueryConditions(queryConditions);
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

	@Override
	public Map<String, Object> queryById(String tableName,int id) {
		//构造 动态 SQL 生成 对象
		DynamicManage dm = new DynamicManage();
		dm.setTableName(tableName);
		List<QueryCondition> queryConditions = new ArrayList<QueryCondition>();
		queryConditions.add(new QueryCondition("id",QueryConditionSymbolEnum.EQUAL.getName(), id));
		dm.setQueryConditions(queryConditions);
		return dynamicTableManageMapper.executeQuerySql(dm).get(0);
	}
	
}
