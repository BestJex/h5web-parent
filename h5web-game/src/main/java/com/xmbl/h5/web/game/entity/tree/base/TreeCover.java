package com.xmbl.h5.web.game.entity.tree.base;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.xmbl.h5.web.game.consts.MongoDBConst;
import  com.xmbl.h5.web.game.entity.tree.base.StageTreeShop.Author;

import lombok.Data;

@Data
@Document(collection = MongoDBConst.stageTreeShop)
public class TreeCover {
	
	private Long treeId;
	private String name;
	private String textureUrl;
	private String dec;
	private String voiceUrl;
	private Long voiceTime;
	private Boolean isCanCreateBattle;
	private Integer maxEnergy;
	private Integer pce;
	private Integer eri;
	
	@Field("shareGetEnergy")
	private Integer shareEnergy;
	private Integer buyEnergyCost;
	private Integer buyEnergyCostType;
	private Boolean isEnergyRecoveryInterval;
	private Boolean isShareGetEnergy;
	private Integer storyType;
	private Boolean isConditionLimit;
	private Integer conditionType;
	private Integer conditionValue;
	private Boolean isElementLimit1;
	private Boolean isElementLimit2;
	private Integer serial;
	private Long headNodeId;
	private Long createTick;
	private Long submitTick;
	private Long updateTick;
	private Integer isShow;
	private Integer praise;
	private List<Object> aLimitTargets1;
	private List<Object> aLimitTargets2;
	private List<Object> bLimitTargets1;
	private List<Object> bLimitTargets2;
	private Integer propGetType;
	private Boolean isUsePropLimit;
//	private List<Object> Props;
//	private List<Node> nodes;
	private Author author;

}
