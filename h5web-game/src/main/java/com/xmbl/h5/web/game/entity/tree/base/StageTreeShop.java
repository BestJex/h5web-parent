package com.xmbl.h5.web.game.entity.tree.base;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.alibaba.fastjson.annotation.JSONField;
import com.xmbl.h5.web.game.consts.MongoDBConst;
import com.xmbl.h5.web.game.entity.tree.base.StageTreeRes.PbTaskTarget;

import lombok.Data;

@Data
@Document(collection = MongoDBConst.stageTreeShop)
public class StageTreeShop {

	public Boolean isShareGetEnergy() {
		return isShareGetEnergy;
	}

	public void setShareGetEnergy(Boolean isShareGetEnergy) {
		this.isShareGetEnergy = isShareGetEnergy;
	}

	private String _id;
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
	private Integer shareGetEnergy;
	private Integer buyEnergyCost;
	private Integer buyEnergyCostType;
	private Boolean isEnergyRecoveryIntegererval;
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
	private List<PbTaskTarget> aLimitTargets1;
	private List<PbTaskTarget> aLimitTargets2;
	private List<PbTaskTarget> bLimitTargets1;
	private List<PbTaskTarget> bLimitTargets2;
	private Integer propGetType;
	private Boolean isUsePropLimit;
	private List<Object> Props;
	private List<Node> Nodes;
	private Author author;

	@Data
	public static class Node {
		private Long nodeId;
		private Long index;
		private Integer nodeType = 0;
		private Integer pathType;
		private Integer pce;
		private String nodeName;
		private Integer openType = 0;
		private Integer passCondition;
		private Long priorId;
		private Long nextId;
		private Long branchId;
		@JSONField(name = "isNew")
		private int isNew;
		@JSONField(name = "isSubmited")
		private int isSubmited;
		private String dec;
		private List<Panel> panels;
	}

	@Data
	public static class Panel {
		private Long index;
		private Long stageId;
		private Integer stageType;
		private String stageTexurl;
		private String stageName;
		private String authorName;
		private Long authorId;
		private String authorAvatar;
		private String plotName;
		private String plotTexurl;
		private Long plotId;
		private Integer difficulty;
		private String tag;
		private Boolean singelModel;
		@JSONField(name = "isNew")
		private int isNew;
		@JSONField(name = "isSubmited")
		private int isSubmited;
		@JSONField(name = "isTimeLimit")
		private boolean isTimeLimit;
	}

	@Data
	public static class Plot {
		@JSONField(name = "isPlay")
		private boolean isPlay;
		private int indexBeginPlay;
		private String plotName;
		private List<DescData> descDatas;
		@JSONField(name = "isJumpOut")
		private boolean isJumpOut;
		private int plotId;
		private long descTime;
	}

	@Data
	public static class DescData {
		private String descText;
		private String voiceUrl;
		private long descPlayTime;
		private String texUrl;
		@JSONField(name = "isUseTex")
		private boolean isUseTex;
		private int recordLength;
		private int id;

	}

	@Data
	public static class Author {
		@Field("id")
		private Long authorId;
		private String name;
		private Integer level;
		private Long sid;
		private Long csId;
		private Integer ll;
		private Integer ls;
		private String avatar;
		private Boolean needGuild;
		private Integer model;
		private Integer nationality;
		private String signature;
		private Byte sex;
		private Integer age;
		private Integer constellation;
		private String city;
		private Double[] geoPos;
		private Integer[] avaters;
	}

}
