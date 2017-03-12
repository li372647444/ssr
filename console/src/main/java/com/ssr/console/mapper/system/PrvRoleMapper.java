package com.ssr.console.mapper.system;

import java.util.List;
import java.util.Map;

import com.ssr.console.model.system.PrvRole;

import tk.mybatis.mapper.common.Mapper;

public interface PrvRoleMapper extends Mapper<PrvRole> {
	public List<PrvRole> selectRoleByMap(Map<String, Object> paraMap);
}