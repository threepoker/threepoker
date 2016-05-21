package com.server.game.proto;

import java.util.Iterator;

import io.netty.channel.Channel;

import org.json.JSONException;
import org.json.JSONObject;

import com.server.game.classic.Desk;
import com.server.game.common.Const;
import com.server.game.data.BaseConfig;
import com.server.game.data.DeskUserData;
import com.server.game.data.User;
import com.server.game.manager.DeskManager;
import com.server.game.manager.UserManager;
import com.server.netty.websocket.MsgManager;

public class ProtoDesk {
	private static ProtoDesk instance = null;
	public static ProtoDesk getInstance() {
		if (null == instance) {
			instance = new ProtoDesk();
		}
		return instance;
	}
	public void enterDeskReq(JSONObject data,Channel channel) throws JSONException{
		User user = UserManager.getInstance().getUser(channel);
		enterDeskRes(channel, DeskManager.getInstance().enterDesk(user, data.getInt("level")));
	}
	public void enterDeskRes(Channel channel,String res) throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("tag", ProtoTag.PROTOENTERDESK.value);
		if (Const.SUCCESS == res) {
			jsonObject.put("status", 1);
		}else {
			jsonObject.put("status", 0);
			jsonObject.put("result", res);
		}
		MsgManager.getInstance().sendMsg(jsonObject.toString(), channel);
	}
	public void notifyEnterDeskRes(Channel channel,User user) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("tag", ProtoTag.PROTONOTIFYENTERDESK.value);
		jsonObject.put("userId", user.getUserId());
		jsonObject.put("nickName",user.getUserName());
		jsonObject.put("gold", user.getGold());
		jsonObject.put("pos", user.getDeskUserData().getPos());
		jsonObject.put("head", user.getHead());
		MsgManager.getInstance().sendMsg(jsonObject.toString(),channel);
	}
	public void exitDeskReq(JSONObject data,Channel channel) throws JSONException{
		User user = UserManager.getInstance().getUser(channel);
		exitDeskRes(channel, DeskManager.getInstance().exitDesk(user));
	}
	public void exitDeskRes(Channel channel,String res) throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("tag", ProtoTag.PROTOEXITDESK.value);
		if (Const.SUCCESS == res) {
			jsonObject.put("status", 1);
		}else {
			jsonObject.put("status", 0);
			jsonObject.put("result", res);
		}
		MsgManager.getInstance().sendMsg(jsonObject.toString(), channel);
	}
	public void notifyExitDeskRes(Channel channel,User user) throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("tag", ProtoTag.PROTONOTIFYEXITDESK.value);
		jsonObject.put("userId", user.getUserId());
		MsgManager.getInstance().sendMsg(jsonObject.toString(),channel);
	}
	
	public void getDeskInfoReq(JSONObject data,Channel channel) throws JSONException{
		getDeskInfoRes(channel);
	}
	public void getDeskInfoRes(Channel channel) throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("tag", ProtoTag.PROTOGETDESKINFO.value);
		User user = UserManager.getInstance().getUser(channel);
		Desk desk = DeskManager.getInstance().getDesk(user.getUserId());
		if (null == desk) {
			jsonObject.put("status", 0);
			jsonObject.put("result", "进入牌桌失败");
			MsgManager.getInstance().sendMsg(jsonObject.toString(), channel);
			return;
		}
		int userNum = desk.getUserMap().size();
		jsonObject.put("playerNum", userNum);
		jsonObject.put("status", 0);
		int index = 0;
		for (User userIterUser : desk.getUserMap().values()) {
			jsonObject.put(index+"_userId", userIterUser.getUserId());
			jsonObject.put(index+"_userGold", userIterUser.getGold());
			DeskUserData deskUserData = desk.getDeskUserData(userIterUser.getUserId());
			if (null != deskUserData) {
				jsonObject.put(index+"_userPutInGold", deskUserData.getPutInGold());
				jsonObject.put(index+"_userIsPlaying", deskUserData.isPlaying());
				jsonObject.put(index+"_userIsSeeCard", deskUserData.isSeeCard());
				jsonObject.put(index+"_userIsGiveUp", deskUserData.isGiveUp());
			}
			jsonObject.put(index+"_userPos", userIterUser.getDeskUserData().getPos());
		}
		jsonObject.put("putIntoTotalGold", desk.getPutIntoTotalGold());
		jsonObject.put("singlePutIntoGold", desk.getSinglePutIntoGold());
		jsonObject.put("currentRound", desk.getCurrentRound());
		jsonObject.put("maxRound", BaseConfig.getInstance().MAXROUND);
		jsonObject.put("curTurnUserId ", desk.getCurrentRound());
		jsonObject.put("curTurnEndTime  ", desk.getCurTurnEndTime());
	}
	public void notifyDealCardRes(Channel channel,int bankerId) throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("tag", ProtoTag.PROTONOTIFYDEALCARD.value);
		jsonObject.put("bankerId", bankerId);
		MsgManager.getInstance().sendMsg(jsonObject.toString(),channel);
	}
}
