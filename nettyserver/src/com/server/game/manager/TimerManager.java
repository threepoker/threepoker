package com.server.game.manager;

import io.netty.util.internal.chmv8.ConcurrentHashMapV8.Fun;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.server.Utils.XFException;
import com.server.Utils.XFLog;
import com.server.game.classic.Desk;

public class TimerManager {
	private static TimerManager instance = null;
	private Map<String, MyTask> taskMap = new HashMap<String,MyTask>();
	public static TimerManager getInstance(){
		if (null == instance) {
			instance = new TimerManager();
		}
		return instance;
	}
	public void scheduleOnce(Object object,String selector,long delay){
		String key = object.getClass().getName()+selector;
		unScheduleByKey(key);
        MyTask myTask=new MyTask(object,selector,0,0,delay);  
        taskMap.put(key, myTask);
	}
	public void schedule(Object object,String selector,long delay,int repeat){
		String key = object.getClass().getName()+selector;
		unScheduleByKey(key);
        MyTask myTask=new MyTask(object,selector,1000,repeat,delay);  
        taskMap.put(key, myTask);
	}
	public void schedule(Object object,String selector,long delay,int repeat,long interval){
		String key = object.getClass().getName()+selector;
		unScheduleByKey(key);
        MyTask myTask=new MyTask(object,selector,interval,repeat,delay);  
        taskMap.put(key, myTask);
	}
	public void unScheduleByKey(String key){
		if (null != taskMap.get(key)) {
			MyTask oldTask = taskMap.get(key);
			oldTask.cancel();
			taskMap.remove(key);
		}
	}
	

	private class MyTask extends TimerTask {  
		private Object object;
		private String functionString;
		private long interval;
		private int repeat;
		private long delay;
		private String taskKey;
		public MyTask(Object object,String functionString,long interval,int repeat,long delay) {
			// TODO Auto-generated constructor stub
			this.object  = object;
			this.functionString = functionString;
			this.interval = interval;
			this.repeat = repeat;
			this.delay = delay;
			taskKey = object.getClass().getName()+functionString;
			interval = interval<0 ? 0 : interval;
			repeat = repeat<0 ? 0 : repeat;
			delay = delay<=0 ? 1 : delay;
			Timer  timer=new Timer();
			if (interval>0) {
				//安排指定的任务从指定的延迟后开始进行重复的固定延迟执行。
		        timer.schedule(this,delay,interval);				
			}else {
				//安排在指定延迟后执行指定的任务。 
				timer.schedule(this, delay);
			}
		}
	    @Override  
	    public void run() {          
	    	try {
	    		repeat -= 1;
	    		if (repeat<=0) {
	    			TimerManager.getInstance().unScheduleByKey(taskKey);
				}
		    	Class ownerClass = object.getClass();
				Method method = ownerClass.getDeclaredMethod(functionString);
				method.setAccessible(true);
				method.invoke(object);
		
			} catch (NoSuchMethodException 
					|IllegalAccessException 
					| IllegalArgumentException
					| SecurityException
					| InvocationTargetException e
					) {
				XFException.logException(e);
			} 
	    	
	    }
	}  
}
