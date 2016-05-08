package com.server.game.proto;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import org.json.JSONObject;

import com.server.Utils.NotificationCenter;
import com.server.netty.common.ChannelManager;
import com.server.netty.common.ProtoTag;


public class ProtoLogin {
	private static ProtoLogin instance = null;
	public static ProtoLogin getInstance() {
		if (null == instance) {
			instance = new ProtoLogin();
			//lxftest
			NotificationCenter.getInstance().addObserver(ProtoLogin.getInstance(), "lxftest", Integer.toString(ProtoTag.LOGIN.value), null);
		}
		return instance;
	}
	public void LoginRes(JSONObject data,Channel channel){
		if (true) {//登录成功
			ChannelManager.add("1", (Channel)channel);
		}
	}
	public void LoginRep(){
		
	}
	public void lxftest(Object object) {
		JSONObject jsonObject = (JSONObject)(object);
		System.out.println("lxftest jsonObject.toString = "+jsonObject.toString());
	}
}
