package com.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;
import com.server.db.ConnectionPool.PooledConnection;

public class TableCommonHandle {
	private static TableCommonHandle instance = null;
	public static TableCommonHandle getInstance() {
		if (null == instance) {
			instance = new TableCommonHandle();
		}
		return instance;
	}
	public int executeUpdate(String sql) {//增删改
		ResultSet rs = null;
		PooledConnection conn = null;
		int id = 0;
		try {
			conn = DBManager.getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
		
			preparedStatement.executeUpdate();
			rs = preparedStatement.getGeneratedKeys();
			if (rs != null && rs.next()){
				id = rs.getInt(1); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (null != conn) {
				conn.close();				
			}
		}
		return id;
	}
	public ResultSet executeQuery(String sql) {//查
		PooledConnection conn = null;
		ResultSet rSet = null;
		try {
			conn = DBManager.getConnection();
			rSet = conn.executeQuery(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (null != conn) {
				conn.close();				
			}
		}
		return rSet;
	}
}
