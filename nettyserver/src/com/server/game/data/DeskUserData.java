//用户在游戏中的数据
package com.server.game.data;

import java.util.ArrayList;

public class DeskUserData {
	private int userId;
	private int putInGold = 0;
	private boolean isSeeCard = false;
	private boolean isGiveUp = false;
	private boolean isPlaying = false;
	private boolean isBanker = false;
	private int pos = 0;
	private ArrayList<Integer> cards;
	
	public void reSet(){
		setPutInGold(0);
		setSeeCard(false);
		setGiveUp(false);
		setPlaying(true);
	}
	
	public DeskUserData(int userId) {
		this.userId = userId;
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
}
