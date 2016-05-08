package com.server.netty.common;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.InvocationTargetException;

import org.json.JSONException;
import org.json.JSONObject;

import com.server.Utils.NotificationCenter;
import com.server.game.proto.ProtoLogin;

public class DecodeMSG {
		public static void decode(String msg,Channel channel) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
			int tag = 0;
			try {
				JSONObject rj = new JSONObject(msg);
				tag = rj.getInt("tag");
				if (ProtoTag.getEnumTag(tag) == ProtoTag.LOGIN) {
					ProtoLogin.getInstance().LoginRes(rj,channel);

					ProtoLogin.getInstance();//lxftest
					NotificationCenter.getInstance().postNotification(Integer.toString(tag), rj);
					
				}else{
					NotificationCenter.getInstance().postNotification(Integer.toString(tag), rj);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
