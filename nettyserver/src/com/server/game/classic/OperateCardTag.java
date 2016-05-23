package com.server.game.classic;
public enum OperateCardTag {
	OperateCardFollowToEnd(1),
	OperateCardGiveUp(2),
	OperateCardCompare(3),
	OperateCardSee(4),
	OperateCardFollow(5),
	OperateCardRise(6),
	OperateCardAllIn(7);
	public final int value;
	private OperateCardTag(int value){
		this.value = value; 
	}
	public static OperateCardTag getEnumTag(int value) { 
        for (OperateCardTag code : values()) { 
            if (code.value == value) 
                return code; 
        }
        return null; 
    } 
}