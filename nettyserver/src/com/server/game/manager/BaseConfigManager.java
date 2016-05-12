package com.server.game.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

import com.server.db.TableCommonHandle;
import com.server.db.TableConfigDeskChipHandle;
import com.server.db.TableConfigDeskHandle;
import com.server.db.TableConfigFragHandle;
import com.server.game.data.BaseConfig;
import com.server.game.data.ConfigDeskChip;

public class BaseConfigManager {
	private static BaseConfigManager instance=null;
	public static BaseConfigManager getInstance(){
		if (null==instance) {
			instance = new BaseConfigManager();
		}
		return instance;
	}
	public void loadAllConfig() throws SQLException{
		parseConfigFrag();
		parseConfigDesk();
		parseConfigDeckChip();
	}
	//////////////////////parseConfig////////////////////////////////////////////
	private void parseConfigFrag() throws SQLException{
		ResultSet rSet = TableConfigFragHandle.getInstance().select();
		if (null!=rSet && rSet.next()) {
			BaseConfig.getInstance().NEWUSERGOLD = rSet.getInt(rSet.findColumn("newUserGold"));
		}
	}
	private void parseConfigDesk() throws SQLException{
		ResultSet rSet = TableConfigDeskHandle.getInstance().select();
		if (null!=rSet && rSet.next()) {
			BaseConfig.getInstance().GLDL = rSet.getFloat(rSet.findColumn("dl"));
			BaseConfig.getInstance().GLBZ = rSet.getFloat(rSet.findColumn("bz"));
			BaseConfig.getInstance().GLSJ = rSet.getFloat(rSet.findColumn("sj"));
			BaseConfig.getInstance().GLJH = rSet.getFloat(rSet.findColumn("jh"));
			BaseConfig.getInstance().GLSZ = rSet.getFloat(rSet.findColumn("sz"));
			BaseConfig.getInstance().GLDZ = rSet.getFloat(rSet.findColumn("dz"));
			BaseConfig.getInstance().GLGP = rSet.getFloat(rSet.findColumn("gp"));
			BaseConfig.getInstance().MAXROUND = rSet.getInt(rSet.findColumn("maxRound"));
		}
	}
	private void parseConfigDeckChip() throws SQLException{
		ResultSet rSet = TableConfigDeskChipHandle.getInstance().select();
		while (null!=rSet && rSet.next()) {
			ConfigDeskChip configDeskChip = new ConfigDeskChip();
			configDeskChip.setLevel(rSet.getInt(rSet.findColumn("level")));
			configDeskChip.setName(rSet.getString(rSet.findColumn("name")));
			configDeskChip.setEnterMin(rSet.getInt(rSet.findColumn("enterMin")));
			configDeskChip.setEnterMax(rSet.getInt(rSet.findColumn("enterMax")));
			configDeskChip.setDefaultBet(rSet.getInt(rSet.findColumn("defaultBet")));
			String chipStrings = rSet.getString(rSet.findColumn("chip"));
			String[] chipStrigArray=chipStrings.split(",");			
			for (String chipString : chipStrigArray) {
				configDeskChip.getChips().add(Integer.parseInt(chipString));
			}
		}
	}
	
	
	
	
	
	
	
	//////////////////////////////get ////////////////////////////////////////////////////
	public ConfigDeskChip getConfigDeskChip(int level){
		for (ConfigDeskChip configDeskChip : BaseConfig.getInstance().configChips.values()) {
			if (configDeskChip.getLevel() == level) {
				return configDeskChip;
			}
		}
		return null;
	}
}

