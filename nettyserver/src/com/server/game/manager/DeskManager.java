package com.server.game.manager;

import java.util.ArrayList;

import org.json.JSONException;

import com.server.game.classic.Desk;
import com.server.game.common.Const;
import com.server.game.data.User;
public class DeskManager {
	private static DeskManager instance = null;
	private ArrayList<Desk> deskList = new ArrayList<>();
	public static DeskManager getInstance(){
		if (null == instance) {
			instance = new DeskManager();
		}
		return instance;
	}
	public String enterDesk(User user,int level) throws JSONException{
		if(null == BaseConfigManager.getInstance().getConfigDeskChip(level)){
			return "获取牌桌失败";
		}
		if (user.getGold()<BaseConfigManager.getInstance().getConfigDeskChip(level).getEnterMin()) {
			return "金币不足";
		}
		for (Desk desk : deskList) {
			if (desk.getLevel()==level && desk.getUserMap().size()<5) {
				desk.addUser(user);
				break;
			}
		}
		return Const.SUCCESS;
	}
	public String exitDesk(User user) throws JSONException{
		for (Desk desk : deskList) {
			if (desk.getUserMap().get(user.getUserId()) != null) {
				desk.removeUser(user);
				break;
			}
		}
		return Const.SUCCESS;
	}
	private Desk getNewDesk(int level) {
		Desk desk = new Desk(level);
		deskList.add(desk);
		return desk;
	}
	public Desk getDesk(int userId){
		for (Desk desk : deskList) {
			for (User userIterUser : desk.getUserMap().values()) {
				if (userId == userIterUser.getUserId()) {
					return desk;
				}
			}
		}
		return null;
	}
}
