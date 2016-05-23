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
		public int notificationTag = 0;
		public Object sender = null;
		public MyEvent(Object target,String methordName,int notificationTag,Object sender) {
			this.target = target;
			this.methordName = methordName;
			this.notificationTag = notificationTag;
			this.sender = sender;
		}
	}
	public static NotificationCenter getInstance() {
		if (instance == null) {
			instance = new NotificationCenter();
		}
		return instance;
	}
	public void addObserver(Object target,String methordName,int notificationTag,Object sender){
		if (null==methordName || methordName.length()==0) {
			return;
		}
		removeObserverFunction(target, methordName);
		MyEvent myEvent = new MyEvent(target,methordName,notificationTag,sender);
		arrayList.add(myEvent);
	}
	public void postNotification(int notificationTag,Object object) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		ArrayList<MyEvent> iterArrayList = new ArrayList<MyEvent>(arrayList);
		Iterator<MyEvent> it = iterArrayList.iterator();
	    while(it.hasNext()){
			MyEvent event = it.next();  
			if (notificationTag==event.notificationTag) {
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
	}
	public void removeObserverTag(int notificationTag){
		for (int i = 0; i < arrayList.size(); i++) {
			MyEvent myEvent = arrayList.get(i);
			if (notificationTag == myEvent.notificationTag) {
				arrayList.remove(i--);
			}
		}
	}
	public void removeObserverObject(Object object){
		for (int i = 0; i < arrayList.size(); i++) {
			MyEvent myEvent = arrayList.get(i);
			if (object == myEvent.target) {
				arrayList.remove(i--);
			}
		}
	}
	public void removeObserverFunction(Object object,String methordName){
		for (int i = 0; i < arrayList.size(); i++) {
			MyEvent myEvent = arrayList.get(i);
			if (object == myEvent.target && methordName==myEvent.methordName) {
				arrayList.remove(i);
				break;
			}
		}
	}
}
