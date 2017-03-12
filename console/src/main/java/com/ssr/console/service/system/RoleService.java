package com.ssr.console.service.system;

import java.util.List;
import java.util.Map;

import com.ssr.base.service.BaseService;
import com.ssr.console.model.system.PrvRole;
import com.ssr.console.model.system.PrvRoleFunction;

public interface RoleService extends BaseService {
	public PrvRole queryRoleById(Object id);
	public int saveRole(PrvRole role, boolean saveSelective);
	public List<PrvRole> queryRoleByMap(Map<String, Object> paraMap, int page, int pageSize);
	public List<PrvRole> queryAllRole();
	public List<PrvRoleFunction> queryFunctionByRoleId(int roleId);
	public void saveRoleFunction(int roleId, boolean isMenu, String function[]);
}
