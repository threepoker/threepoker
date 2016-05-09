package com.server.game;

import io.netty.channel.Channel;

public class User {
	private int userId;
	private Channel channel; 
	private boolean isActive = true;
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
	public boolean isActive() {
		return isActive;
	}
	private void readDataFromDb() {
		
	}
	public void writeDataToDb() {
		
	}
	public void exit(){
		this.isActive = false;
	}
}
