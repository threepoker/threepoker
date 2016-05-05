package com.server.db;

import java.sql.SQLException;

import com.server.db.ConnectionPool;
import com.server.db.ConnectionPool.PooledConnection;


public class DBManager {

	private static PooledConnection conn;
	private static ConnectionPool connectionPool;
	private static DBManager inst;

	public void close() {
		try {
			connectionPool.closeConnectionPool();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public DBManager() {
		if (inst != null)
			return;

		// TODO Auto-generated constructor stub

		String connStr = String.format("jdbc:mysql://%s:%d/%s", DBConfig.getInstance().mysqlHost, DBConfig.getInstance().mysqlPort,
				DBConfig.getInstance().mysqlDB);
		connectionPool = new ConnectionPool("com.mysql.jdbc.Driver", connStr, DBConfig.getInstance().mysqlUser, DBConfig.getInstance().mysqlPassword);
		try {
			connectionPool.createPool();
			inst = this;
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static PooledConnection getConnection() {
		if (inst == null)
			new DBManager();

		try {
			
			conn = connectionPool.getConnection();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return conn;
	}
}
