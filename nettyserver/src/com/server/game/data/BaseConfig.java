package com.server.game.data;

import java.util.HashMap;
import java.util.Map;

public class BaseConfig {
	private static BaseConfig instance=null;
	public int NEWUSERGOLD = 0;
	public int NEWUSERDIAMOND = 0;
	//牌型概率
	public float GLDL = 0;
	public float GLBZ = 0;
	public float GLSJ = 0;
	public float GLJH = 0;
	public float GLSZ = 0;
	public float GLDZ = 0;
	public float GLGP = 0;
	public int MAXROUND = 0;
	public Map<Integer, ConfigDeskChip> configChips = new HashMap<Integer, ConfigDeskChip>();
	public static BaseConfig getInstance(){
		if (null==instance) {
			instance = new BaseConfig();
		}
		return instance;
	}
	
}
