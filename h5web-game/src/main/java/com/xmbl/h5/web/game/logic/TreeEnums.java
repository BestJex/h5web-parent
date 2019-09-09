package com.xmbl.h5.web.game.logic;

public class TreeEnums {
	//关卡集模式   1生存模式  2故事
	public static enum StoryType {
		survive(1), story(2);
		
		public int storyType;
		private StoryType(int storyType) {
			this.storyType = storyType;
		}
		
		public static StoryType getStoryType(int storyType) {
			StoryType[] types = StoryType.values();
			for (StoryType st : types) {
				if (st.storyType == storyType) {
					return st;
				}
			}
			return null;
		}
	}
	//关卡类型 0：普通关卡  1： 关卡包
	public static enum NodeType {
		normal,pack;
		public static NodeType getNodeType(int type) {
			if (type >= NodeType.values().length) {
				return null;
			}
			return NodeType.values()[type];
		}
	}
	//路径类型 0：主线  1：支线
	public static enum PathType{
		mainline,branch
	}
	//开放方式 0：初始开放   1：顺序开放
	public static enum OpenType{
		noOrder, order
	}
	//道具获取方式： 0 ：付费获取   1：免费赠送
	public static enum PropGetType{
		pay, free
	}
	//条件限制类型 0:步数   1：时间
	public static enum ConditionType{
		step, time
	}
	// 关卡类型
	public static enum stageType{
		
	}
}
