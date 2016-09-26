package com.petm.property.enums;

public enum EnumWorkOrderStatus {

	NULL(0, "空数据"), 
	CREATED(1, "已创建"), 
	CANCELED(2, "已取消"),
	COMPLETED(3, "已完成");

	private int type;
	private String desc;

	EnumWorkOrderStatus(int type, String desc) {
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
