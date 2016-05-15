package com.server.game.classic;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import com.server.game.data.BaseConfig;
import com.server.game.data.DeskUserData;
import com.server.game.data.User;
import com.server.game.proto.ProtoDesk;

public class Desk {
	private int level;
	private Map<Integer, User> userMap = new HashMap<Integer,User>();
	private Map<Integer, DeskUserData> userDataMap = new HashMap<Integer,DeskUserData>();
	private int putIntoTotalGold = 0;
	private int	singlePutIntoGold = 0;
	private int currentRound = 0;
	private int maxRound = BaseConfig.getInstance().MAXROUND;
	private int curTurnUserId = 0;
	private int curTurnEndTime = 0;//秒
	public Desk(int level) {
		this.level = level;
	}
	public void addUser(User user) throws JSONException{
		for (User userIterUser : userMap.values()) {
			ProtoDesk.getInstance().notifyEnterDeskRes(userIterUser.getChannel(),user);
		}
		int pos = getPos();
		userMap.put(user.getUserId(), user);
		user.setPos(pos);
		DeskUserData deskUserData = new DeskUserData(user.getUserId());
		this.userDataMap.put(user.getUserId(), deskUserData);
	}
	public void removeUser(User user) throws JSONException{
		userMap.remove(user.getUserId());
		for (User userIterUser : userMap.values()) {
			ProtoDesk.getInstance().notifyExitDeskRes(userIterUser.getChannel(),user);
		}
	}
	
	
	public int getLevel() {
		return level;
	}
	public Map<Integer, User> getUserMap() {
		return userMap;
	}	
	public Map<Integer, DeskUserData> getUserDataMap() {
		return userDataMap;
	}
	public DeskUserData getDeskUserData(int userId){
		for (DeskUserData deskUserData : userDataMap.values()) {
			if (deskUserData.getUserId() == userId) {
				return deskUserData;
			}
		}
		return null;
	}
	public int getPutIntoTotalGold() {
		return putIntoTotalGold;
	}
	public void setPutIntoTotalGold(int putIntoTotalGold) {
		this.putIntoTotalGold = putIntoTotalGold;
	}
	public int getSinglePutIntoGold() {
		return singlePutIntoGold;
	}
	public void setSinglePutIntoGold(int singlePutIntoGold) {
		this.singlePutIntoGold = singlePutIntoGold;
	}
	public int getCurrentRound() {
		return currentRound;
	}
	public void setCurrentRound(int currentRound) {
		this.currentRound = currentRound;
	}
	public int getCurTurnUserId() {
		return curTurnUserId;
	}
	public void setCurTurnUserId(int curTurnUserId) {
		this.curTurnUserId = curTurnUserId;
	}
	public int getCurTurnEndTime() {
		return curTurnEndTime;
	}
	public void setCurTurnEndTime(int curTurnEndTime) {
		this.curTurnEndTime = curTurnEndTime;
	}
	/////////////////////////位置管理//////////////////////
	//一桌五个人 分：0、1、2、3、4号座位
	public int getPos(){
		int result = -1;
		for (int i = 0; i < 5; i++) {
			result = i;
			for (User user : userMap.values()) {
				if (i == user.getPos()) {
					result = -1;
					break;
				}
			}
			if (result != -1) {
				break;
			}
		}
		return result;
	}
	
}
