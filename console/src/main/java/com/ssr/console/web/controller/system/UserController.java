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
import com.ssr.base.model.system.PrvUser;
import com.ssr.base.model.system.PrvUserRole;
import com.ssr.base.service.system.RoleService;
import com.ssr.base.service.system.UserService;
import com.ssr.base.util.AjaxSupport;
import com.ssr.base.util.annotation.Right;
import com.ssr.base.util.security.Cryption;
import com.ssr.base.web.controller.BaseController;

@Controller
@RequestMapping("/system")
public class UserController extends BaseController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	
	@Right(id = "SSR_C_SYS_SYSUSERLIST_PAGE", name = "用户管理", order = 1, 
			isMenu = true, moduleId = "SSR_C_SYS", moduleName = "系统管理", moduleOrder = 1)
	@RequestMapping(value = "/sysUserList", method = RequestMethod.GET)
	public String sysUserList(HttpServletRequest req){
		return "system/sysUserList";
	}
	
	@Right(id = "SSR_C_SYS_ADDSYSUSER_PAGE", name = "新增用户页面", 
			moduleId = "SSR_C_SYS", moduleName = "系统管理", moduleOrder = 1)
	@RequestMapping(value = "/addSysUser", method = RequestMethod.GET)
	public ModelAndView addSysUser(HttpServletRequest req){
		ModelAndView mv = new ModelAndView("system/addSysUser");
		List<PrvRole> roles = roleService.queryAllRole();
		mv.addObject("roles", roles);
		return mv;
	}
	
	@Right(id = "SSR_C_SYS_ADDSYSUSER_POST", name = "新增用户", 
			moduleId = "SSR_C_SYS", moduleName = "系统管理", moduleOrder = 1)
	@RequestMapping(value = "/addSysUser", method = RequestMethod.POST)
	@ResponseBody
	public Object addSysUser(PrvUser user, int roleId, int customerId) throws Exception{
		user.setCreateTime(new Date());
		user.setLoginPassword(Cryption.enCrytor(user.getLoginPassword()));
		user.setPwdChange("F");
		userService.saveUserAndUserRole(user, true, roleId, customerId);
		AjaxSupport<PrvUser> re = new AjaxSupport<PrvUser>();
		re.setModel(user);
		return re;
	}
	
	@Right(id = "SSR_C_SYS_UPDATESYSUSER_PAGE", name = "更新用户页面",
			moduleId = "SSR_C_SYS", moduleName = "系统管理", moduleOrder = 1)
	@RequestMapping(value = "/updateSysUser/{id}", method = RequestMethod.GET)
	public ModelAndView updateSysUser(@PathVariable int id){
		PrvUser user = userService.queryUserById(id);
		ModelAndView mv = new ModelAndView("system/updateSysUser");
		mv.addObject("user", user);
		List<PrvRole> roles = roleService.queryAllRole();
		mv.addObject("roles", roles);
		
		PrvUserRole ur = new PrvUserRole();
		ur.setUserId(id);
		ur = userService.queryUserRole(ur);
		if(ur != null){
			mv.addObject("roleId", ur.getRoleId());
		}
		else{
			mv.addObject("roleId", "");
		}
		
		return mv;
	}
	
	@Right(id = "SSR_C_SYS_UPDATESYSUSER_POST", name = "更新用户", 
			moduleId = "SSR_C_SYS", moduleName = "系统管理", moduleOrder = 1)
	@RequestMapping(value = "/updateSysUser", method = RequestMethod.POST)
	@ResponseBody
	public Object updateSysUser(PrvUser user, int roleId, int customerId) throws Exception{
		user.setLoginPassword(Cryption.enCrytor(user.getLoginPassword()));
		userService.saveUserAndUserRole(user, true, roleId, customerId);
		AjaxSupport<PrvUser> re = new AjaxSupport<PrvUser>();
		re.setModel(user);
		return re;
	}
	
	@Right(id = "SSR_C_SYS_SYSUSERLIST_POST", name = "用户管理列表", 
			moduleId = "SSR_C_SYS", moduleName = "系统管理", moduleOrder = 1)
	@RequestMapping(value = "/sysUserList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> querySysUserList(HttpServletRequest req){

		int page = 1;
		int pageSize = 10;
		if(req.getParameter("pageNo") != null){
			page = Integer.valueOf(req.getParameter("pageNo"));
		}
		if(req.getParameter("limit") != null){
			pageSize = Integer.valueOf(req.getParameter("limit"));
		}
		debug("Controller分页日志:page=" + page + "pageSize=" + pageSize);
		return pageToJson(new PageInfo<PrvUser>(userService.queryUserByMap(
				getRequestParameterAsMap(req), page, pageSize)));
	}
}
