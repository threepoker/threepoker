package com.server.game.manager;

import io.netty.channel.Channel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import com.server.Utils.XFException;
import com.server.db.TableConfigDeskChipHandle;
import com.server.db.TableUserFortuneHandle;
import com.server.db.TableUserHandle;
import com.server.game.data.ConfigDeskChip;
import com.server.game.data.User;

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
		try {
			User user = new User(userId);
			ResultSet rSet = TableUserFortuneHandle.getInstance().select("userId",userId);
			if (null!=rSet && rSet.next()) {
				user.setGold(rSet.getLong(rSet.findColumn("gold")));
				user.setDiamond(rSet.getInt(rSet.findColumn("diamond")));
			}
			rSet = TableUserHandle.getInstance().selectUser("userId", userId);
			if (null!=rSet && rSet.next()) {
				user.setNickName(rSet.getString(rSet.findColumn("nickName")));
				user.setHead(rSet.getString(rSet.findColumn("head")));
			}
			remove(user.getUserId());
			user.setChannel(channel);
			userList.add(user);
		} catch (SQLException e) {
			XFException.logException(e);
		}
	}
	public void remove(Channel channel){
		Iterator<User> it = userList.iterator();
        while(it.hasNext()){
        	User iterUser = it.next();
            if (channel == iterUser.getChannel()) {
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
	public User getUser(Channel channel){
		Iterator<User> it = userList.iterator();
        while(it.hasNext()){
        	User iterUser = it.next();
            if (channel == iterUser.getChannel()) {
				return iterUser;
			}
        }
        return null;
	}
	public User getUser(int userId){
		Iterator<User> it = userList.iterator();
        while(it.hasNext()){
        	User iterUser = it.next();
            if (userId == iterUser.getUserId()) {
				return iterUser;
			}
        }
        return null;
	}
}
