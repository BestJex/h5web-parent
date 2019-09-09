package com.xmbl.h5.web.common.logic;

public enum ERankType {
	ERROR(0), TREE(1), NODE(2), STAGE(3);

	public int type;

	private ERankType(int type) {
		this.type = type;
	}

	public static final ERankType getRankType(int type) {
		for (ERankType rankType : ERankType.values()) {
			if (type == rankType.type) {
				return rankType;
			}
		}
		return ERROR;
	}
}
