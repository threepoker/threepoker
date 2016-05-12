package com.server.game.proto;

import java.sql.ResultSet;
import java.sql.SQLException;

import io.netty.channel.Channel;

import org.json.JSONException;
import org.json.JSONObject;

import com.server.db.TableUserHandle;
import com.server.game.manager.UserManager;
import com.server.netty.common.MsgManager;


public class ProtoLogin {
	private static ProtoLogin instance = null;
	public static ProtoLogin getInstance() {
		if (null == instance) {
			instance = new ProtoLogin();
		}
		return instance;
	}

	public void loginRes(Channel channel,int status ,String result) throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("tag", ProtoTag.PROTOLOGIN.value);
		jsonObject.put("result", result);
		jsonObject.put("status", status);
		MsgManager.getInstance().sendMsg(jsonObject.toString(),channel);
	}
	public void loginReq(JSONObject data,Channel channel) throws JSONException, SQLException{
		String userNameString = data.getString("userName");
		String deviceIdString = data.getString("deviceId");
		int userId = 0;
		boolean loginResult = false;
		ResultSet resultSet = null;
		String reasionString = "";
		if (userNameString.length()>0) {
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
			reasionString = "登录成功";
			this.loginRes(channel, 1, reasionString);
		}else {
			reasionString = "登录失败";
			this.loginRes(channel, 0, reasionString);
		}
		System.out.println("loginResult = "+loginResult);
		
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
