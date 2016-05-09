package com.server.netty.common;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.server.Utils.NotificationCenter;
import com.server.game.proto.ProtoLogin;

public class MsgManager {
	private static MsgManager instance = null;
	public static MsgManager getInstance(){
		if (null == instance) {
			instance = new MsgManager();
		}
		return instance;
	}
	public void decode(String msg,Channel channel) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException{
		int tag = 0;
		try {
			JSONObject rj = new JSONObject(msg);
			tag = rj.getInt("tag");
			if (ProtoTag.getEnumTag(tag) == ProtoTag.LOGIN) {
				ProtoLogin.getInstance().LoginRes(rj,channel);					
			}else{
				NotificationCenter.getInstance().postNotification(Integer.toString(tag), rj);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String encode(String msg){
		return msg;
	}
	public void sendMsg(String msg,Channel channel){
		channel.writeAndFlush(msg);
	}
}
