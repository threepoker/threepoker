package com.server.game.classic;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import com.server.Utils.NotificationCenter;
import com.server.Utils.NotificationTag;
import com.server.Utils.XFLog;
import com.server.game.card.CardUtils;
import com.server.game.data.BaseConfig;
import com.server.game.data.DeskUserData;
import com.server.game.data.User;
import com.server.game.manager.TimerManager;
import com.server.game.proto.ProtoDesk;
import com.server.game.proto.ProtoTag;

public class Desk {
	private int level;
	private Map<Integer, User> userMap = new HashMap<Integer,User>();
	private int putIntoTotalGold = 0;
	private int	singlePutIntoGold = 0;
	private int currentRound = 0;
	private int maxRound = BaseConfig.getInstance().MAXROUND;
	private int curTurnEndTime = 0;//秒
	private boolean status =false;//true在玩，false等待
	private CardUtils cardUtils = new CardUtils();
	private User curTurnUser;
	private int roundCountDown = 0;
	private final int ROUNDMAXTIME = 8;
	public Desk(int level) {
		this.level = level;
	}
	public void addUser(User user){
		for (User userIterUser : userMap.values()) {
			ProtoDesk.getInstance().notifyEnterDeskRes(userIterUser.getChannel(),userIterUser);
		}
		int pos = getPos();
		userMap.put(user.getUserId(), user);
		DeskUserData deskUserData = new DeskUserData(user.getUserId(),this);
		deskUserData.setPos(pos);
		user.setDeskUserData(deskUserData);	
		if (1 == userMap.size()) {
			setBanker(user);
			NotificationCenter.getInstance().addObserver(this, "operateCard", NotificationTag.Notif_OperateCard.value, null);
		}
		dealCard();
	}
	public void removeUser(User user) {
		user.setDeskUserData(null);
		userMap.remove(user.getUserId());
		for (User userIterUser : userMap.values()) {
			ProtoDesk.getInstance().notifyExitDeskRes(userIterUser.getChannel(),user);
		}
		if (0==userMap.size()) {
			NotificationCenter.getInstance().removeObserverObject(this);
		}
	}	
	
	public int getLevel() {
		return level;
	}
	public Map<Integer, User> getUserMap() {
		return userMap;
	}
	public Map<Integer, User> getNoGiveUpPlayingMap(){
		Map<Integer, User> noGiveUpPlayingMap = new HashMap<Integer,User>();
		for(User user : userMap.values()){
			if (user.getDeskUserData().isPlaying() && !user.getDeskUserData().isGiveUp()) {
				noGiveUpPlayingMap.put(user.getUserId(), user);
			}
		}
		return noGiveUpPlayingMap;
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
	private void dealCard(){
		if (status || userMap.size()<2) {
			return;
		}
		status = true;
		cardUtils.reSetcards();
		for (User user : userMap.values()) {
			user.getDeskUserData().gameBegin();
			user.getDeskUserData().setCards(cardUtils.getRandomCards());
			ProtoDesk.getInstance().notifyDealCardRes(user.getChannel(), getBanker().getUserId());
		}
		long dealCardTime = 2000;
		TimerManager.getInstance().scheduleOnce(this, "startRound",dealCardTime);
		curTurnUser = getNextRoundUser(getBanker());
	}
	public void countDown() {
		setRoundCountDown(getRoundCountDown()-1);
		XFLog.out().println("roundCountDown = "+roundCountDown);
		if (roundCountDown<=0) {
			curTurnUser.getDeskUserData().setGiveUp(true);
			curTurnUser = getNextRoundUser(curTurnUser);
			if (null == curTurnUser) {
				return;
			}
			startRound();
		}
	}
	public void startRound(){
		round();
		setRoundCountDown(ROUNDMAXTIME);
		TimerManager.getInstance().schedule(this, "countDown",1000,ROUNDMAXTIME);
	}
	public void round(){;
		for (User user : userMap.values()) {
			ProtoDesk.getInstance().notifyRoundRes(user);
		}
	}
	private User getNextRoundUser(User user){
		User nextRoundUser = null;
		int pos = user.getDeskUserData().getPos();
		for (int i = 0; i < 4; i++) {
			pos = (pos+1)%5;
			User posUser = getUserByPos(pos);
			if (null != posUser 
					&& getDeskUserData(posUser.getUserId()).isPlaying()
					&& !getDeskUserData(posUser.getUserId()).isGiveUp()) {
				nextRoundUser = posUser;
				break;
			}
		}
		//仅剩下一个没弃牌正在游戏中的玩家
		if (1 == getNoGiveUpPlayingMap().size()) {
			XFLog.out().println("胜利的玩家："+getNoGiveUpPlayingMap().toString());
			return null;
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
		return curTurnUser.getUserId();
	}
	public int getCurTurnEndTime() {
		return curTurnEndTime;
	}
	public void setCurTurnEndTime(int curTurnEndTime) {
		this.curTurnEndTime = curTurnEndTime;
	}
	public int getRoundCountDown() {
		return roundCountDown;
	}
	public void setRoundCountDown(int roundCountDown) {
		this.roundCountDown = roundCountDown;
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
	public User getUserByUserId(int userId){
		for(User user : userMap.values()){
			if (user.getDeskUserData().getUserId() == userId) {
				return user;
			}
		}
		return null;
	}
}
