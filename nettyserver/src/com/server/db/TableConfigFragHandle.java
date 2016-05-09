package com.server.db;

import java.sql.ResultSet;

public class TableConfigFragHandle {
	private static TableConfigFragHandle instance = null;
	public static TableConfigFragHandle getInstance() {
		if (null == instance) {
			instance = new TableConfigFragHandle();
		}
		return instance;
	}
	public ResultSet select(){
		String sqlString = String.format("select * from configFrag");
		return TableCommonHandle.getInstance().executeQuery(sqlString);
	}
}
