package com.server.db;

import java.sql.SQLException;

import com.server.Utils.XFException;
import com.server.db.ConnectionPool;
import com.server.db.ConnectionPool.PooledConnection;


public class DBManager {

	private static PooledConnection conn;
	private static ConnectionPool connectionPool;
	private static DBManager inst;

	public void close() {
		connectionPool.closeConnectionPool();
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
			XFException.logException(e);
		}

	}

	public static PooledConnection getConnection() {
		if (inst == null)
			new DBManager();

		conn = connectionPool.getConnection();

		return conn;
	}
}
