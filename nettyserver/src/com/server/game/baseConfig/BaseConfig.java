package com.server.game.baseConfig;

public class BaseConfig {
	private static BaseConfig instance=null;
	public int newUserGold;
	public int newUserDiaomon = 0;
	public static BaseConfig getInstance(){
		if (null==instance) {
			instance = new BaseConfig();
		}
		return instance;
	}
	
}
