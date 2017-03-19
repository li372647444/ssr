package com.ssr.console.web.controller.manage;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.ssr.base.util.AjaxSupport;
import com.ssr.base.util.annotation.Right;
import com.ssr.base.web.controller.BaseController;

@Controller
@RequestMapping("/manage")
public class ManageController extends BaseController {
	
	@Right(id = "SSR_C_MANAGE_LIST_PAGE", name = "管理页面", order = 1, 
			isMenu = true, moduleId = "SSR_C_MANAGE", moduleName = "数据管理", moduleOrder = 3)
	@RequestMapping(value = "/manageList", method = RequestMethod.GET)
	public String manageList(HttpServletRequest req){
		return "manage/manageList";
	}
	
	@Right(id = "SSR_C_MANAGE_ADDMANAGE_PAGE", name = "新增页面", 
			moduleId = "SSR_C_MANAGE", moduleName = "数据管理", moduleOrder = 3)
	@RequestMapping(value = "/addManage", method = RequestMethod.GET)
	public ModelAndView addManage(HttpServletRequest req){
		ModelAndView mv = new ModelAndView("manage/addManage");
		return mv;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Right(id = "SSR_C_MANAGE_ADDMANAGE_POST", name = "新增", 
			moduleId = "SSR_C_MANAGE", moduleName = "数据管理", moduleOrder = 3)
	@RequestMapping(value = "/addManage", method = RequestMethod.POST)
	@ResponseBody
	public Object addManage(Model model) throws Exception{
		AjaxSupport re = new AjaxSupport();
		re.setModel(null);
		return re;
	}
	
	@Right(id = "SSR_C_MANAGE_UPDATEMANAGE_PAGE", name = "更新页面",
			moduleId = "SSR_C_MANAGE", moduleName = "数据管理", moduleOrder = 3)
	@RequestMapping(value = "/updateManage/{id}", method = RequestMethod.GET)
	public ModelAndView updateManage(@PathVariable int id){
		ModelAndView mv = new ModelAndView("manage/updateManage");
		mv.addObject("manage", null);
		return mv;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Right(id = "SSR_C_MANAGE_UPDATEMANAGE_POST", name = "更新", 
			moduleId = "SSR_C_MANAGE", moduleName = "数据管理", moduleOrder = 3)
	@RequestMapping(value = "/updateManage", method = RequestMethod.POST)
	@ResponseBody
	public Object updateManage(Model model) throws Exception{
		AjaxSupport re = new AjaxSupport();
		re.setModel(null);
		return re;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Right(id = "SSR_C_MANAGE_DELETEMANAGE_POST", name = "删除", 
			moduleId = "SSR_C_MANAGE", moduleName = "数据管理", moduleOrder = 3)
	@RequestMapping(value = "/deleteManage/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Object deleteManage(@PathVariable int id) throws Exception{
		AjaxSupport re = new AjaxSupport();
		re.setModel(true);
		return re;
	}
	
	@SuppressWarnings("rawtypes")
	@Right(id = "SSR_C_MANAGE__MANAGELIST_POST", name = "列表", 
			moduleId = "SSR_C_MANAGE", moduleName = "数据管理", moduleOrder = 3)
	@RequestMapping(value = "/ManageList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryManageList(HttpServletRequest req){
		int page = 1;
		int pageSize = 10;
		if(req.getParameter("pageNo") != null){
			page = Integer.valueOf(req.getParameter("pageNo"));
		}
		if(req.getParameter("limit") != null){
			pageSize = Integer.valueOf(req.getParameter("limit"));
		}
		debug("Controller分页日志:page=" + page + "pageSize=" + pageSize);
		return pageToJson(new PageInfo());
	}
}
