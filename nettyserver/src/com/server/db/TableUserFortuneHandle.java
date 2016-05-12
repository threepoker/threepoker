package com.server.db;
import java.sql.ResultSet;

import com.server.game.data.BaseConfig;
public class TableUserFortuneHandle {
	private static TableUserFortuneHandle instance = null;
	public static TableUserFortuneHandle getInstance() {
		if (null == instance) {
			instance = new TableUserFortuneHandle();
		}
		return instance;
	}
	public void insertUserFortune(int userId){
		String sql = String.format("insert userFortune(userId,gold,diamond) values('%d','%d','%d')",
				userId,BaseConfig.getInstance().NEWUSERGOLD,BaseConfig.getInstance().NEWUSERDIAMOND);
		TableCommonHandle.getInstance().executeUpdate(sql);
	}
	public ResultSet select(){
		String sqlString = String.format("select * from userFortune");
		return TableCommonHandle.getInstance().executeQuery(sqlString);
	}
	public ResultSet select(String property,String value){
		String sqlString = String.format("select * from userFortune where %s='%s'",property,value);
		return TableCommonHandle.getInstance().executeQuery(sqlString);
	}
	public ResultSet select(String property,int value){
		String sqlString = String.format("select * from userFortune where %s='%d'",property,value);
		return TableCommonHandle.getInstance().executeQuery(sqlString);
	}
}
