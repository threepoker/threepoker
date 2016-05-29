package com.server.game.proto;

public enum ProtoTag {
	proto_login(100),
	proto_getUserInfo(101),
	proto_enterDesk(102),
	proto_exitDesk(103),
	proto_notifyEnterDesk(104),
	proto_notifyExitDesk(105),
	proto_getDeskInfo(106),
	proto_updateGold(107),
	proto_notifyDealCard(108),
	proto_notifyRound(109),
	proto_operateCard(110);
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
