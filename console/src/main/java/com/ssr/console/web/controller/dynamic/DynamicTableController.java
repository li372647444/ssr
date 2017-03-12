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
import com.ssr.base.util.annotation.Right;
import com.ssr.base.web.controller.BaseController;
import com.ssr.console.model.dynamic.DynamicTableManage;
import com.ssr.console.service.dynamic.DynamicTableManageService;

@Controller
@RequestMapping("/dynamic")
public class DynamicTableController extends BaseController {
	
	@Autowired
	private DynamicTableManageService dynamicTableManageService;
	
	@Right(id = "SSR_C_DYNAMIC_DYNAMICTABLELIST_PAGE", name = "表管理", order = 1, 
			isMenu = true, moduleId = "SSR_C_DYNAMIC", moduleName = "动态表管理", moduleOrder = 2)
	@RequestMapping(value = "/dynamicTableList", method = RequestMethod.GET)
	public String dynamicTableList(HttpServletRequest req){
		return "dynamic/dynamicTableList";
	}
	
	@Right(id = "SSR_C_DYNAMIC_ADDDYNAMICTABLE_PAGE", name = "新增表页面", 
			moduleId = "SSR_C_DYNAMIC", moduleName = "动态表管理", moduleOrder = 2)
	@RequestMapping(value = "/addDynamicTable", method = RequestMethod.GET)
	public ModelAndView addDynamicTable(HttpServletRequest req){
		ModelAndView mv = new ModelAndView("dynamic/addDynamicTable");
		return mv;
	}
	
	@Right(id = "SSR_C_DYNAMIC_ADDDYNAMICTABLE_POST", name = "新增表", 
			moduleId = "SSR_C_DYNAMIC", moduleName = "动态表管理", moduleOrder = 2)
	@RequestMapping(value = "/addDynamicTable", method = RequestMethod.POST)
	@ResponseBody
	public Object addDynamicTable(DynamicTableManage dynamicTableManage) throws Exception{
		dynamicTableManage.setCreateTime(new Date());
		dynamicTableManageService.saveDynamicTableManage(dynamicTableManage, true);
		AjaxSupport<DynamicTableManage> re = new AjaxSupport<DynamicTableManage>();
		re.setModel(dynamicTableManage);
		return re;
	}
	
	@Right(id = "SSR_C_DYNAMIC_UPDATEDYNAMICTABLE_PAGE", name = "更新表",
			moduleId = "SSR_C_DYNAMIC", moduleName = "动态表管理", moduleOrder = 2)
	@RequestMapping(value = "/updateDynamicTable/{id}", method = RequestMethod.GET)
	public ModelAndView updateDynamicTable(@PathVariable int id){
		DynamicTableManage dynamicTableManage = dynamicTableManageService.queryDynamicTableManageById(id);
		ModelAndView mv = new ModelAndView("dynamic/updateDynamicTable");
		mv.addObject("dynamicTableManage", dynamicTableManage);
		return mv;
	}
	
	@Right(id = "SSR_C_DYNAMIC_UPDATEDYNAMICTABLE_POST", name = "更新表", 
			moduleId = "SSR_C_DYNAMIC", moduleName = "动态表管理", moduleOrder = 2)
	@RequestMapping(value = "/updateDynamicTable", method = RequestMethod.POST)
	@ResponseBody
	public Object updateDynamicTable(DynamicTableManage dynamicTableManage) throws Exception{
		dynamicTableManageService.saveDynamicTableManage(dynamicTableManage, true);
		AjaxSupport<DynamicTableManage> re = new AjaxSupport<DynamicTableManage>();
		re.setModel(dynamicTableManage);
		return re;
	}
	
	@Right(id = "SSR_C_DYNAMIC__DYNAMICTABLELIST_POST", name = "表列表", 
			moduleId = "SSR_C_DYNAMIC", moduleName = "动态表管理", moduleOrder = 2)
	@RequestMapping(value = "/dynamicTableList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryDynamicTableList(HttpServletRequest req){
		int page = 1;
		int pageSize = 10;
		if(req.getParameter("pageNo") != null){
			page = Integer.valueOf(req.getParameter("pageNo"));
		}
		if(req.getParameter("limit") != null){
			pageSize = Integer.valueOf(req.getParameter("limit"));
		}
		debug("Controller分页日志:page=" + page + "pageSize=" + pageSize);
		return pageToJson(new PageInfo<DynamicTableManage>(dynamicTableManageService.queryDynamicTableManageByMap(
				getRequestParameterAsMap(req), page, pageSize)));
	}
}
