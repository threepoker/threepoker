package com.server.Utils;

import org.apache.log4j.Logger;

public class XFStack {
	private static XFStack instance=null;
	public static XFStack out(){
		if (null == instance) {
			instance = new XFStack();
		}
		return instance;
	}
	public static void logStack(Exception e){
		e.printStackTrace();
		XFLog.out().logError("stack begin");
		StackTraceElement[] stackElements = e.getStackTrace(); 
		for (int i = 0; i < stackElements.length; i++) {
			String info = stackElements[i].getClassName()+"\t" 
					+ stackElements[i].getFileName()+"\t"
					+ stackElements[i].getLineNumber()+"\t"
					+ stackElements[i].getMethodName();
            XFLog.out().logError(info);
        }
		XFLog.out().logError("stack end");
	}
}
