package com.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.server.db.ConnectionPool.PooledConnection;

public class TableUserHandle {
	public void insertUser(){
		String sql = "select * from user";
		ResultSet rs;
		PooledConnection conn = null;
		try {
			conn = DBManager.getConnection();
			rs = conn.executeQuery(sql);
			
			if (rs.next()){
				 System.out.println(rs.getInt(1)+"/t"+rs.getString(2)+"/t"+rs.getString(3)); 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != conn) {
				conn.close();				
			}
		}
	}
}
