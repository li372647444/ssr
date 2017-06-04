package com.ssr.base.service.system.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.ssr.base.mapper.system.PrvRoleFunctionMapper;
import com.ssr.base.mapper.system.PrvRoleMapper;
import com.ssr.base.model.system.Function;
import com.ssr.base.model.system.PrvRole;
import com.ssr.base.model.system.PrvRoleFunction;
import com.ssr.base.service.impl.BaseServiceImpl;
import com.ssr.base.service.system.RoleService;
import com.ssr.base.util.SystemConstants;

import tk.mybatis.mapper.entity.Example;

@Service
public class RoleServiceImpl extends BaseServiceImpl implements RoleService {
	
	@Autowired
	private PrvRoleMapper prvRoleMapper;
	@Autowired
	private PrvRoleFunctionMapper prvRoleFunctionMapper;
	
	public PrvRole queryRoleById(Object id) {
		return prvRoleMapper.selectByPrimaryKey(id);
	}
	
	public int saveRole(PrvRole role, boolean saveSelective){
		int result;
		if(saveSelective){
			if(role.getId() != null){
				result = prvRoleMapper.updateByPrimaryKeySelective(role);
			}
			else{
				result = prvRoleMapper.insertSelective(role);
			}
		}
		else{
			if(role.getId() != null){
				result = prvRoleMapper.updateByPrimaryKey(role);
			}
			else{
				result = prvRoleMapper.insert(role);
			}
		}
		return result;
	}
	
	public List<PrvRole> queryRoleByMap(Map<String, Object> paraMap, int page, int pageSize){
		PageHelper.startPage(page, pageSize);
		debug("Service分页日志:page=" + page + "pageSize=" + pageSize);
		return prvRoleMapper.selectRoleByMap(paraMap);
	}
	
	public List<PrvRole> queryAllRole(){
		PrvRole role = new PrvRole();
		role.setState("QY");
		return prvRoleMapper.select(role);
	}
	
	public List<PrvRoleFunction> queryFunctionByRoleId(int roleId){
		PrvRoleFunction rf = new PrvRoleFunction();
		rf.setRoleId(roleId);
		return prvRoleFunctionMapper.select(rf);
	}
	
	public void saveRoleFunction(int roleId, boolean isMenu, String function[]){
		Set<String> rightIds = new HashSet<String>();
		PrvRoleFunction funTemp = new PrvRoleFunction();
		funTemp.setRoleId(roleId);
		List<PrvRoleFunction> list = prvRoleFunctionMapper.select(funTemp);
		Function fc = null;
		for(PrvRoleFunction rf : list){
			fc = SystemConstants.FUNCTION_MAP.get(rf.getFunctionId());
			if(fc == null){
				continue;
			}
			if(isMenu){
				if(!fc.getIsMenu()){
					rightIds.add(rf.getFunctionId());
				}
			}
			else{
				if(fc.getIsMenu()){
					rightIds.add(rf.getFunctionId());
				}
			}
		}
		if(function != null){
			for(String f : function){
				rightIds.add(f);
			}
		}
		
		Example ex = new Example(PrvRoleFunction.class);
		ex.createCriteria().andEqualTo("roleId", roleId);
		prvRoleFunctionMapper.deleteByExample(ex);
		
		PrvRoleFunction fun = new PrvRoleFunction();
		fun.setRoleId(roleId);
		for(String s : rightIds){
			fun.setFunctionId(s);
			prvRoleFunctionMapper.insertSelective(fun);
		}
	}
}
