package com.server.Utils;


public enum NotificationTag {
	Notif_OperateCard(500);
	public final int value;
	private NotificationTag(int value){
		this.value = value; 
	}
	public static NotificationTag getEnumTag(int value) { 
        for (NotificationTag code : values()) { 
            if (code.value == value) 
                return code; 
        }
        return null; 
    } 
}