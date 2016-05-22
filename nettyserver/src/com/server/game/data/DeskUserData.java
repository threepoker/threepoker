//用户在游戏中的数据
package com.server.game.data;

import java.util.ArrayList;

import com.server.game.classic.Desk;
import com.server.game.manager.BaseConfigManager;

public class DeskUserData {
	private int userId;
	private int putInGold = 0;
	private boolean isSeeCard = false;
	private boolean isGiveUp = false;
	private boolean isPlaying = false;
	private boolean isBanker = false;
	private int pos = 0;
	private Desk desk = null;
	private ArrayList<Integer> cards;
	
	public void gameBegin(){
		setPutInGold(0);
		setSeeCard(false);
		setGiveUp(false);
		setPlaying(true);
	}	
	public DeskUserData(int userId,Desk desk) {
		this.userId = userId;
		this.desk = desk;
	}
	public boolean isShowFollowToEnd(){
		return !isGiveUp;
	}
	public boolean isShowGiveUp(){
		return !isGiveUp;
	}
	public boolean isShowCompare(){
		return (userId==desk.getCurTurnUserId())&&(desk.getCurrentRound()>=5 || desk.getNoGiveUpPlayingMap().size()==2);
	}
	public boolean isShowSeeCard() {
		return (userId==desk.getCurTurnUserId())&&!isSeeCard;
	}
	public boolean isShowFollow() {
		User user = desk.getUserByUserId(userId);
		int rate = isSeeCard ? 2 : 1;
		if ((userId==desk.getCurTurnUserId())&& user.getGold() >= 3*rate*desk.getSinglePutIntoGold()) {
			return true;
		}
		return false;
	}
	public boolean isShowRise(){
		User user = desk.getUserByUserId(userId);
		int rate = isSeeCard ? 2 : 1;
		if ((userId==desk.getCurTurnUserId())&& user.getGold() >= 3*rate*BaseConfigManager.getInstance().getConfigDeskChip(desk.getLevel()).getMaxChip()) {
			return true;
		}
		return false;
	}
	
	public int getUserId() {
		return userId;
	}
	public int getPutInGold() {
		return putInGold;
	}
	public void setPutInGold(int putInGold) {
		this.putInGold = putInGold;
	}
	public boolean isSeeCard() {
		return isSeeCard;
	}
	public void setSeeCard(boolean isSeeCard) {
		this.isSeeCard = isSeeCard;
	}
	public boolean isGiveUp() {
		return isGiveUp;
	}
	public void setGiveUp(boolean isGiveUp) {
		this.isGiveUp = isGiveUp;
	}
	public boolean isPlaying() {
		return isPlaying;
	}
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	public ArrayList<Integer> getCards() {
		return cards;
	}
	public void setCards(ArrayList<Integer> cards) {
		this.cards = cards;
	}
	public boolean isBanker() {
		return isBanker;
	}
	public void setBanker(boolean isBanker) {
		this.isBanker = isBanker;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public Desk getDesk() {
		return desk;
	}

}
