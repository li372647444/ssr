package com.ssr.console.web.controller.system;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.ssr.base.model.system.PrvRole;
import com.ssr.base.model.system.PrvRoleFunction;
import com.ssr.base.model.system.PrvUser;
import com.ssr.base.service.system.RoleService;
import com.ssr.base.util.AjaxSupport;
import com.ssr.base.util.SystemConstants;
import com.ssr.base.util.annotation.Right;
import com.ssr.base.web.controller.BaseController;

@Controller
@RequestMapping("/system")
public class RoleController extends BaseController {
	
	@Autowired
	private RoleService roleService;
	
	@Right(id = "SSR_C_SYS_SYSROLELIST_PAGE", name = "角色管理", order = 2, 
			isMenu = true, moduleId = "SSR_C_SYS", moduleName = "系统管理", moduleOrder = 1)
	@RequestMapping(value = "/sysRoleList", method = RequestMethod.GET)
	public String sysRoleList(HttpServletRequest req){
		return "system/sysRoleList";
	}
	
	@Right(id = "SSR_C_SYS_ADDSYSROLE_PAGE", name = "新增角色页面", 
			moduleId = "SSR_C_SYS", moduleName = "系统管理", moduleOrder = 1)
	@RequestMapping(value = "/addSysRole", method = RequestMethod.GET)
	public String addSysRole(HttpServletRequest req){
		return "system/addSysRole";
	}
	
	@Right(id = "SSR_C_SYS_ADDSYSROLE_POST", name = "新增角色", 
			moduleId = "SSR_C_SYS", moduleName = "系统管理", moduleOrder = 1)
	@RequestMapping(value = "/addSysRole", method = RequestMethod.POST)
	@ResponseBody
	public Object addSysRole(PrvRole role, HttpServletRequest req) throws Exception{
		role.setCreateTime(new Date());
		role.setCreateUserId(((PrvUser)req.getSession().getAttribute(SystemConstants.SESSION_USER)).getId());
		roleService.saveRole(role, true);
		AjaxSupport<PrvRole> re = new AjaxSupport<PrvRole>();
		re.setModel(role);
		return re;
	}
	
	@Right(id = "SSR_C_SYS_UPDATESYSROLE_PAGE", name = "更新角色页面",
			moduleId = "SSR_C_SYS", moduleName = "系统管理", moduleOrder = 1)
	@RequestMapping(value = "/updateSysRole/{id}", method = RequestMethod.GET)
	public ModelAndView updateSysRole(@PathVariable int id){
		PrvRole role = roleService.queryRoleById(id);
		ModelAndView mv = new ModelAndView("system/updateSysRole");
		mv.addObject("role", role);
		return mv;
	}
	
	@Right(id = "SSR_C_SYS_UPDATESYSROLE_POST", name = "更新角色", 
			moduleId = "SSR_C_SYS", moduleName = "系统管理", moduleOrder = 1)
	@RequestMapping(value = "/updateSysRole", method = RequestMethod.POST)
	@ResponseBody
	public Object updateSysRole(PrvRole role) throws Exception{
		roleService.saveRole(role, true);
		AjaxSupport<PrvRole> re = new AjaxSupport<PrvRole>();
		re.setModel(role);
		return re;
	}
	
	@Right(id = "SSR_C_SYS_SETROLEMENU_PAGE", name = "设置角色菜单权限页面",
			moduleId = "SSR_C_SYS", moduleName = "系统管理", moduleOrder = 1)
	@RequestMapping(value = "/setRoleMenu/{id}", method = RequestMethod.GET)
	public ModelAndView setRoleMenu(@PathVariable int id){
		PrvRole role = roleService.queryRoleById(id);
		ModelAndView mv = new ModelAndView("system/setRoleMenu");
		mv.addObject("role", role);
		mv.addObject("moduleMap", SystemConstants.MODULE_MAP);
		List<PrvRoleFunction> list = roleService.queryFunctionByRoleId(id);
		StringBuilder sb = new StringBuilder();
		for(PrvRoleFunction rf : list){
			sb.append(rf.getFunctionId());
		}
		mv.addObject("rights", sb.toString());
		return mv;
	}
	
	@Right(id = "SSR_C_SYS_SETROLEMENU_POST", name = "设置角色菜单权限", 
			moduleId = "SSR_C_SYS", moduleName = "系统管理", moduleOrder = 1)
	@RequestMapping(value = "/setRoleMenu", method = RequestMethod.POST)
	@ResponseBody
	public Object setRoleMenu(PrvRole role, HttpServletRequest req){
		String[] values = req.getParameterValues("rightIds");
		roleService.saveRoleFunction(role.getId(), true, values);
		AjaxSupport<PrvRole> re = new AjaxSupport<PrvRole>();
		re.setModel(role);
		return re;
	}
	
	@Right(id = "SSR_C_SYS_SETROLEFUNCTION_PAGE", name = "设置角色功能权限页面",
			moduleId = "SSR_C_SYS", moduleName = "系统管理", moduleOrder = 1)
	@RequestMapping(value = "/setRoleFunction/{id}", method = RequestMethod.GET)
	public ModelAndView setRoleFunction(@PathVariable int id){
		PrvRole role = roleService.queryRoleById(id);
		ModelAndView mv = new ModelAndView("system/setRoleFunction");
		mv.addObject("role", role);
		mv.addObject("moduleMap", SystemConstants.MODULE_MAP);
		List<PrvRoleFunction> list = roleService.queryFunctionByRoleId(id);
		StringBuilder sb = new StringBuilder();
		for(PrvRoleFunction rf : list){
			sb.append(rf.getFunctionId());
		}
		mv.addObject("rights", sb.toString());
		return mv;
	}
	
	@Right(id = "SSR_C_SYS_SETROLEFUNCTION_POST", name = "设置角色功能权限", 
			moduleId = "SSR_C_SYS", moduleName = "系统管理", moduleOrder = 1)
	@RequestMapping(value = "/setRoleFunction", method = RequestMethod.POST)
	@ResponseBody
	public Object setRoleFunction(PrvRole role, HttpServletRequest req){
		String[] values = req.getParameterValues("rightIds");
		roleService.saveRoleFunction(role.getId(), false, values);
		AjaxSupport<PrvRole> re = new AjaxSupport<PrvRole>();
		re.setModel(role);
		return re;
	}
	
	@Right(id = "SSR_C_SYS_SYSROLELIST_POST", name = "角色管理列表", 
			moduleId = "SSR_C_SYS", moduleName = "系统管理", moduleOrder = 1)
	@RequestMapping(value = "/sysRoleList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> querySysRoleList(HttpServletRequest req){

		int page = 1;
		int pageSize = 10;
		if(req.getParameter("pageNo") != null){
			page = Integer.valueOf(req.getParameter("pageNo"));
		}
		if(req.getParameter("limit") != null){
			pageSize = Integer.valueOf(req.getParameter("limit"));
		}
		debug("Controller分页日志:page=" + page + "pageSize=" + pageSize);
		return pageToJson(new PageInfo<PrvRole>(roleService.queryRoleByMap(
				getRequestParameterAsMap(req), page, pageSize)));
	}
}
