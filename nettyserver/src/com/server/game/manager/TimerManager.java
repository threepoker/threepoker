package com.server.game.manager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.TimerTask;

import com.server.game.classic.Desk;

public class TimerManager {
	private static TimerManager instance = null;
	public static TimerManager getInstance(){
		if (null == instance) {
			instance = new TimerManager();
		}
		return instance;
	}
	public void schudele(Object object,String selcetor,float interval){
		
	}
	
	

	private class MyTask extends TimerTask {  
		private Object object;
		private String myFunction;
		public MyTask(Object object,String function) {
			// TODO Auto-generated constructor stub
			setObject(object);
			setMyFunction(function);
		}
	    @Override  
	    public void run() {          
	    	try {
		    	Class ownerClass = object.getClass();
				Method method = ownerClass.getMethod(myFunction);
				method.invoke(ownerClass);
		
			} catch (NoSuchMethodException 
					|IllegalAccessException 
					| IllegalArgumentException
					| SecurityException
					| InvocationTargetException e
					) {
				e.printStackTrace();
			} 
	    	
	    }
		public Object getObject() {
			return object;
		}
		public void setObject(Object object) {
			this.object = object;
		}
		public String getMyFunction() {
			return myFunction;
		}
		public void setMyFunction(String myFunction) {
			this.myFunction = myFunction;
		}   
	}  
}
