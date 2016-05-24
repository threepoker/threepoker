package com.server.netty.websocket;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;



import com.server.Utils.NotificationCenter;
import com.server.Utils.XFStack;
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
	public void getMsg(String msg,Channel channel) {
		int tag = 0;
		try {
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
		} catch (Exception e) {
			XFStack.logStack(e);
		}
	}
	public void sendMsg(String msg,Channel channel){
		// 返回应答给客户端
		channel.writeAndFlush(new TextWebSocketFrame(msg));
	}
}
