package com.ssr.console.mapper.dynamic;

import java.util.List;
import java.util.Map;

import com.ssr.console.model.dynamic.DynamicColumnManage;
import tk.mybatis.mapper.common.Mapper;

public interface DynamicColumnManageMapper extends Mapper<DynamicColumnManage> {

	public List<DynamicColumnManage> selectDynamicColumnManageByMap(Map<String, Object> paraMap);
}