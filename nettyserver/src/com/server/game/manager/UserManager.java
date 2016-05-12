package com.server.game.manager;

import io.netty.channel.Channel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import com.server.db.TableConfigDeskChipHandle;
import com.server.db.TableUserFortuneHandle;
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
	public void add(int userId,Channel channel) throws NumberFormatException, SQLException{
		User user = new User(userId);
		ResultSet rSet = TableUserFortuneHandle.getInstance().select("userId",userId);
		if (null!=rSet && rSet.next()) {
			user.setGold(rSet.getLong(rSet.findColumn("gold")));
			user.setDiamond(rSet.getInt(rSet.findColumn("diamond")));
		}
		remove(user.getUserId());
		user.setChannel(channel);
		userList.add(user);
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
}
