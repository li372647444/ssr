package com.ssr.console.web.controller.dynamic;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.ssr.base.exception.UserException;
import com.ssr.base.model.dynamic.DynamicColumnManage;
import com.ssr.base.model.dynamic.DynamicTableManage;
import com.ssr.base.model.system.PrvUser;
import com.ssr.base.service.dynamic.DynamicColumnManageService;
import com.ssr.base.service.dynamic.DynamicManageService;
import com.ssr.base.service.dynamic.DynamicTableManageService;
import com.ssr.base.util.AjaxSupport;
import com.ssr.base.util.SystemConstants;
import com.ssr.base.web.controller.BaseController;

@Controller
@RequestMapping("/dynamic")
public class DynamicManageController extends BaseController {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private DynamicTableManageService dynamicTableManageService;
	@Autowired
	private DynamicColumnManageService dynamicColumnManageService;
	@Autowired
	private DynamicManageService dynamicManageService;
	
	@RequestMapping(value = "/dynamicManage/{name}/list", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable String name,HttpServletRequest req){
		if(logger.isDebugEnabled()){
			logger.debug("DynamicManageController GET==>/"+name+"/list");
		}
		//查询动态表的表信息
		DynamicTableManage table = dynamicTableManageService.queryByTableName(name);
		if(table==null){
			throw new UserException("功能已移除，请重新登录！");
		}
		//查询动态表的列信息
		List<DynamicColumnManage> columns = dynamicColumnManageService.queryDynamicColumnManageByTableId(table.getId());
		ModelAndView mv = new ModelAndView("dynamic/dynamicManageList");
		mv.addObject("table", table);
		mv.addObject("columns", columns);
		return mv;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/dynamicManage/{name}/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryList(@PathVariable String name,HttpServletRequest req){
		if(logger.isDebugEnabled()){
			logger.debug("DynamicManageController POST==>/"+name+"/list");
		}
		int page = 1;
		int pageSize = 10;
		if(req.getParameter("pageNo") != null){
			page = Integer.valueOf(req.getParameter("pageNo"));
		}
		if(req.getParameter("limit") != null){
			pageSize = Integer.valueOf(req.getParameter("limit"));
		}
		debug("Controller分页日志:page=" + page + "pageSize=" + pageSize);
		Map<String,Object> reMap = getRequestParameterAsMap(req);
		reMap.remove("pageNo");
		reMap.remove("limit");
		reMap.put("state", "1");//有效
		List<Map<String,Object>> list = dynamicManageService.queryByMap(name,reMap,page,pageSize);
		return pageToJson(new PageInfo(list));
	}
	
	@RequestMapping(value = "/dynamicManage/{name}/add", method = RequestMethod.GET)
	public ModelAndView addPage(@PathVariable String name,HttpServletRequest req){
		//查询动态表的表信息
		DynamicTableManage table = dynamicTableManageService.queryByTableName(name);
		//查询动态表的列信息
		List<DynamicColumnManage> columns = dynamicColumnManageService.queryDynamicColumnManageByTableId(table.getId());
		ModelAndView mv = new ModelAndView("dynamic/addDynamicManage");
		mv.addObject("table", table);
		mv.addObject("columns", columns);
		//mv.addObject("typesForMysql", MysqlColumnTypeEnum.values());
		return mv;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/dynamicManage/{dynamicTableName}/add", method = RequestMethod.POST)
	@ResponseBody
	public Object add(@PathVariable String dynamicTableName,HttpServletRequest req) throws Exception{
		Map<String, Object> reMap = getRequestParameterAsMap(req);
		reMap.remove("dynamicTableName");//除去 动态表名 的参数（该字段非动态表的字段）
		Map<String, Object> resultMap = null;
		if(!reMap.isEmpty()){
			PrvUser user  = (PrvUser) req.getSession().getAttribute(SystemConstants.SESSION_USER);
			Object id = reMap.get("id");
			reMap.remove("id");//除去 id 的参数（该字段非手动天机的字段  或者 该字段用于修改条件）
			if(id==null || "".equals(id.toString())){
				//新增
				reMap.put("create_time", new Date());
				reMap.put("create_user_id", user.getId());
				reMap.put("state", "1");//记录有效
				resultMap = dynamicManageService.saveDynamicManage(dynamicTableName,reMap);
			} else {
				//修改
				reMap.put("update_time", new Date());
				reMap.put("update_user_id", user.getId());
				resultMap = dynamicManageService.updateDynamicManage(dynamicTableName,Integer.valueOf(id.toString()),reMap);
			}
		}
		AjaxSupport re = new AjaxSupport();
		re.setModel(resultMap);
		return re;
	}
	
	@RequestMapping(value = "/dynamicManage/update/{id}", method = RequestMethod.GET)
	public ModelAndView updateManage(@PathVariable int id){
		ModelAndView mv = new ModelAndView("manage/updateManage");
		mv.addObject("manage", null);
		return mv;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/dynamicManage/update", method = RequestMethod.POST)
	@ResponseBody
	public Object updateManage(Model model) throws Exception{
		AjaxSupport re = new AjaxSupport();
		re.setModel(null);
		return re;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/dynamicManage/{dynamicTableName}/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Object deleteManage(@PathVariable String dynamicTableName,@PathVariable int id) throws Exception{
		boolean temp = dynamicManageService.deleteById(dynamicTableName,id);
		AjaxSupport re = new AjaxSupport();
		if(temp){
			re.setModel(true);
		} else {
			re.setSuccess(false);
			re.setErrorMessage("操作失敗！");
		}
		return re;
	}
	
}
