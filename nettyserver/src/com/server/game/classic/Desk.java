package com.server.game.classic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;

import com.server.game.data.User;
import com.server.game.proto.ProtoDesk;

public class Desk {
	private int level;
	private Map<Integer, User> userMap = new HashMap<Integer,User>();
	public Desk(int level) {
		this.level = level;
	}
	public void addUser(User user) throws JSONException{
		for (User userIterUser : userMap.values()) {
			ProtoDesk.getInstance().notifyEnterDeskRes(userIterUser.getChannel(),user);
		}
		int pos = getPos();
		userMap.put(pos, user);
		user.setPos(pos);
	}
	public void removeUser(User user) throws JSONException{
		userMap.remove(user.getUserId());
		for (User userIterUser : userMap.values()) {
			ProtoDesk.getInstance().notifyLeaveDeskRes(userIterUser.getChannel(),user);
		}
	}
	
	
	public int getLevel() {
		return level;
	}
	public Map<Integer, User> getUserMap() {
		return userMap;
	}
	
	/////////////////////////位置管理//////////////////////
	//一桌五个人 分：0、1、2、3、4号座位
	public int getPos(){
		int result = -1;
		for (int i = 0; i < 5; i++) {
			result = i;
			for (int pos : userMap.keySet()) {
				if (i == pos) {
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
	
}
