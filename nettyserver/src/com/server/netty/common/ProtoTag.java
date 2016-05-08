package com.server.netty.common;

public enum ProtoTag {
	LOGIN(100);
	
	public final int value;
	private ProtoTag(int value){
		this.value = value; 
	}
	public static ProtoTag getEnumTag(int value) { 
        for (ProtoTag code : values()) { 
            if (code.value == value) 
                return code; 
        }
        return null; 
    } 
}
