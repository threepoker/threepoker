package com.server.netty.common;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
public class ChannelManager {
	private static HashMap<String,Channel> map=new HashMap<String,Channel>();
	public static void add(String userId,Channel channel){
		map.put(userId, channel);
	}
	public static void remove(String userId){
		if (map.containsKey("userId")) {
			map.remove(userId);			
		}
	}
	public static void remove(Channel channel){
		if(map.containsValue(channel)){
			Iterator<Map.Entry<String, Channel>> ite = map.entrySet().iterator();
		    while(ite.hasNext()){
				Map.Entry<String, Channel> entry=ite.next();  
				Channel channel2=entry.getValue();
				if(channel == channel2){
					ite.remove();
					break;
				}
		    }
		}
	}
	public static Channel getChannel(String userId){
		if (map.containsKey(userId)) {
			if (map.get(userId).isActive()) {
				return map.get(userId);				
			}else{
				map.remove(userId);
			}
		}
		return null;		
	} 
}
