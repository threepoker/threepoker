package com.server.game.baseConfig;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.server.db.TableCommonHandle;
import com.server.db.TableConfigFragHandle;

public class BaseConfigManager {
	private static BaseConfigManager instance=null;
	public static BaseConfigManager getInstance(){
		if (null==instance) {
			instance = new BaseConfigManager();
		}
		return instance;
	}
	public void loadAllConfig() throws SQLException{
		loadConfigFrag();
	}
	private void loadConfigFrag() throws SQLException{
		ResultSet rSet = TableConfigFragHandle.getInstance().select();
		if (null!=rSet && rSet.next()) {
			BaseConfig.getInstance().newUserGold = rSet.getInt(rSet.findColumn("newUserGold"));
		}
	}
}
