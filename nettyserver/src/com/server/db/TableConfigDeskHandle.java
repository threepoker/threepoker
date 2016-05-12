package com.server.db;

import java.sql.ResultSet;

public class TableConfigDeskHandle {
	private static TableConfigDeskHandle instance = null;
	public static TableConfigDeskHandle getInstance() {
		if (null == instance) {
			instance = new TableConfigDeskHandle();
		}
		return instance;
	}
	public ResultSet select(){
		String sqlString = String.format("select * from configDesk");
		return TableCommonHandle.getInstance().executeQuery(sqlString);
	}
}
