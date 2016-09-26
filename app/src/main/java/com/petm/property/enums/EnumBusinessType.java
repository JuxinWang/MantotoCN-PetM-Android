package com.petm.property.enums;

public enum EnumBusinessType {
	NULL(0, "空数据"), 
	PET(1, "宠物店"), 
	REPAIR(2, "家庭维修");

	private int type;
	private String desc;

	EnumBusinessType(int type, String desc) {
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
