package com.ssr.console.web.controller.dynamic;

import java.util.Date;
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
import com.ssr.base.util.AjaxSupport;
import com.ssr.base.util.SystemConstants;
import com.ssr.base.util.annotation.Right;
import com.ssr.base.util.constantenum.MysqlColumnTypeEnum;
import com.ssr.base.util.constantenum.QueryConditionSymbolEnum;
import com.ssr.base.web.controller.BaseController;
import com.ssr.console.model.dynamic.DynamicColumnManage;
import com.ssr.console.model.dynamic.DynamicTableManage;
import com.ssr.console.model.system.PrvUser;
import com.ssr.console.service.dynamic.DynamicColumnManageService;
import com.ssr.console.service.dynamic.DynamicTableManageService;

@Controller
@RequestMapping("/dynamic")
public class DynamicColumnController extends BaseController {
	
	@Autowired
	private DynamicTableManageService dynamicTableManageService;
	
	@Autowired
	private DynamicColumnManageService dynamicColumnManageService;
	
	@Right(id = "SSR_C_DYNAMIC_DYNAMICCOLUMNLIST_PAGE", name = "列管理页面",
			moduleId = "SSR_C_DYNAMIC", moduleName = "动态表管理", moduleOrder = 2)
	@RequestMapping(value = "/dynamicColumnList/{id}", method = RequestMethod.GET)
	public ModelAndView dynamicColumnList(@PathVariable int id,HttpServletRequest req){
		DynamicTableManage dynamicTableManage = dynamicTableManageService.queryDynamicTableManageById(id);
		ModelAndView mv = new ModelAndView("dynamic/dynamicColumnList");
		mv.addObject("dynamicTableManage", dynamicTableManage);
		mv.addObject("queryConditionSymbols", QueryConditionSymbolEnum.values());
		return mv;
	}
	
	@Right(id = "SSR_C_DYNAMIC_DYNAMICCOLUMNLIST_POST", name = "列列表", 
			moduleId = "SSR_C_DYNAMIC", moduleName = "动态表管理", moduleOrder = 2)
	@RequestMapping(value = "/dynamicColumnList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryDynamicColumnList(HttpServletRequest req){
		int page = 1;
		int pageSize = 10;
		if(req.getParameter("pageNo") != null){
			page = Integer.valueOf(req.getParameter("pageNo"));
		}
		if(req.getParameter("limit") != null){
			pageSize = Integer.valueOf(req.getParameter("limit"));
		}
		debug("Controller分页日志:page=" + page + "pageSize=" + pageSize);
		return pageToJson(new PageInfo<DynamicColumnManage>(dynamicColumnManageService.queryDynamicColumnManageByMap(
				getRequestParameterAsMap(req), page, pageSize)));
	}
	
	@Right(id = "SSR_C_DYNAMIC_ADDDYNAMICCOLUMN_PAGE", name = "新增列页面", 
			moduleId = "SSR_C_DYNAMIC", moduleName = "动态表管理", moduleOrder = 2)
	@RequestMapping(value = "/addDynamicColumn/{id}", method = RequestMethod.GET)
	public ModelAndView addDynamicColumn(@PathVariable int id,HttpServletRequest req){
		DynamicTableManage dynamicTableManage = dynamicTableManageService.queryDynamicTableManageById(id);
		ModelAndView mv = new ModelAndView("dynamic/addDynamicColumn");
		mv.addObject("dynamicTableManage", dynamicTableManage);
		mv.addObject("typesForMysql", MysqlColumnTypeEnum.values());
		mv.addObject("queryConditionSymbols", QueryConditionSymbolEnum.values());
		return mv;
	}
	
	@Right(id = "SSR_C_DYNAMIC_ADDDYNAMICCOLUMN_POST", name = "新增列", 
			moduleId = "SSR_C_DYNAMIC", moduleName = "动态表管理", moduleOrder = 2)
	@RequestMapping(value = "/addDynamicColumn", method = RequestMethod.POST)
	@ResponseBody
	public Object addDynamicColumn(DynamicColumnManage dynamicColumnManage,HttpServletRequest request) throws Exception{
		PrvUser user  = (PrvUser) request.getSession().getAttribute(SystemConstants.SESSION_USER);
		if(dynamicColumnManage.getId()!=null){//修改
			dynamicColumnManage.setUpdateUserId(user.getId());
			dynamicColumnManage.setUpdateTime(new Date());
			dynamicColumnManageService.updateDynamicColumnManage(dynamicColumnManage);
		} else {//新增
			dynamicColumnManage.setCreateUserId(user.getId());
			dynamicColumnManage.setCreateTime(new Date());
			dynamicColumnManageService.saveDynamicColumnManage(dynamicColumnManage);
		}
		AjaxSupport<DynamicColumnManage> re = new AjaxSupport<DynamicColumnManage>();
		re.setModel(dynamicColumnManage);
		return re;
	}
	
	@Right(id = "SSR_C_DYNAMIC_UPDATEDYNAMICCOLUMN_PAGE", name = "更新列页面",
			moduleId = "SSR_C_DYNAMIC", moduleName = "动态表管理", moduleOrder = 2)
	@RequestMapping(value = "/updateDynamicColumn/{id}", method = RequestMethod.GET)
	public ModelAndView updateDynamicColumn(@PathVariable int id){
		DynamicColumnManage dynamicColumnManage = dynamicColumnManageService.queryDynamicColumnManageById(id);
		DynamicTableManage dynamicTableManage = dynamicTableManageService.queryDynamicTableManageById(dynamicColumnManage.getTableId());
		ModelAndView mv = new ModelAndView("dynamic/updateDynamicColumn");
		mv.addObject("dynamicTableManage", dynamicTableManage);
		mv.addObject("dynamicColumnManage", dynamicColumnManage);
		mv.addObject("typesForMysql", MysqlColumnTypeEnum.values());
		mv.addObject("queryConditionSymbols", QueryConditionSymbolEnum.values());
		return mv;
	}
	
	@Right(id = "SSR_C_DYNAMIC_UPDATEDYNAMICCOLUMN_POST", name = "更新列", 
			moduleId = "SSR_C_DYNAMIC", moduleName = "动态表管理", moduleOrder = 2)
	@RequestMapping(value = "/updateDynamicColumn", method = RequestMethod.POST)
	@ResponseBody
	public Object updateDynamicColumn(DynamicColumnManage dynamicColumnManage,HttpServletRequest request) throws Exception{
		PrvUser user  = (PrvUser) request.getSession().getAttribute(SystemConstants.SESSION_USER);
		dynamicColumnManage.setUpdateUserId(user.getId());
		dynamicColumnManage.setUpdateTime(new Date());
		dynamicColumnManageService.updateDynamicColumnManage(dynamicColumnManage);
		AjaxSupport<DynamicColumnManage> re = new AjaxSupport<DynamicColumnManage>();
		re.setModel(dynamicColumnManage);
		return re;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Right(id = "SSR_C_DYNAMIC_DELETEDYNAMICOLUMN_POST", name = "删除列", 
			moduleId = "SSR_C_DYNAMIC", moduleName = "动态表管理", moduleOrder = 2)
	@RequestMapping(value = "/deleteDynamicColumn/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Object deleteDynamicColumn(@PathVariable int id) throws Exception{
		dynamicColumnManageService.deleteDynamicColumnManage(id);
		AjaxSupport re = new AjaxSupport();
		re.setModel(true);
		return re;
	}
}
