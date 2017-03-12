package com.ssr.console.mapper.system;

import java.util.List;
import java.util.Map;

import com.ssr.console.model.system.PrvUser;

import tk.mybatis.mapper.common.Mapper;

public interface PrvUserMapper extends Mapper<PrvUser> {
	public List<PrvUser> selectUserByMap(Map<String, Object> paraMap);
}