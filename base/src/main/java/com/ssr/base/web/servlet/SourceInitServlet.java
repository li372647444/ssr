package com.ssr.base.web.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssr.base.model.dynamic.DynamicTableManage;
import com.ssr.base.model.system.Function;
import com.ssr.base.model.system.Module;
import com.ssr.base.service.dynamic.DynamicTableManageService;
import com.ssr.base.util.SpringBeanContext;
import com.ssr.base.util.SystemConstants;
import com.ssr.base.util.annotation.Right;

/**
 * 初始化相关内容servlet
 *
 */
public class SourceInitServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(getClass());
	
	private DynamicTableManageService dynamicTableManageService;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doPost(req, resp);
	}

	@Override
	public void init() throws ServletException {
		Map<String, Object> map = SpringBeanContext.getBeansWithAnnotation(Controller.class);
		Object bean = null;
		RequestMapping brm = null;
		RequestMapping mrm = null;
		Right r = null;
		String url = null;
		/**
		 * 通过Controller获取所有的HTTP服务类
		 */
		for(String key : map.keySet()){
			bean = map.get(key);
			Method m[] = bean.getClass().getMethods();
			brm = bean.getClass().getAnnotation(RequestMapping.class);
			/**
			 * 通过METHOD获取所有的HTTP服务方法
			 */
			for(Method t : m){
				r = t.getAnnotation(Right.class);
				mrm = t.getAnnotation(RequestMapping.class);
				if(r != null){
					if(brm != null){
						url = brm.value()[0] + mrm.value()[0];
					}
					else{
						url = mrm.value()[0];
					}
					Function f = new Function();
					f.setId(r.id());
					f.setName(r.name());
					f.setDesc(r.desc());
					f.setOrder(r.order());
					f.setUrl(url);
					f.setIsMenu(r.isMenu());
					Module md = null;
					if(SystemConstants.MODULE_MAP.containsKey(r.moduleId())){
						md = SystemConstants.MODULE_MAP.get(r.moduleId());
					}
					else{
						md = new Module();
					}
					md.setId(r.moduleId());
					md.setName(r.moduleName());
					md.setDesc(r.moduleDesc());
					md.setOrder(r.moduleOrder());
					f.setModuleId(r.moduleId());
					f.setModuleName(r.moduleName());
					f.setModuleDesc(r.moduleDesc());
					f.setModuleOrder(r.moduleOrder());
					List<Function> temFunList = null;
					if(md.getFunList() == null){
						temFunList = new ArrayList<Function>();
						temFunList.add(f);
						md.setFunList(temFunList);
					}
					else{
						temFunList = md.getFunList();
						temFunList.add(f);
						md.setFunList(temFunList);
					}
					SystemConstants.FUNCTION_MAP.put(r.id(), f);
					SystemConstants.MODULE_MAP.put(r.moduleId(), md);
					
				}
			}
		}
		
		//查找动态表，缓存成菜单与权限
		addDynamicTableModuleMap();
		
		/**
		 * 根据模块order排序
		 */
		List<Map.Entry<String, Module>> sortList = new ArrayList<Map.Entry<String,Module>>(
				SystemConstants.MODULE_MAP.entrySet());
		Collections.sort(sortList, new Comparator<Map.Entry<String, Module>>() {
			public int compare(Entry<String, Module> o1, Entry<String, Module> o2) {
				return o1.getValue().getOrder() - o2.getValue().getOrder();
			}
		});
		SystemConstants.MODULE_MAP.clear();
		for(Map.Entry<String, Module> temEntry : sortList){
			SystemConstants.MODULE_MAP.put(temEntry.getKey(), temEntry.getValue());
		}
		/**
		 * 根据功能order排序
		 */
		for(String sortListKey : SystemConstants.MODULE_MAP.keySet()){
			Module sortListMd = SystemConstants.MODULE_MAP.get(sortListKey);
			List<Function> functionList = sortListMd.getFunList();
			Collections.sort(functionList, new Comparator<Function>() {
				public int compare(Function o1, Function o2) {
					return o1.getOrder() - o2.getOrder();
				}
			});
			sortListMd.setFunList(functionList);
			SystemConstants.MODULE_MAP.put(sortListKey, sortListMd);
		}
	}
	
	
	/**
	 * 查找动态表，缓存成菜单与权限
	 * **/
	private void addDynamicTableModuleMap(){
		if(logger.isDebugEnabled()){
			logger.debug("查找动态表，缓存成菜单与权限.....");
		}
		dynamicTableManageService = SpringBeanContext.getBean(DynamicTableManageService.class);
		List<DynamicTableManage> dynamicTableManages = dynamicTableManageService.queryDynamicTableManageByModel(new DynamicTableManage());
		
		if(dynamicTableManages!=null && dynamicTableManages.size()>0){
			//模块 菜单
			Module module = new Module();
			module.setId("SSR_B_DYNAMIC");
			module.setName("数据管理");
			module.setDesc("数据管理");
			module.setOrder(7);
			
			int order = 1;
			for(DynamicTableManage dt:dynamicTableManages){
				String tableName = dt.getTableName();
				String idFirst = "SSR_B_DYNAMIC_"+tableName.toUpperCase();
				String name = dt.getRemark();
				//增删改查
				List<Function> functions = new ArrayList<Function>();
				Function listPage = new Function();
				listPage.setId(idFirst +"_LIST_PAGE");
				listPage.setName(name+"管理");
				listPage.setDesc(name+"列表页面");
				listPage.setOrder(order++);
				listPage.setUrl("/dynamic/dynamicManage/"+ tableName +"/list");
				listPage.setIsMenu(true);
				listPage.setModule(module);
				functions.add(listPage);
				
				Function listPost = new Function();
				listPost.setId(idFirst +"_LIST_POST");
				listPost.setName(name+"列表");
				listPost.setDesc(name+"列表查询");
				listPost.setUrl("/dynamic/dynamicManage/"+ tableName +"/list");
				listPost.setIsMenu(false);
				listPost.setModule(module);
				functions.add(listPost);
				
				Function addPage = new Function();
				addPage.setId(idFirst +"_ADD_PAGE");
				addPage.setName(name+"新增页面");
				addPage.setDesc(name+"新增页面");
				addPage.setUrl("/dynamic/dynamicManage/"+ tableName +"/add");
				addPage.setIsMenu(false);
				addPage.setModule(module);
				functions.add(addPage);
				
				Function addPost = new Function();
				addPost.setId(idFirst +"_ADD_POST");
				addPost.setName(name+"新增");
				addPost.setDesc(name+"新增");
				addPost.setUrl("/dynamic/dynamicManage/"+ tableName +"/add");
				addPost.setIsMenu(false);
				addPost.setModule(module);
				functions.add(addPost);
				
				Function updatePage = new Function();
				updatePage.setId(idFirst +"_UPDATE_PAGE");
				updatePage.setName(name+"修改页面");
				updatePage.setDesc(name+"修改页面");
				updatePage.setUrl("/dynamic/dynamicManage/"+ tableName +"/update");
				updatePage.setIsMenu(false);
				updatePage.setModule(module);
				functions.add(updatePage);
				
				Function updatePost = new Function();
				updatePost.setId(idFirst +"_UPDATE_POST");
				updatePost.setName(name+"修改");
				updatePost.setDesc(name+"修改");
				updatePost.setUrl("/dynamic/dynamicManage/"+ tableName +"/update");
				updatePost.setIsMenu(false);
				updatePost.setModule(module);
				functions.add(updatePost);
				
				Function deletePost = new Function();
				deletePost.setId(idFirst +"_DELETE_POST");
				deletePost.setName(name+"删除");
				deletePost.setDesc(name+"删除");
				deletePost.setUrl("/dynamic/dynamicManage/"+ tableName +"/delete");
				deletePost.setIsMenu(false);
				deletePost.setModule(module);
				functions.add(deletePost);
				module.setFunList(functions);
				
				SystemConstants.FUNCTION_MAP.put(listPage.getId(), listPage);
				SystemConstants.FUNCTION_MAP.put(listPost.getId(), listPost);
				SystemConstants.FUNCTION_MAP.put(addPage.getId(), addPage);
				SystemConstants.FUNCTION_MAP.put(addPost.getId(), addPost);
				SystemConstants.FUNCTION_MAP.put(updatePage.getId(), updatePage);
				SystemConstants.FUNCTION_MAP.put(updatePost.getId(), updatePost);
				SystemConstants.FUNCTION_MAP.put(deletePost.getId(), deletePost);
				order++;
				if(logger.isDebugEnabled()){
					logger.debug("加载动态功能-----"+listPage.getId()+ "---" +listPage.getName());
				}
			}
			SystemConstants.MODULE_MAP.put(module.getId(), module);
		}
		if(logger.isDebugEnabled()){
			for(String key:SystemConstants.MODULE_MAP.keySet()){
				logger.debug("已获取权限功能-----"+key+ "---" +SystemConstants.MODULE_MAP.get(key).getName());
			}
		}
	}
}
