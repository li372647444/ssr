package com.ssr.base.util.composite.jexcel.exceldb;

import com.ssr.base.util.composite.jexcel.ConnectionSupervise;

public class ConnectionSuperviseFactory {

	public static ConnectionSupervise getConnectionSupervise(String connectionSuperviseClass) throws Exception {
		try {
			return (ConnectionSupervise) Class.forName(connectionSuperviseClass).newInstance();
		} catch (ClassNotFoundException ex) {
			throw new Exception("指定的数据库连接获取、关闭类[" + connectionSuperviseClass + "]不存在！");
		} catch (IllegalAccessException ex) {
			throw new Exception("指定的数据库连接获取、关闭类[" + connectionSuperviseClass + "]无法访问！");
		} catch (InstantiationException ex) {
			throw new Exception("实例化指定的数据库连接获取、关闭类[" + connectionSuperviseClass + "]出错，请确认该类含有无参构造函数！");
		}
	}
}
