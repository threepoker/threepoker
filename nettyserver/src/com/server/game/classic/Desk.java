package com.server.game.classic;

import java.util.HashMap;
import java.util.Map;

import com.server.game.data.User;

public class Desk {
	private int level;
	private Map<Integer, User> userMap = new HashMap<Integer,User>();
	public Desk(int level) {
		this.level = level;
	}
	public void addUser(User user){
		userMap.put(user.getUserId(), user);
	}
	public void removeUser(User user){
		userMap.remove(user.getUserId());
		
	}
	
	
	public int getLevel() {
		return level;
	}
	public Map<Integer, User> getUserMap() {
		return userMap;
	}
	
	
}
