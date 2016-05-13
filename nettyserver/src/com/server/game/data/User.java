package com.server.game.data;

import io.netty.channel.Channel;

public class User {
	private int userId;
	private Channel channel;
	private String userName;
	private long gold = 0;
	private int diamond = 0;
	private int pos;
	private String head;
	public User(int userId){
		this.userId = userId;
	}
	public int getUserId() {
		return userId;
	}
	
	
	
	
	
	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	public long getGold() {
		return gold;
	}
	public void setGold(long gold) {
		this.gold = gold;
	}
	public int getDiamond() {
		return diamond;
	}
	public void setDiamond(int diamond) {
		this.diamond = diamond;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public boolean isActive() {
		return channel.isActive();
	}
}
