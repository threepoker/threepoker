package com.server.Utils;

public class XFException {
	public static void logException(Exception e){
		XFStack.logStack(e);
	}
}
