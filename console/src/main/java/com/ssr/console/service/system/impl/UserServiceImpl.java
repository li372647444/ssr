package com.ssr.console.service.system.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.ssr.console.mapper.system.PrvUserMapper;
import com.ssr.console.mapper.system.PrvUserRoleMapper;
import com.ssr.console.model.system.PrvUser;
import com.ssr.console.model.system.PrvUserRole;
import com.ssr.console.service.system.UserService;
import com.ssr.base.service.impl.BaseServiceImpl;

import tk.mybatis.mapper.entity.Example;

@Service
public class UserServiceImpl extends BaseServiceImpl implements UserService {

	@Autowired
	private PrvUserMapper prvUserMapper;
	@Autowired
	private PrvUserRoleMapper prvUserRoleMapper;
	
	public PrvUser queryUserById(Object id) {
		return prvUserMapper.selectByPrimaryKey(id);
	}

	public int saveUser(PrvUser user, boolean saveSelective) {
		int result;
		if(saveSelective){
			if(user.getId() != null){
				result = prvUserMapper.updateByPrimaryKeySelective(user);
			}
			else{
				result = prvUserMapper.insertSelective(user);
			}
		}
		else{
			if(user.getId() != null){
				result = prvUserMapper.updateByPrimaryKey(user);
			}
			else{
				result = prvUserMapper.insert(user);
			}
		}
		return result;
	}
	
	public List<PrvUser> queryUserByPage(PrvUser user, int page, int pageSize){
		PageHelper.startPage(page, pageSize);
		debug("Service分页日志:page=" + page + "pageSize=" + pageSize);
		return prvUserMapper.select(user);
	}

	public List<PrvUser> queryUserByMap(Map<String, Object> paraMap, int page, int pageSize){
		PageHelper.startPage(page, pageSize);
		debug("Service分页日志:page=" + page + "pageSize=" + pageSize);
		return prvUserMapper.selectUserByMap(paraMap);
	}

	public PrvUser queryUserByLogin(PrvUser user){
		return prvUserMapper.selectOne(user);
	}
	
	public void saveUserRole(PrvUserRole userRole){
		prvUserRoleMapper.insert(userRole);
	}
	
	public PrvUserRole queryUserRole(PrvUserRole userRole){
		return prvUserRoleMapper.selectOne(userRole);
	}
	
	public void saveUserAndUserRole(PrvUser user, boolean saveSelective, int roleId, int customerId){
		saveUser(user, saveSelective);
		
		PrvUserRole ur = new PrvUserRole();
		ur.setUserId(user.getId());
		PrvUserRole urT = prvUserRoleMapper.selectOne(ur);
		if(urT == null){
			ur.setRoleId(roleId);
			prvUserRoleMapper.insertSelective(ur);
		}
		else{
			urT.setRoleId(roleId);
			Example exT = new Example(PrvUserRole.class);
			exT.createCriteria().andEqualTo("userId", user.getId());
			prvUserRoleMapper.updateByExampleSelective(urT, exT);
		}
		
	}
	
	public PrvUser queryByModel(PrvUser user){
		return prvUserMapper.selectOne(user);
	}
	
}
