package com.server.game.proto;

public enum ProtoTag {
	PROTOLOGIN(100),
	PROTOGETUSERINFO(101),
	PROTOENTERDESK(102),
	PROTOEXITDESK(103),
	PROTONOTIFYENTERDESK(104),
	PROTONOTIFYEXITDESK(105),
	PROTOGETDESKINFO(106),
	PROTOUPDATEGOLD(107),
	PROTONOTIFYDEALCARD(108),
	PROTONOTIFYROUND(109);
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
