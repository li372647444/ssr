package com.ssr.base.util.composite.jexcel.exceldb;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ssr.base.util.SpringBeanContext;
import com.ssr.base.util.composite.jexcel.ConnectionSupervise;

public class ConnectionSuperviseImpl implements ConnectionSupervise {

	@Override
	public Connection getConnection() throws Exception {
		DataSource ds = (DataSource)SpringBeanContext.getBean("dataSource");
		return ds.getConnection();
	}

	@Override
	public void closeConnection(Connection conn) throws Exception {
		if(conn != null){
			conn.close();
		}
	}

}
