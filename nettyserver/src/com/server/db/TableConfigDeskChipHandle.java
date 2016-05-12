package com.server.db;

import java.sql.ResultSet;

public class TableConfigDeskChipHandle {
	private static TableConfigDeskChipHandle instance = null;
	public static TableConfigDeskChipHandle getInstance() {
		if (null == instance) {
			instance = new TableConfigDeskChipHandle();
		}
		return instance;
	}
	public ResultSet select(){
		String sqlString = String.format("select * from configDeskChip");
		return TableCommonHandle.getInstance().executeQuery(sqlString);
	}
}
