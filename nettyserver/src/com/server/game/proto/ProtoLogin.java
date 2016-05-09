package com.server.game.proto;

import java.sql.ResultSet;
import java.sql.SQLException;

import io.netty.channel.Channel;

import org.json.JSONException;
import org.json.JSONObject;

import com.server.db.TableUserHandle;
import com.server.game.UserManager;


public class ProtoLogin {
	private static ProtoLogin instance = null;
	public static ProtoLogin getInstance() {
		if (null == instance) {
			instance = new ProtoLogin();
		}
		return instance;
	}

	public void LoginRes(JSONObject data,Channel channel) throws JSONException, SQLException{
		String userNameString = data.getString("userName");
		String deviceIdString = data.getString("deviceId");
		int userId = 0;
		boolean loginResult = false;
		ResultSet resultSet = null;
		String reasionString = "";
		if (userNameString.length()>0) {
			TableUserHandle instanceHandle = TableUserHandle.getInstance();
			resultSet = TableUserHandle.getInstance().selectUser("userName", userNameString);
			if (null!=resultSet && resultSet.next()) {
				loginResult = isLoginSuccess(resultSet, data);
				userId = resultSet.getInt(resultSet.findColumn("userId"));
			}else{
				reasionString = "用户名或者密码错误";
			}
		}else{
			resultSet = TableUserHandle.getInstance().selectUser("deviceId", deviceIdString);
			if (null!=resultSet && resultSet.next()) {
				userId = resultSet.getInt(resultSet.findColumn("userId"));
			}else{
				userId = TableUserHandle.getInstance().insertUser(userNameString, data.getString("password"), data.getString("deviceId"), data.getString("nickName"),
						data.getInt("channelId"), data.getString("model"), data.getString("version"), false);
			}		
			loginResult = true;
		}
		if (loginResult) {//登录成功
			UserManager.getInstance().add(userId, channel);
		}
		System.out.println("loginResult = "+loginResult);
	}
	public void LoginRep(int userId,int status,String result){
		UserManager.getInstance().
	}
	
	private boolean isLoginSuccess(ResultSet resultSet,JSONObject data) throws JSONException, SQLException {
		String userName = data.getString("userName");
		String password= data.getString("password");
		boolean loginResult = false;
		if (userName.length()>0) {
			if(resultSet.getString(resultSet.findColumn("password")) == password){
				loginResult = true;
			}else {
				loginResult = false;
			}	
		}
		return loginResult;
	}
}
