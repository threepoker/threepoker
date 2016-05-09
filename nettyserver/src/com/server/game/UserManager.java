package com.server.game;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.Iterator;

public class UserManager {
	private static UserManager instance = null;
	private ArrayList<User> userList = new ArrayList<>();
	public static UserManager getInstance(){
		if (null == instance) {
			instance = new UserManager();
		}
		return instance;
	}
	public void add(int userId,Channel channel){
		User user = new User(userId);
		remove(user.getUserId());
		user.setChannel(channel);
		userList.add(user);
	}
	public void remove(Channel channel){
		Iterator<User> it = userList.iterator();
        while(it.hasNext()){
        	User iterUser = it.next();
            if (channel == iterUser.getChannel()) {
            	iterUser.exit();
				userList.remove(iterUser);
				break;
			}
        }
	}
	public void remove(int userId){
		Iterator<User> it = userList.iterator();
        while(it.hasNext()){
        	User iterUser = it.next();
            if (userId == iterUser.getUserId()) {
            	iterUser.exit();
				userList.remove(iterUser);
				break;
			}
        }
	}
	public Channel getChannel(int userId){
		Iterator<User> it = userList.iterator();
        while(it.hasNext()){
        	User iterUser = it.next();
            if (userId == iterUser.getUserId()) {
            	return iterUser.getChannel();
			}
        }
        return null;
	}
}
