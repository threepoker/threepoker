package com.server.game.proto;

import java.util.ArrayList;
import java.util.Iterator;

import io.netty.channel.Channel;

import org.json.JSONException;
import org.json.JSONObject;

import com.server.Utils.NotificationCenter;
import com.server.Utils.XFException;
import com.server.Utils.XFLog;
import com.server.Utils.XFStack;
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
			NotificationCenter.getInstance().addObserver(instance, "enterDeskReq", ProtoTag.proto_enterDesk.value, null);
			NotificationCenter.getInstance().addObserver(instance, "exitDeskReq", ProtoTag.proto_exitDesk.value, null);
			NotificationCenter.getInstance().addObserver(instance, "getDeskInfoReq", ProtoTag.proto_getDeskInfo.value, null);
			NotificationCenter.getInstance().addObserver(instance, "operateCardReq", ProtoTag.proto_operateCard.value, null);
		}
		return instance;
	}
	public void enterDeskReq(Object object) {
		JSONObject jsonObject = (JSONObject)(object);
		User user;
		try {
			user = UserManager.getInstance().getUser(jsonObject.getInt("userId"));
			enterDeskRes(user.getChannel(), DeskManager.getInstance().enterDesk(user, jsonObject.getInt("level")));
		} catch (JSONException e) {
			XFException.logException(e);
		}
	}
	public void enterDeskRes(Channel channel,String res) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("tag", ProtoTag.proto_enterDesk.value);
			if (Const.SUCCESS == res) {
				jsonObject.put("status", 1);
			}else {
				jsonObject.put("status", 0);
				jsonObject.put("result", res);
			}
			MsgManager.getInstance().sendMsg(jsonObject.toString(), channel);
		} catch (JSONException e) {
			XFException.logException(e);
		}
	}
	public void notifyEnterDeskRes(Channel channel,User user)  {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("tag", ProtoTag.proto_notifyEnterDesk.value);
			jsonObject.put("userId", user.getUserId());
			jsonObject.put("nickName",user.getUserName());
			jsonObject.put("gold", user.getGold());
			jsonObject.put("pos", user.getDeskUserData().getPos());
			jsonObject.put("head", user.getHead());
			MsgManager.getInstance().sendMsg(jsonObject.toString(),channel);
		} catch (JSONException e) {
			XFException.logException(e);
		}
	}
	public void exitDeskReq(Object object) {
		JSONObject jsonObject = (JSONObject)(object);
		User user;
		try {
			user = UserManager.getInstance().getUser(jsonObject.getInt("userId"));
			exitDeskRes(user.getChannel(), DeskManager.getInstance().exitDesk(user));
		} catch (JSONException e) {
			XFException.logException(e);
		}
	}
	public void exitDeskRes(Channel channel,String res) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("tag", ProtoTag.proto_exitDesk.value);
			if (Const.SUCCESS == res) {
				jsonObject.put("status", 1);
			}else {
				jsonObject.put("status", 0);
				jsonObject.put("result", res);
			}
			MsgManager.getInstance().sendMsg(jsonObject.toString(), channel);
		} catch (JSONException e) {
			XFException.logException(e);
		}
	}
	public void notifyExitDeskRes(Channel channel,User user) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("tag", ProtoTag.proto_notifyExitDesk.value);
			jsonObject.put("userId", user.getUserId());
			MsgManager.getInstance().sendMsg(jsonObject.toString(),channel);
		} catch (JSONException e) {
			XFException.logException(e);
		}
	}
	
	public void getDeskInfoReq(Object object) {
		JSONObject jsonObject = (JSONObject)(object);
		User user;
		try {
			user = UserManager.getInstance().getUser(jsonObject.getInt("userId"));
			getDeskInfoRes(user);
		} catch (JSONException e) {
			XFException.logException(e);
		}
	}
	public void getDeskInfoRes(User user) {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("tag", ProtoTag.proto_getDeskInfo.value);
			Desk desk = DeskManager.getInstance().getDesk(user.getUserId());
			if (null == desk) {
				jsonObject.put("status", 0);
				jsonObject.put("result", "进入牌桌失败");
				MsgManager.getInstance().sendMsg(jsonObject.toString(), user.getChannel());
				return;
			}
			int userNum = desk.getUserMap().size();
			jsonObject.put("userNum", userNum);
			jsonObject.put("status", 1);
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
				index++;
			}
			jsonObject.put("putIntoTotalGold", desk.getPutIntoTotalGold());
			jsonObject.put("singlePutIntoGold", desk.getSinglePutIntoGold());
			jsonObject.put("currentRound", desk.getCurrentRound());
			jsonObject.put("maxRound", BaseConfig.getInstance().MAXROUND);
			jsonObject.put("curTurnUserId ", desk.getCurrentRound());
			jsonObject.put("curTurnEndTime  ", desk.getCurTurnEndTime());
			MsgManager.getInstance().sendMsg(jsonObject.toString(),user.getChannel());
		} catch (JSONException e) {
			XFException.logException(e);
		}
	}
	public void notifyDealCardRes(Channel channel,int bankerId){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("tag", ProtoTag.proto_notifyDealCard.value);
			jsonObject.put("bankerId", bankerId);
			MsgManager.getInstance().sendMsg(jsonObject.toString(),channel);
		} catch (JSONException e) {
			XFException.logException(e);
		}
	}
	public void notifyRoundRes(User user){
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("tag", ProtoTag.proto_notifyRound.value);
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
			XFException.logException(e);
		}
	}
	
	public void operateCardReq(Object object) {
		JSONObject jsonObject = (JSONObject)(object);
		
	}
	public void operateCardRes(Channel channel,int status,String reasult,int userId
			,int type,int compareUserId,int winnerUserId
			,ArrayList<Integer>userCards,ArrayList<Integer>compareCards) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("tag", ProtoTag.proto_operateCard.value);
			jsonObject.put("status",status);
			jsonObject.put("result",reasult);
			jsonObject.put("userId", userId);
			jsonObject.put("type", type);
			jsonObject.put("compareUserId", compareUserId);
			jsonObject.put("winnerUserId", winnerUserId);
			if (null!=userCards) {
				for (int i = 0; i < userCards.size(); i++) {
					jsonObject.put("userCard_"+i, userCards.get(i));
				}
			}
			if (null!=compareCards) {
				for (int i = 0; i < compareCards.size(); i++) {
					jsonObject.put("compareCards_"+i, compareCards	.get(i));
				}				
			}
		} catch (JSONException e) {
			XFException.logException(e);
		}
	}
	public void operateCardRes(Channel channel,int status,String reasult,int userId
			,int type) {
		operateCardRes(channel,status,reasult,userId,type,0,0,null,null);
	}
}
