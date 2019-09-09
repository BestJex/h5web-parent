package com.xmbl.h5.web.game.entity.tree.base;

import lombok.Data;

@Data
public class PbTGStageInfo {
	
	private long StageId;//关卡的id
	private String  Name;//关卡的名称
	private int StageVictoryType;//关卡类型
	private boolean SingelModel;//关卡模式
	private int WinType;	  //胜利条件示意图0.无、1.传染、2.毛毛虫、3.经典
	private boolean IsSubmit;	  //是否上传过挑战商店
	private boolean IsOfficial;	  //是否是官方关卡
	private boolean IsTimeLimit;	  //是否限时关卡
	private int Downloads;	  //下载量	
	private String TextureUrl;	  //封面标示
	private int RuleType;	  //游戏规则 消除 收集
//	optional PbPlayer       Author				=  12;	  //作者
//	optional bool  			IsFromPkShop		=  13;
//	optional bool 			IsSubmitShop	 	=  14;    //是否勾选了上传挑战商店
//	optional bool 			IsSubmitPKShop	 	=  15;    //是否勾选了上传PK商店
//	optional bool 			IsSubmitedShop		=  16;	  //是否已经上传到挑战商店
//	optional bool    	  	IsSubmitedPkShop	=  17;	  //是否已经上传到PK商店
//	optional bool			IsMatchCondition    =  18;	  //符合条件
//	optional int32 			Like				=  19;	  //点赞
//	optional int32 			OpenCount			=  20;	  //打开次数
//	optional int32			PassRate			=  21;    //通关率
//	optional int32			PlayState 			=  22;    //过关状态,0没玩过1通关2玩过没通关
//	optional int32 			PermitCreateCompetition	= 23;	//允许其他玩家点赞收藏后开启竞技比赛

}
