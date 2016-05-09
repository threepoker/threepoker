package com.server.db;

import java.sql.ResultSet;
import java.sql.Timestamp;

import com.server.db.TableCommonHandle;

public class TableUserHandle {
	private static TableUserHandle instance = null;
	public static TableUserHandle getInstance() {
		if (null == instance) {
			instance = new TableUserHandle();
		}
		return instance;
	}
	public int insertUser(String username,String password,String deviceId,String nickName,int channelId,String model,String version,boolean isRobot){
		Timestamp createTimestamp = new Timestamp(System.currentTimeMillis());
		String sql = String.format("insert user(userName,password,deviceId,nickName,channelId,model,version,isRobot,createTime,lastLoginTime) values('%s','%s','%s','%s',%d,'%s','%s',%b,'%s','%s')",username,password,deviceId,nickName,channelId,model,version,isRobot,createTimestamp.toString(),createTimestamp.toString());
		int userId = TableCommonHandle.getInstance().executeUpdate(sql);
		TableUserFortuneHandle.getInstance().insertUserFortune(userId);
		return userId;
	}
	public ResultSet selectUser(String property,String value){
		String sqlString = String.format("select * from user where %s='%s'",property,value);
		return TableCommonHandle.getInstance().executeQuery(sqlString);
	}
	public ResultSet selectUser(String property,int value){
		String sqlString = String.format("select * from user where %s='%d'",property,value);
		return TableCommonHandle.getInstance().executeQuery(sqlString);
	}
}
