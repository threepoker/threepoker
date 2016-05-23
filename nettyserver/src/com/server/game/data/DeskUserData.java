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
	private final int COMPARETURNLIMIT = 1;//至少一轮才能比牌
	private final int ALLINTURNLIMIT = 5;//至少五轮才能比牌
	private final int FOLLORATEWLIMIT = 3;//
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
		User user = desk.getUserByUserId(userId);
		int rate = isSeeCard ? 2 : 1;
		if (user.getGold() >= FOLLORATEWLIMIT*rate*desk.getSinglePutIntoGold()) {
			return true;
		}
		return false;
	}
	public boolean isShowGiveUp(){
		return !isGiveUp;
	}
	public boolean isShowCompare(){
		return (userId==desk.getCurTurnUserId())&&(desk.getCurrentRound()>COMPARETURNLIMIT);
	}
	public boolean isShowSeeCard() {
		return !isSeeCard;
	}
	public boolean isShowFollow() {
		User user = desk.getUserByUserId(userId);
		int rate = isSeeCard ? 2 : 1;
		if ((userId==desk.getCurTurnUserId())&& user.getGold() >= FOLLORATEWLIMIT*rate*desk.getSinglePutIntoGold()) {
			return true;
		}
		return false;
	}
	public boolean isShowRise(){
		User user = desk.getUserByUserId(userId);
		int rate = isSeeCard ? 2 : 1;
		if ((userId==desk.getCurTurnUserId())
				&& user.getGold() >= FOLLORATEWLIMIT*rate*BaseConfigManager.getInstance().getConfigDeskChip(desk.getLevel()).getMaxChip()
				&& desk.getSinglePutIntoGold() != BaseConfigManager.getInstance().getConfigDeskChip(desk.getLevel()).getMaxChip()) {
			return true;
		}
		return false;
	}
	public boolean isShowAllIn(){
		if (desk.getCurrentRound() > ALLINTURNLIMIT) {
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
