package com.ssr.base.util.composite.jexcel;

import java.sql.Connection;

/**
 * 设置数据库连接 
 *
 */
public interface ConnectionSupervise {
	
	public Connection getConnection() throws Exception;

	public void closeConnection(Connection con) throws Exception;
}