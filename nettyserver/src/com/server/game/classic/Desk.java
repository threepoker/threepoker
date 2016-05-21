package com.server.game.classic;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import com.server.game.card.CardUtils;
import com.server.game.data.BaseConfig;
import com.server.game.data.DeskUserData;
import com.server.game.data.User;
import com.server.game.proto.ProtoDesk;

public class Desk {
	private int level;
	private Map<Integer, User> userMap = new HashMap<Integer,User>();
	private int putIntoTotalGold = 0;
	private int	singlePutIntoGold = 0;
	private int currentRound = 0;
	private int maxRound = BaseConfig.getInstance().MAXROUND;
	private int curTurnUserId = 0;
	private int curTurnEndTime = 0;//秒
	private boolean status =false;//true在玩，false等待
	private CardUtils cardUtils = new CardUtils();
	private User curOperateUser;
	public Desk(int level) {
		this.level = level;
	}
	public void addUser(User user) throws JSONException{
		for (User userIterUser : userMap.values()) {
			ProtoDesk.getInstance().notifyEnterDeskRes(userIterUser.getChannel(),user);
		}
		int pos = getPos();
		userMap.put(user.getUserId(), user);
		DeskUserData deskUserData = new DeskUserData(user.getUserId());
		deskUserData.setPos(pos);
		user.setDeskUserData(deskUserData);	
		if (1 == userMap.size()) {
			setBanker(user);
		}
		dealCard();
	}
	public void removeUser(User user) throws JSONException{
		user.setDeskUserData(null);
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
	public DeskUserData getDeskUserData(int userId){
		for (User user : userMap.values()) {
			if (user.getUserId() == userId) {
				return user.getDeskUserData();
			}
		}
		return null;
	}
	/*
	 * 游戏逻辑
	 */
	private void dealCard() throws JSONException{
		if (status || userMap.size()<2) {
			return;
		}
		status = true;
		cardUtils.reSetcards();
		for (User user : userMap.values()) {
			user.getDeskUserData().reSet();
			user.getDeskUserData().setCards(cardUtils.getRandomCards());
			ProtoDesk.getInstance().notifyDealCardRes(user.getChannel(), getBanker().getUserId());
		}
	}
	private void round(){
		
	}
	User getNextRoundUser(User user){
		User nextRoundUser = null;
		int pos = user.getDeskUserData().getPos();
		for (int i = 0; i < 5; i++) {
			pos = (pos+1)%5;
			nextRoundUser = getUserByPos(pos);
			if (null != nextRoundUser) {
				break;
			}
		}
		return nextRoundUser;
	}
	
	void setBanker(User user){
		for (User userIter : userMap.values()) {
			userIter.getDeskUserData().setBanker(user.getUserId() == userIter.getUserId());
		}
	}
	User getBanker(){
		User bankerUser = null;
		for (User user : userMap.values()) {
			if (user.getDeskUserData().isBanker()) {
				bankerUser = user;
				break;
			}
		}
		return bankerUser;
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
	//一桌五个人 ,分：0、1、2、3、4号座位
	public int getPos(){
		int result = -1;
		for (int i = 0; i < 5; i++) {
			result = i;
			for (User user : userMap.values()) {
				if (i == user.getDeskUserData().getPos()) {
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
	public User getUserByPos(int pos) {
		for(User user : userMap.values()){
			if (user.getDeskUserData().getPos() == pos) {
				return user;
			}
		}
		return null;
	}
}
