package com.server.Utils;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.PropertyConfigurator;

import com.server.game.classic.Desk;
import com.server.game.manager.DeskManager;

public class XFLog {
	private static Logger logger=null;
	private static XFLog instance=null;
	public static XFLog out(){
		if (null == instance) {
			instance = new XFLog();
			//PropertyConfigurator.configure("log4j.properties");//加载.properties文件
			logger=Logger.getLogger(XFLog.class); 
		}
		return instance;
	}
	public void println(String s){
		System.out.println(s);
	}
	public void println(Boolean b){
		System.out.println(b);
	}
	public void println(int i){
		System.out.println();
	}
	public void println(long l){
		System.out.println(l);
	}
	public void println(Object o){
		System.out.print(o);
	}
	public void logError(String message) {
		logger.error(message);
	}
}
