//用户在游戏中的数据
package com.server.game.data;

public class DeskUserData {
	private int userId;
	private int putInGold = 0;
	private boolean isSeeCard = false;
	private boolean isGiveUp = false;
	private boolean isPlaying = false;
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
}
