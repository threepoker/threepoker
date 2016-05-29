package com.server.game.classic;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.server.Utils.NotificationCenter;
import com.server.Utils.NotificationTag;
import com.server.Utils.XFException;
import com.server.Utils.XFLog;
import com.server.game.card.CardUtils;
import com.server.game.common.Const;
import com.server.game.data.BaseConfig;
import com.server.game.data.DeskUserData;
import com.server.game.data.User;
import com.server.game.manager.TimerManager;
import com.server.game.proto.ProtoDesk;
import com.server.game.proto.ProtoTag;
import com.sun.net.httpserver.Authenticator.Success;

public class Desk {
	private int level;
	private Map<Integer, User> userMap = new HashMap<Integer,User>();
	private long putIntoTotalGold = 0;
	private long singlePutIntoGold = 0;
	private int currentRound = 0;
	private int maxRound = BaseConfig.getInstance().MAXROUND;
	private int curTurnEndTime = 0;//秒
	private CardUtils cardUtils = new CardUtils();
	private User curTurnUser;
	private int roundCountDown = 0;
	private final int ROUNDMAXTIME = 8000;
	private final int RESULTTIME = 5;
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
			NotificationCenter.getInstance().addObserver(this, "operateCardReq", NotificationTag.Notif_OperateCard.value, null);
		}else if(2 == userMap.size()){
			TimerManager.getInstance().unScheduleByKey(this.getClass().getName()+"dealCard");
			dealCard();			
		}
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
		if (userMap.size()<2) {
			return;
		}
		cardUtils.reSetcards();
		for (User user : userMap.values()) {
			user.getDeskUserData().reSet();
			user.getDeskUserData().setCards(cardUtils.getRandomCards());
			ProtoDesk.getInstance().notifyDealCardRes(user.getChannel(), getBanker().getUserId());
		}
		long dealCardTime = 2000;
		curTurnUser = getBanker();
		TimerManager.getInstance().scheduleOnce(this, "goNextRound",dealCardTime);
	}
	public void countDown() {
		setRoundCountDown(getRoundCountDown()-1);
		XFLog.out().println("roundCountDown = "+roundCountDown);
		if (roundCountDown<=0) {
			curTurnUser.getDeskUserData().setGiveUp(true);
			goNextRound();
		}
	}
	public void goNextRound(){
		curTurnUser = getNextRoundUser(curTurnUser);
		if (null == curTurnUser) {
			return;
		}
		round();
		TimerManager.getInstance().schedule(this, "countDown",1000,ROUNDMAXTIME/1000);
	}
	public void round(){
		setRoundCountDown(ROUNDMAXTIME/1000);
		for (User user : userMap.values()) {
			ProtoDesk.getInstance().notifyRoundRes(user);
		}
	}
	public void showResult(){
		TimerManager.getInstance().scheduleOnce(this, "dealCard", RESULTTIME);
		long totalPutIn = 0;
		Map<Integer, User> noGiveUpPlayingMap = getNoGiveUpPlayingMap();
		if (0 == noGiveUpPlayingMap.size()) {
			Exception e = new Exception("结算出错，没有胜利玩家");
			XFException.logException(e);
			return;
		}
		long winGold = getPutIntoTotalGold()/noGiveUpPlayingMap.size();
		for(User user : noGiveUpPlayingMap.values()){
			user.setGold(user.getGold()+winGold);
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
		if (1 == getNoGiveUpPlayingMap().size()) {//延迟100毫秒结算
			XFLog.out().println("胜利的玩家："+getNoGiveUpPlayingMap().toString());
			showResult();
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
	public void operateCardReq(Object object) {
		int status = 1;
		String	result = "";  // 字符串提示
		int userId = 0;
		int type = 1;
		int compareUserId = 0;
		int winnerUserId = 0;
		ArrayList<Integer> userCardArrayList = null;
		ArrayList<Integer> compareCardArrayList = null;

		JSONObject jsonObject = (JSONObject)(object);
		int reqUserId = 0;
		User reqUser = null;
		try {
			reqUserId = jsonObject.getInt("userId");
			reqUser = getUserMap().get(reqUserId);
			type = jsonObject.getInt("type");
			if (reqUser == null) 
				return;
		} catch (JSONException e) {
			XFException.logException(e);
		}
		if (reqUser.getUserId() != curTurnUser.getUserId()) {
			ProtoDesk.getInstance().operateCardRes(reqUser.getChannel(), 0, "不在操作时间", reqUserId, type);
			return;
		}
		String operateResult = "";
		switch (OperateCardTag.getEnumTag(type)) {
		case OperateCardFollowToEnd:
		{
			operateResult = OperateCardFollowToEnd();
			if (Const.SUCCESS != operateResult) {
				status = 0;
				result = operateResult;
			}
			break;			
		}
		case OperateCardGiveUp:
		{
			curTurnUser.getDeskUserData().setGiveUp(true);
			goNextRound();
			break;	
		}
		case OperateCardCompare:
			
			break;
		case OperateCardSee:
		{
			userCardArrayList = curTurnUser.getDeskUserData().getCards();
			break;			
		}
		case OperateCardFollow:
		{
			boolean isSeeCard = curTurnUser.getDeskUserData().isSeeCard();
			long needGold = isSeeCard ? 2*getSinglePutIntoGold() : getSinglePutIntoGold();
			if (curTurnUser.getGold() >= needGold) {
				curTurnUser.setGold(curTurnUser.getGold()-needGold);
				setPutIntoTotalGold(getPutIntoTotalGold()+needGold);
				
			}else {
				status = 0;
				result = "金币不足";
			}
			goNextRound();
			break; 
		}
		case OperateCardRise:
			
			break;
		case OperateCardAllIn:
			
			break;
		default:
			break;
		}
		ProtoDesk.getInstance().operateCardRes(reqUser.getChannel(), 0, "不在操作时间", reqUserId, type
				,compareUserId,winnerUserId,userCardArrayList,compareCardArrayList);
	}
	String OperateCardFollowToEnd(){
		goNextRound();
		return Const.SUCCESS;
	}
	
	
	
	
	
	
	
	
	
	public long getPutIntoTotalGold() {
		return putIntoTotalGold;
	}
	public void setPutIntoTotalGold(long putIntoTotalGold) {
		this.putIntoTotalGold = putIntoTotalGold;
	}
	public long getSinglePutIntoGold() {
		return singlePutIntoGold;
	}
	public void setSinglePutIntoGold(long singlePutIntoGold) {
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
