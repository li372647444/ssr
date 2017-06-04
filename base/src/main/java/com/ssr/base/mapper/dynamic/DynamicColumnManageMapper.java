package com.ssr.base.mapper.dynamic;

import java.util.List;
import java.util.Map;

import com.ssr.base.model.dynamic.DynamicColumnManage;

import tk.mybatis.mapper.common.Mapper;

public interface DynamicColumnManageMapper extends Mapper<DynamicColumnManage> {

	public List<DynamicColumnManage> selectDynamicColumnManageByMap(Map<String, Object> paraMap);

	public int selectNextColumnFieldSerialNumber(Map<String, Object> paraMap);
}