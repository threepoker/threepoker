package com.server.game.proto;

import java.util.Iterator;

import io.netty.channel.Channel;

import org.json.JSONException;
import org.json.JSONObject;

import com.server.Utils.NotificationCenter;
import com.server.Utils.XFLog;
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
			NotificationCenter.getInstance().addObserver(instance, "enterDeskReq", ProtoTag.PROTOENTERDESK.value, null);
			NotificationCenter.getInstance().addObserver(instance, "exitDeskReq", ProtoTag.PROTOEXITDESK.value, null);
			NotificationCenter.getInstance().addObserver(instance, "getDeskInfoReq", ProtoTag.PROTOGETDESKINFO.value, null);
			NotificationCenter.getInstance().addObserver(instance, "operateCardReq", ProtoTag.PROTOOPERATECARD.value, null);
		}
		return instance;
	}
	public void enterDeskReq(Object object) throws JSONException{
		JSONObject jsonObject = (JSONObject)(object);
		User user = UserManager.getInstance().getUser(jsonObject.getInt("userId"));
		enterDeskRes(user.getChannel(), DeskManager.getInstance().enterDesk(user, jsonObject.getInt("level")));
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
	public void exitDeskReq(Object object) throws JSONException{
		JSONObject jsonObject = (JSONObject)(object);
		User user = UserManager.getInstance().getUser(jsonObject.getInt("userId"));
		exitDeskRes(user.getChannel(), DeskManager.getInstance().exitDesk(user));
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
	
	public void getDeskInfoReq(Object object) throws JSONException{
		JSONObject jsonObject = (JSONObject)(object);
		User user = UserManager.getInstance().getUser(jsonObject.getInt("userId"));
		getDeskInfoRes(user);
	}
	public void getDeskInfoRes(User user) throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("tag", ProtoTag.PROTOGETDESKINFO.value);
		Desk desk = DeskManager.getInstance().getDesk(user.getUserId());
		if (null == desk) {
			jsonObject.put("status", 0);
			jsonObject.put("result", "进入牌桌失败");
			MsgManager.getInstance().sendMsg(jsonObject.toString(), user.getChannel());
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
	public void notifyRoundRes(User user){
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("tag", ProtoTag.PROTONOTIFYROUND.value);
			jsonObject.put("userId", user.getUserId());
			jsonObject.put("countDown", user.getDeskUserData().getDesk().getRoundCountDown());
			jsonObject.put("curRound", user.getDeskUserData().getDesk().getCurrentRound());
			//跟到底
			jsonObject.put("operate_0", user.getDeskUserData().isShowFollowToEnd());
			//弃牌
			jsonObject.put("operate_1", user.getDeskUserData().isShowGiveUp());
			//比牌
			jsonObject.put("operate_2", user.getDeskUserData().isShowCompare());
			//看牌
			jsonObject.put("operate_3",user.getDeskUserData().isShowSeeCard());
			//跟注
			jsonObject.put("operate_4", user.getDeskUserData().isShowFollow());
			//加注
			jsonObject.put("operate_5", user.getDeskUserData().isShowRise());
			
			MsgManager.getInstance().sendMsg(jsonObject.toString(),user.getChannel());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			XFLog.out(e.getMessage());
		}
	}
	
	public void operateCardReq(Object object) {
		JSONObject jsonObject = (JSONObject)(object);
		
	}
	public void operateCardRes(Channel channel) {
		
	}
}
