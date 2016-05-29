package com.server.game.proto;

import java.sql.ResultSet;
import java.sql.SQLException;

import io.netty.channel.Channel;

import org.json.JSONException;
import org.json.JSONObject;

import com.server.Utils.NotificationCenter;
import com.server.Utils.XFException;
import com.server.Utils.XFLog;
import com.server.Utils.XFStack;
import com.server.db.TableUserHandle;
import com.server.game.data.User;
import com.server.game.manager.UserManager;
import com.server.netty.websocket.MsgManager;


public class ProtoLogin {
	private static ProtoLogin instance = null;
	public static ProtoLogin getInstance() {
		if (null == instance) {
			instance = new ProtoLogin();
			NotificationCenter.getInstance().addObserver(instance, "getUserInfoReq", ProtoTag.proto_getUserInfo.value, null);
		}
		return instance;
	}

	public void loginRes(Channel channel,int status ,String result,int userId) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("tag", ProtoTag.proto_login.value);
			jsonObject.put("result", result);
			jsonObject.put("status", status);
			jsonObject.put("userId", userId);
			MsgManager.getInstance().sendMsg(jsonObject.toString(),channel);
		} catch (JSONException e) {
			XFException.logException(e);
		}
	}
	public void loginReq(JSONObject data,Channel channel) {
		String userNameString;
		try {
			userNameString = data.getString("userName");
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
				this.loginRes(channel, 1, reasionString,userId);
			}else {
				reasionString = "登录失败";
				this.loginRes(channel, 0, reasionString,userId);
			}
			XFLog.out().println("loginResult = "+loginResult+" userId="+userId);
		} catch (Exception e) {
			XFException.logException(e);
		}
		
	}
	public void getUserInfoRes(JSONObject data) {
		JSONObject jsonRes = new JSONObject();
		User user;
		try {
			user = UserManager.getInstance().getUser(data.getInt("userId"));
			if (null != user) {
				jsonRes.put("status", 1);
				jsonRes.put("gold", user.getGold());
				jsonRes.put("diamond", user.getDiamond());
			}else{
				jsonRes.put("status", 0);
				jsonRes.put("result", "获取用户数据失败");
			}
			jsonRes.put("tag", ProtoTag.proto_getUserInfo.value);
			MsgManager.getInstance().sendMsg(jsonRes.toString(),user.getChannel());
		} catch (JSONException e) {
			XFException.logException(e);
		}
	}
	public void getUserInfoReq(Object object){
		JSONObject josnObject = (JSONObject)(object);
		getUserInfoRes(josnObject);
	}
	
	private boolean isLoginSuccess(ResultSet resultSet,JSONObject data)  {
		String userName;
		try {
			userName = data.getString("userName");
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
		} catch (Exception e) {
			XFException.logException(e);
		}
		return false;
	}
	
	
}
