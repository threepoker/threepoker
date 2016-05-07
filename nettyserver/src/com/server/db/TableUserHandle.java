package com.server.db;

import java.sql.Timestamp;

import com.server.db.TableCommonHandle;

public class TableUserHandle {
	public void insertUser(String username,String password,String deviceId,String nickName,int channelId,String model,String version,boolean isRobot){
		TableCommonHandle tableCommonHandle = new TableCommonHandle();
		Timestamp createTimestamp = new Timestamp(System.currentTimeMillis());
		String sql = String.format("insert user(userName,password,deviceId,nickName,channelId,model,version,isRobot,createTime,lastLoginTime) values('%s','%s','%s','%s',%d,'%s','%s',%b,'%s','%s')",username,password,deviceId,nickName,channelId,model,version,isRobot,createTimestamp.toString(),createTimestamp.toString());
		int userId = tableCommonHandle.executeUpdate(sql);
	}
}
