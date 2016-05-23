package com.server.netty.websocket;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;


import com.server.Utils.NotificationCenter;
import com.server.game.proto.ProtoTag;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;
import com.server.game.proto.ProtoLogin;

public class MsgManager {
	private static MsgManager instance = null;
	public static MsgManager getInstance(){
		if (null == instance) {
			instance = new MsgManager();
		}
		return instance;
	}
	public String decode(String msg){
		return msg;
	}
	public String encode(String msg){
		return msg;
	}
	public void getMsg(String msg,Channel channel) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException, JSONException{
		int tag = 0;
		JSONObject rj = new JSONObject(decode(msg));
		tag = rj.getInt("tag");
		switch (ProtoTag.getEnumTag(tag)) {
		case PROTOLOGIN:
			ProtoLogin.getInstance().loginReq(rj,channel);	
			break;
		default:
			NotificationCenter.getInstance().postNotification(tag, rj);			
			break;
		}
	}
	public void sendMsg(String msg,Channel channel){
		// 返回应答给客户端
		channel.writeAndFlush(new TextWebSocketFrame(msg));
	}
}
