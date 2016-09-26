package com.petm.property.enums;

public enum EnumGiftStatus {

	NULL(0, "无状态"), 
	NOMAL(1, "正常"), 
	LOCKED(2, "已冻结"), 
	EXPIRED(3, "已过期");

	private int type;
	private String desc;

	EnumGiftStatus(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public int getType() {
		return type;
	}

	public String getDesc() {
		return this.desc;
	}
}
