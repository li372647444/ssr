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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssr.base.model.Function;
import com.ssr.base.model.Module;
import com.ssr.base.util.SpringBeanContext;
import com.ssr.base.util.SystemConstants;
import com.ssr.base.util.annotation.Right;

/**
 * 初始化相关内容servlet
 *
 * @author PengLian
 */
public class SourceInitServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

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
	
}
