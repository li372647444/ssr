package com.ssr.console.service.system;

import java.util.List;
import java.util.Map;

import com.ssr.base.service.BaseService;
import com.ssr.console.model.system.PrvUser;
import com.ssr.console.model.system.PrvUserRole;

public interface UserService extends BaseService{
	public PrvUser queryUserById(Object id);
	public List<PrvUser> queryUserByPage(PrvUser user, int page, int pageSize);
	public int saveUser(PrvUser user, boolean saveSelective);
	public List<PrvUser> queryUserByMap(Map<String, Object> paraMap, int page, int pageSize);
	public PrvUser queryUserByLogin(PrvUser user);
	public void saveUserRole(PrvUserRole userRole);
	public PrvUserRole queryUserRole(PrvUserRole userRole);
	public void saveUserAndUserRole(PrvUser user, boolean saveSelective, int roleId, int customerId);
	public PrvUser queryByModel(PrvUser user);
}
