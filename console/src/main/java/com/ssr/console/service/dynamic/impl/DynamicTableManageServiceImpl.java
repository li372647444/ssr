package com.ssr.console.service.dynamic.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.ssr.base.service.impl.BaseServiceImpl;
import com.ssr.console.mapper.dynamic.DynamicTableManageMapper;
import com.ssr.console.model.dynamic.DynamicTableManage;
import com.ssr.console.service.dynamic.DynamicTableManageService;

@Service
public class DynamicTableManageServiceImpl extends BaseServiceImpl implements DynamicTableManageService {

	@Autowired
	private DynamicTableManageMapper dynamicTableManageMapper;
	
	public DynamicTableManage queryDynamicTableManageById(Object id) {
		return dynamicTableManageMapper.selectByPrimaryKey(id);
	}
	
	public int saveDynamicTableManage(DynamicTableManage dynamicTableManage, boolean saveSelective) {
		int result;
		if(saveSelective){
			if(dynamicTableManage.getId() != null){
				result = dynamicTableManageMapper.updateByPrimaryKeySelective(dynamicTableManage);
			}
			else{
				result = dynamicTableManageMapper.insertSelective(dynamicTableManage);
			}
		}
		else{
			if(dynamicTableManage.getId() != null){
				result = dynamicTableManageMapper.updateByPrimaryKey(dynamicTableManage);
			}
			else{
				result = dynamicTableManageMapper.insert(dynamicTableManage);
			}
		}
		return result;
	}
	
	public List<DynamicTableManage> queryDynamicTableManageByPage(DynamicTableManage dynamicTableManage, int page, int pageSize){
		PageHelper.startPage(page, pageSize);
		debug("Service分页日志:page=" + page + "pageSize=" + pageSize);
		return dynamicTableManageMapper.select(dynamicTableManage);
	}

	public List<DynamicTableManage> queryDynamicTableManageByMap(Map<String, Object> paraMap, int page, int pageSize){
		PageHelper.startPage(page, pageSize);
		debug("Service分页日志:page=" + page + "pageSize=" + pageSize);
		return dynamicTableManageMapper.selectDynamicTableManageByMap(paraMap);
	}

	public DynamicTableManage queryByModel(DynamicTableManage dynamicTableManage){
		return dynamicTableManageMapper.selectOne(dynamicTableManage);
	}
}
