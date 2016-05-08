package com.server.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import java.util.Iterator;



public class NotificationCenter {
	private static NotificationCenter instance = null;
	private static ArrayList<MyEvent> arrayList =new ArrayList<MyEvent>();
	class MyEvent{
		public Object target = null;
		public String methordName = "";
		public String notificationName = "";
		public Object sender = null;
		public MyEvent(Object target,String methordName,String notificationName,Object sender) {
			this.target = target;
			this.methordName = methordName;
			this.notificationName = notificationName;
			this.sender = sender;
		}
	}
	public static NotificationCenter getInstance() {
		if (instance == null) {
			instance = new NotificationCenter();
		}
		return instance;
	}
	public void addObserver(Object target,String methordName,String notificationName,Object sender){
		if (null==notificationName || notificationName.length()==0 || null==methordName || methordName.length()==0) {
			return;
		}
		MyEvent myEvent = new MyEvent(target,methordName,notificationName,sender);
		arrayList.add(myEvent);
	}
	public void postNotification(String notificationName,Object object) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Iterator<MyEvent> it = arrayList.iterator();
	    while(it.hasNext()){
			MyEvent event = it.next();  
			
			try {
				Method method = event.target.getClass().getDeclaredMethod(event.methordName,Object.class);
				if (null != object) {
					method.invoke(event.target, object);					
				}else {
					method.invoke(event.target, event.sender);
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
	    }
	}
	public void removeObserver(String notificationName){
		for (int i = 0; i < arrayList.size(); i++) {
			MyEvent myEvent = arrayList.get(i);
			if (notificationName == myEvent.notificationName) {
				arrayList.remove(i--);
			}
		}
	}
}
