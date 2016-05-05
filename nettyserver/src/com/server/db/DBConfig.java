package com.server.db;

public class DBConfig {
	private static DBConfig dbConfig = null;
	public final String mysqlHost = "120.25.196.22";
	public final String mysqlPort = "3306";
	public final String mysqlUser = "liuxiaofei";
	public final String mysqlPassword = "liuxiaofei";
	public final String mysqlDB = "threepoker";
	public static DBConfig getInstance(){
		if (null == dbConfig) {
			dbConfig = new DBConfig();
		}
		return dbConfig;
	}
}
