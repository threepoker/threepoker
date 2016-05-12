package com.server.game.proto;

import io.netty.channel.Channel;

import org.json.JSONException;
import org.json.JSONObject;

import com.server.game.data.User;
import com.server.game.manager.DeskManager;
import com.server.game.manager.UserManager;
import com.server.netty.common.MsgManager;

public class ProtoDesk {
	private static ProtoDesk instance = null;
	public static ProtoDesk getInstance() {
		if (null == instance) {
			instance = new ProtoDesk();
		}
		return instance;
	}
	public void enterDeskReq(JSONObject data,Channel channel){
		User user = UserManager.getInstance().getUser(channel);
		try {
			DeskManager.getInstance().enterDesk(user, data.getInt("level"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void enterDeskRes(Channel channel,int status){
		
	}
	
	public void notifyLeaveDeskRes(Channel channel,int userId) throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("tag", ProtoTag.PROTONOTIFYLEAVEDESK.value);
		jsonObject.put("userId", userId);
		MsgManager.getInstance().sendMsg(jsonObject.toString(),channel);
	}
}
