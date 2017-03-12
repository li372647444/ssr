package com.ssr.console.web.controller.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ssr.base.model.Function;
import com.ssr.base.model.Module;
import com.ssr.base.redis.RedisUtil;
import com.ssr.base.util.AjaxSupport;
import com.ssr.base.util.CommonUtils;
import com.ssr.base.util.SystemConfig;
import com.ssr.base.util.SystemConstants;
import com.ssr.base.util.security.Cryption;
import com.ssr.base.web.controller.BaseController;
import com.ssr.console.model.system.PrvRoleFunction;
import com.ssr.console.model.system.PrvUser;
import com.ssr.console.model.system.PrvUserRole;
import com.ssr.console.service.system.RoleService;
import com.ssr.console.service.system.UserService;

/**
 * 登录controller
 *
 * @author PengLian
 */
@Controller
public class LoginController extends BaseController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	
	private String index = SystemConfig.getValueByKey("portal.index", "base/index");
	
	private String authLoginUser = SystemConfig.getValueByKey("auth.loginUser", "power_nhjk");
	
	private String authIndex = SystemConfig.getValueByKey("auth.portalIndex", "power_energy/energyPortal");

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String welcome() {
        return "redirect:/index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request) {
    	//增加授权登录的首页判断
    	ModelAndView mv = null;
		mv = new ModelAndView(index);
        return mv;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request) {
    	//清理授权登录的SESSION缓存变量
    	request.getSession().removeAttribute("auth_userName");
    	request.getSession().removeAttribute(SystemConstants.SESSION_CUSTOMER);
    	request.getSession().removeAttribute("auth_portalIndex");
        return "base/login";
    }
    
    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public String authLogin(HttpServletRequest request) throws Exception{
    	String token = request.getParameter("token");
    	if(token == null || "".equals(token)){
    		throw new Exception("auth授权失败,请联系系统管理员!");
    	}
    	String key = token;
    	token = RedisUtil.getString(token);
    	if(token == null || "".equals(token)){
    		throw new Exception("auth授权过期,请重新获取授权码!");
    	}
    	//每个TOKEN只能使用一次
    	RedisUtil.deleteByKey(key);
    	String t[] = token.split("\\|");
    	String userName = t[0];
    	String cstId = t[1];
    	String authUser = t[2];
    	String url = t[3];
    	
    	//通过配置取出auth真正对应的系统登录用户
    	String tu[] = authLoginUser.split("\\|");
    	Map<String, String> userMap = new HashMap<String, String>();
    	for(String u : tu){
    		String tex[] = u.split("_");
    		userMap.put(tex[0], tex[1]);
    	}
    	
    	String tp[] = authIndex.split("\\|");
    	Map<String, String> urlMap = new HashMap<String, String>();
    	for(String tUrl : tp){
    		String tel[] = tUrl.split("_");
    		urlMap.put(tel[0], tel[1]);
    	}
    	
    	int iCstId = 0;
    	if(cstId != null && !"".equals(cstId) && !"all".equals(cstId)){
    		iCstId = Integer.parseInt(cstId);
    	}
    	
    	request.getSession().setAttribute("auth_userName", userName);
    	request.getSession().setAttribute(SystemConstants.SESSION_CUSTOMER, iCstId);
    	request.getSession().setAttribute("auth_portalIndex", urlMap.get(authUser));
    	request.getSession().setAttribute("auth_url", url);
    	
    	PrvUser user = new PrvUser();
    	user.setLoginName(userMap.get(authUser));
    	user = userService.queryByModel(user);
    	request.getSession().setAttribute(SystemConstants.SESSION_USER, user);
		
		PrvUserRole ur = new PrvUserRole();
		ur.setUserId(user.getId());
		ur = userService.queryUserRole(ur);
		List<PrvRoleFunction> list = roleService.queryFunctionByRoleId(ur.getRoleId());
		Map<String, Function> functionMap = new LinkedHashMap<String, Function>();
		List<Function> fList = new ArrayList<Function>();
		for(PrvRoleFunction f : list){
			String fId = f.getFunctionId();
			if(SystemConstants.FUNCTION_MAP.containsKey(fId)){
				functionMap.put(fId, SystemConstants.FUNCTION_MAP.get(fId));
				fList.add(SystemConstants.FUNCTION_MAP.get(fId));
			}
		}
		
		request.getSession().setAttribute(SystemConstants.SESSION_FUNCTION, functionMap);
		request.getSession().setAttribute(SystemConstants.SESSION_MENU, createModuleMapByFunction(fList));
    	
    	return "redirect:/index";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public AjaxSupport<Object> login(PrvUser user, HttpServletRequest request) throws Exception{
    	AjaxSupport<Object> as = new AjaxSupport<Object>();
    	String validateCode = request.getParameter("validateCode");
    	String sessionVcode = (String)request.getSession().getAttribute("loginValidateCode");
    	if(sessionVcode == null){
    		as.setErrorMessage("验证码过期!");
    		return as;
    	}
    	
    	long codeTime = (long)request.getSession().getAttribute(SystemConstants.SESSION_VCODE_TIME);
    	long expire = SystemConstants.SESSION_VCODE_EXPIRE;
    	long now = System.currentTimeMillis();
    	if(!validateCode.equals(sessionVcode) || (codeTime + expire) < now){
    		as.setErrorMessage("您输入的验证码有误!");
    		return as;
    	}
    	
		request.getSession().removeAttribute("loginValidateCode");
		String tPass = Cryption.enCrytor(user.getLoginPassword());
		user.setLoginPassword(null);
		PrvUser inUser = userService.queryUserByLogin(user);
		
		if(inUser == null){
			as.setErrorMessage("用户不存在!");
    		return as;
		}
		
    	int max = inUser.getLoginErrorTimes();
    	if(max >= SystemConstants.LOGIN_MAX_ERROR_TIMES){
			as.setErrorMessage("用户名或密码错误次数超过限制,请联系管理员!");
        	return as;
		}
		if(!tPass.equals(inUser.getLoginPassword())){
			inUser.setLoginErrorTimes(++max);
			userService.saveUser(inUser, true);
			as.setErrorMessage("用户密码错误!");
    		return as;
		}
		if("TY".equals(inUser.getState())){
			as.setErrorMessage("该帐号被停用!");
    		return as;
		}
		inUser.setLoginIp(request.getRemoteAddr());
		inUser.setLoginTime(new Date());
		userService.saveUser(inUser, true);
		request.getSession().setAttribute(SystemConstants.SESSION_USER, inUser);
		
		PrvUserRole ur = new PrvUserRole();
		ur.setUserId(inUser.getId());
		ur = userService.queryUserRole(ur);
		List<PrvRoleFunction> list = roleService.queryFunctionByRoleId(ur.getRoleId());
		Map<String, Function> functionMap = new LinkedHashMap<String, Function>();
		List<Function> fList = new ArrayList<Function>();
		for(PrvRoleFunction f : list){
			String fId = f.getFunctionId();
			if(SystemConstants.FUNCTION_MAP.containsKey(fId)){
				functionMap.put(fId, SystemConstants.FUNCTION_MAP.get(fId));
				fList.add(SystemConstants.FUNCTION_MAP.get(fId));
			}
		}
		
		request.getSession().setAttribute(SystemConstants.SESSION_FUNCTION, functionMap);
		request.getSession().setAttribute(SystemConstants.SESSION_MENU, createModuleMapByFunction(fList));
		
    	return as;
    }
    
    /**
     * 根据功能列表生成对用的模块Map
     * @param fList
     * @return
     */
    private Map<String, Module> createModuleMapByFunction(List<Function> fList){
    	Map<String, Module> moduleMap = new LinkedHashMap<String, Module>();
		for(Function fun : fList){
			Module md = null;
			if(moduleMap.containsKey(fun.getModuleId())){
				md = moduleMap.get(fun.getModuleId());
			}
			else{
				md = new Module();
			}
			md.setId(fun.getModuleId());
			md.setName(fun.getModuleName());
			md.setDesc(fun.getModuleDesc());
			md.setOrder(fun.getModuleOrder());
			List<Function> temFunList = null;
			if(md.getFunList() == null){
				temFunList = new ArrayList<Function>();
				temFunList.add(fun);
				md.setFunList(temFunList);
			}
			else{
				temFunList = md.getFunList();
				temFunList.add(fun);
				md.setFunList(temFunList);
			}
			moduleMap.put(md.getId(), md);
		}
		
		/**
		 * 根据模块order排序
		 */
		List<Map.Entry<String, Module>> sortList = new ArrayList<Map.Entry<String,Module>>(
				moduleMap.entrySet());
		Collections.sort(sortList, new Comparator<Map.Entry<String, Module>>() {
			public int compare(Entry<String, Module> o1, Entry<String, Module> o2) {
				return o1.getValue().getOrder() - o2.getValue().getOrder();
			}
		});
		moduleMap.clear();
		for(Map.Entry<String, Module> temEntry : sortList){
			moduleMap.put(temEntry.getKey(), temEntry.getValue());
		}
		/**
		 * 根据功能order排序
		 */
		for(String sortListKey : moduleMap.keySet()){
			Module sortListMd = moduleMap.get(sortListKey);
			List<Function> functionList = sortListMd.getFunList();
			Collections.sort(functionList, new Comparator<Function>() {
				public int compare(Function o1, Function o2) {
					return o1.getOrder() - o2.getOrder();
				}
			});
			sortListMd.setFunList(functionList);
			moduleMap.put(sortListKey, sortListMd);
		}
		return moduleMap;
    }

    @RequestMapping(value = "/exit", method = RequestMethod.GET)
    public String exitLogin(HttpServletRequest request) throws Exception {
        request.getSession().invalidate();
        return "redirect:/login";
    }

    @RequestMapping(value = "/deny", method = RequestMethod.GET)
    public String deny(HttpServletRequest request) throws Exception {
        return "base/deny";
    }
    
    @RequestMapping(value = "/notFound", method = RequestMethod.GET)
    public String notFound(HttpServletRequest request) throws Exception {
        return "base/404";
    }
    
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public AjaxSupport<Object> updatePassword(HttpServletRequest request) throws Exception{
    	AjaxSupport<Object> as = new AjaxSupport<Object>();
    	String oldPwd =  request.getParameter("oldPwd");
    	String newPwd =  request.getParameter("newPwd");
    	
    	PrvUser u = (PrvUser)request.getSession().getAttribute(SystemConstants.SESSION_USER);
		if (u == null) {
			as.setErrorMessage("登录过期，请重新登录！");
			return as;
		}
		
		/**
		 * 重新查询防止内存数据不同步
		 */
		u = userService.queryUserById(u.getId());
		if (u.getLoginPassword().equals(Cryption.enCrytor(oldPwd))) {
			if (!CommonUtils.isEmpty(newPwd)) {
				u.setLoginPassword(Cryption.enCrytor(newPwd));
				userService.saveUser(u, true);
			}
		} else {
			as.setErrorMessage("原密码错误！");
		}
    	return as;
    }
}
