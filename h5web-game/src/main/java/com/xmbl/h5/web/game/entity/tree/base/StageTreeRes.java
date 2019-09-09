package com.xmbl.h5.web.game.entity.tree.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.xmbl.h5.web.game.consts.MongoDBConst;
import com.xmbl.h5.web.game.proto.MsgObject;
import com.xmbl.h5.web.game.proto.PbPlayerEntity;

import lombok.Data;

@Data
@Document(collection = MongoDBConst.stageTreeRes)
public class StageTreeRes {
	@Id
	private String _id;
	@Field("id")
	private Long resId;// stageTreeRes的id字段，spring将命名为id的field当人mongo自己生成的字段，所以改个名字
	private Long treeId;
	private Long stageAuthorId;
	private Long streeAuthorId;
	private Long createTick;
	private Long submitTick;
	private Integer auditStatus;
	private Integer isShow;
	private Boolean needSave;
	private Integer energyIncome;

	private Shield shield;
	private List<Object> slaves;
	private Stage stage;

	@Data
	public static class Shield {
		private Boolean tex;
		private Boolean motif;
		private Boolean name;
		private Boolean intro;
		private Boolean voice;
		private Boolean plot;
	}

	@Data
	public static class Stage {
		private Long stageId;
		private Integer stageType;
		private Integer ruleType;
		private String name;
		private String dec;
		private String textureUrl;
		private Integer moneyType;
		private Integer cost;
		private String voiceUrl;
		private Integer voiceTime;
		private Integer step;
		private Integer time;
		private Integer fishNum;
		private Integer linkNum;
		private Integer propNum;
		private Boolean isTargetAdd;
		private Boolean singelModel;
		private Boolean dropModel;
		private Boolean rollVictory;
		private Boolean createFish;
		private Boolean usePropInBag;
		private Boolean isAddInfectJudge;
		private Boolean isChangeedDropStage;
		private Boolean isTimeLimit;
		private Boolean isStepLimit;
		private Boolean isConditionLimit;
		private Boolean isElementLimit1;
		private Boolean isElementLimit2;
		private Boolean isSyncCollectTarget;
		private Boolean isDefaultMotif;
		private Integer timeLimitLength;
		private Long createTime;
		private Long updateTime;
		private Boolean isOfficial;
		private Boolean isUpfoldPropItems;
		private Long cId;

		private String cKey;
		private Boolean isSubmitShop;
		private Boolean isSubmitPkShop;
		private Boolean isLimitUsePropCount;
		private Long motifGroupId;
		private String motifGroupName;

		private Author author;

		private List<PbTaskTarget> targets1;
		private List<PbTaskTarget> targets2;

		private List<PbTaskTarget> adTarget1;
		private List<PbTaskTarget> adTarget2;

		private List<PbTaskTarget> bdTarget1;
		private List<PbTaskTarget> bdTarget2;

		private List<PbBlock> blocks;

		private List<PbDropBlock> dropBlocks;

		private List<Integer> dropModels;

		private List<Item> items;
		private List<PbPropChest> propChests;
		private List<PbMagicHat> magicHats;
		private List<PbRollTarget> aRollTarget;
		private List<PbRollTarget> bRollTarget;
		private List<PbRollTarget> autoRollTarget;
		private List<List<Integer>> caterpillar;
		private List<PbHidden> hiddens;
		private List<PbBelt> belts;

		private List<Integer> baseColor;

		private List<ItemMotif> itemMotifs;

		private Integer permitCreateCompetition;

	}

	// @Data
	// public static class PbPropChest {
	// private Integer idx;// 宝箱所在棋盘的下标(序号)
	// private List<Integer> propIds;// 宝箱内包含的道具ID数组
	// private List<Integer> propNums;// 宝箱内包含的道具数量数组
	// private List<Integer> propWeights;// 宝箱内包含的道具在[随机获取]模式下被获得的权重
	// private Integer hitTimes;// 宝箱被打开需要消除的次数
	// private Integer getByRamdom;// 道具是否是以随机的形式获得。true - 随机获取道具;false - 按回合顺序获取道具
	// }

	@Data
	public static class PbMagicHat {
		@Field("id")
		private Integer idx;// 魔术帽所在棋盘的下标(序号)
		private Integer hit;// 喷射需要消除次数
		private Integer outPutPropCount;// 每次喷射物品数量
		private List<Integer> elements;// 魔术帽内包含的元素ID数组
		private List<PbPropChest> propChests;// 魔术帽内包含的道具宝箱数组
		
		
	}

	@Data
	public static class PbPropChest {
		@Field("id")
		private Integer idx;// 宝箱所在棋盘的下标(序号)
		private List<Integer> propIds;// 宝箱内包含的道具ID数组
		private List<Integer> propNums;// 宝箱内包含的道具数量数组
		private List<Integer> propWeights;// 宝箱内包含的道具在[随机获取]模式下被获得的权重
		private Integer hit;// 宝箱被打开需要消除的次数
		private Boolean randomType; // 道具是否是以随机的形式获得。true - 随机获取道具;false - 按回合顺序获取道具
	}

	@Data
	public static class Item {
		@Field("id")
		private Integer itemId;
		private Integer num;
	}

	@Data
	public static class PbHidden {
		List<Integer> body;// 隐藏块的身体
		@Field("id")
		Integer blockId;// 隐藏块ID
		Integer index;// 出生点位置
	}

	@Data
	public static class PbBelt {
		private Integer enterId; // 入口的id
		private Integer exitId; // 出口的id
		private List<Integer> bodies; // 传送带身体坐标
	}

	@Data
	public static class PbRollTarget {
		private int type;
		private int rollNum;
		private int serchRow;
		private Boolean isTargetAdd;
		private List<PbTaskTarget> targets;
	}

	@Data
	public static class Author {
		@Field("id")
		private Long authorId;
		private String name;
		private Integer level;
		private Integer sid;
		private Integer csId;
		private Integer ll;
		private Integer ls;
		private String avatar;
		private Boolean needGuild;
		private Integer model;
		private Integer nationality;
		private String signature;
		private Integer sex;
		private Integer age;
		private Integer constellation;
		private String city;
		private Float[] geoPos;
		private List<Integer> avaters;
	}

	@Data
	public static class PbTaskTarget {
		private Integer target;
		private Integer num;
		private Integer cur;
	}

	@Data
	public static class PbBlock {
		private Integer id1;
		private Integer id2;
		private Integer id3;
		private Integer id4;
		private Integer id5;
		private Integer id6;
		private Integer id7;
	}

	@Data
	public static class PbDropBlock {
		private List<Integer> dropIds;
	}

	@Data
	public static class ItemMotif {
		@Field("id")
		private Integer itemMotifId;
		private String tex;
	}

	public MsgObject.PbTGStage transfer(StageTreeShop tree) {
		MsgObject.PbTGStage.Builder builder = MsgObject.PbTGStage.newBuilder();
		builder.setStageId(this.stage.stageId);
		builder.setDec(this.stage.dec);
		builder.setCost(this.stage.cost);

		PbPlayerEntity.PbPlayer.Builder pbPlayer = PbPlayerEntity.PbPlayer.newBuilder();
		pbPlayer.setAvatar(stage.author.avatar);
		pbPlayer.setAge(stage.author.age);
		pbPlayer.setCity(stage.author.city);
		pbPlayer.addAllAvaters(stage.author.avaters);
		pbPlayer.setConstellation(stage.author.constellation);
		pbPlayer.setCurServerId(stage.author.csId);
		pbPlayer.setGenLat(stage.author.geoPos[0]);
		pbPlayer.setGenLon(stage.author.geoPos[1]);
		pbPlayer.setId(stage.author.authorId);
		pbPlayer.setLadderAddStarScore(stage.author.ls);// TODO 大概是，待确认
		pbPlayer.setLadderLv(stage.author.ll);// TODO 大概是，待确认
		pbPlayer.setLadderStar(0);
		pbPlayer.setLevel(stage.author.level);
		pbPlayer.setModel(stage.author.model);
		pbPlayer.setName(stage.author.name);
		pbPlayer.setNationality(stage.author.nationality);
		pbPlayer.setNeedGuild(stage.author.needGuild);
		pbPlayer.setRank(0);
		pbPlayer.setServerId(stage.author.sid);
		pbPlayer.setSex(stage.author.sex);
		pbPlayer.setSignature(stage.author.signature);
		pbPlayer.setState(0);

		builder.setAuthor(pbPlayer.build());

		List<MsgObject.PbTaskTarget> aLimitTargets1 = new ArrayList<>();
		if (Objects.nonNull(stage.adTarget1)) {
			for (PbTaskTarget target : stage.adTarget1) {
				MsgObject.PbTaskTarget.Builder tasktarget = MsgObject.PbTaskTarget.newBuilder();
				tasktarget.setCur(target.cur);
				tasktarget.setNum(target.num);
				tasktarget.setTarget(target.target);
				aLimitTargets1.add(tasktarget.buildPartial());
			}
		}
		builder.addAllALimitTargets1(aLimitTargets1);

		List<MsgObject.PbTaskTarget> aLimitTargets2 = new ArrayList<>();
		if (Objects.nonNull(stage.adTarget2)) {
			for (PbTaskTarget target : stage.adTarget2) {
				MsgObject.PbTaskTarget.Builder tasktarget = MsgObject.PbTaskTarget.newBuilder();
				tasktarget.setCur(target.cur);
				tasktarget.setNum(target.num);
				tasktarget.setTarget(target.target);
				aLimitTargets2.add(tasktarget.buildPartial());
			}
		}
		builder.addAllALimitTargets2(aLimitTargets2);
		
		List<MsgObject.PbTaskTarget> bLimitTargets1 = new ArrayList<>();
		if (Objects.nonNull(stage.bdTarget1)) {
			for (PbTaskTarget target : stage.bdTarget1) {
				MsgObject.PbTaskTarget.Builder tasktarget = MsgObject.PbTaskTarget.newBuilder();
				tasktarget.setCur(target.cur);
				tasktarget.setNum(target.num);
				tasktarget.setTarget(target.target);
				bLimitTargets1.add(tasktarget.buildPartial());
			}
		}
		builder.addAllALimitTargets1(bLimitTargets1);

		List<MsgObject.PbTaskTarget> bLimitTargets2 = new ArrayList<>();
		if (Objects.nonNull(stage.bdTarget2)) {
			for (PbTaskTarget target : stage.bdTarget2) {
				MsgObject.PbTaskTarget.Builder tasktarget = MsgObject.PbTaskTarget.newBuilder();
				tasktarget.setCur(target.cur);
				tasktarget.setNum(target.num);
				tasktarget.setTarget(target.target);
				bLimitTargets2.add(tasktarget.buildPartial());
			}
		}
		builder.addAllALimitTargets2(bLimitTargets2);

		List<MsgObject.PbRollTarget> aRollTargets = new ArrayList<>();
		if (Objects.nonNull(stage.aRollTarget)) {
			for (PbRollTarget target : stage.aRollTarget) {
				MsgObject.PbRollTarget.Builder rollTarget = MsgObject.PbRollTarget.newBuilder();
				rollTarget.setIsTargetAdd(target.isTargetAdd);
				rollTarget.setRollNum(target.rollNum);
				rollTarget.setSerchRow(target.serchRow);
				rollTarget.setType(target.type);

				List<MsgObject.PbTaskTarget> pbTaskTarget = new ArrayList<>();
				if (Objects.nonNull(target.targets)) {
					for (PbTaskTarget t : target.targets) {
						MsgObject.PbTaskTarget.Builder tasktarget = MsgObject.PbTaskTarget.newBuilder();
						tasktarget.setCur(t.cur);
						tasktarget.setNum(t.num);
						tasktarget.setTarget(t.target);
						pbTaskTarget.add(tasktarget.buildPartial());
					}
				}
				rollTarget.addAllTargets(pbTaskTarget);
				aRollTargets.add(rollTarget.build());
			}
		}
		builder.addAllARollTargets(aRollTargets);

		List<MsgObject.PbRollTarget> bRollTargets = new ArrayList<>();
		if (Objects.nonNull(stage.bRollTarget)) {
			for (PbRollTarget target : stage.bRollTarget) {
				MsgObject.PbRollTarget.Builder rollTarget = MsgObject.PbRollTarget.newBuilder();
				rollTarget.setIsTargetAdd(target.isTargetAdd);
				rollTarget.setRollNum(target.rollNum);
				rollTarget.setSerchRow(target.serchRow);
				rollTarget.setType(target.type);

				List<MsgObject.PbTaskTarget> pbTaskTarget = new ArrayList<>();
				if (Objects.nonNull(target.targets)) {
					for (PbTaskTarget t : target.targets) {
						MsgObject.PbTaskTarget.Builder tasktarget = MsgObject.PbTaskTarget.newBuilder();
						tasktarget.setCur(t.cur);
						tasktarget.setNum(t.num);
						tasktarget.setTarget(t.target);
						pbTaskTarget.add(tasktarget.buildPartial());
					}
				}
				rollTarget.addAllTargets(pbTaskTarget);
				bRollTargets.add(rollTarget.build());
			}
		}
		builder.addAllBRollTargets(bRollTargets);

		List<MsgObject.PbRollTarget> autoRollTargets = new ArrayList<>();
		if (Objects.nonNull(stage.autoRollTarget)) {
			for (PbRollTarget target : stage.autoRollTarget) {
				MsgObject.PbRollTarget.Builder rollTarget = MsgObject.PbRollTarget.newBuilder();
				rollTarget.setIsTargetAdd(target.isTargetAdd);
				rollTarget.setRollNum(target.rollNum);
				rollTarget.setSerchRow(target.serchRow);
				rollTarget.setType(target.type);

				List<MsgObject.PbTaskTarget> pbTaskTarget = new ArrayList<>();
				if (Objects.nonNull(target.targets)) {
					for (PbTaskTarget t : target.targets) {
						MsgObject.PbTaskTarget.Builder tasktarget = MsgObject.PbTaskTarget.newBuilder();
						tasktarget.setCur(t.cur);
						tasktarget.setNum(t.num);
						tasktarget.setTarget(t.target);
						pbTaskTarget.add(tasktarget.buildPartial());
					}
				}
				rollTarget.addAllTargets(pbTaskTarget);
				autoRollTargets.add(rollTarget.build());
			}
		}
		builder.addAllAutoRollTargets(autoRollTargets);

		List<MsgObject.PbBelts> pbBelts = new ArrayList<>();
		if (Objects.nonNull(stage.belts)) {
			for (PbBelt belt : stage.belts) {
				MsgObject.PbBelts.Builder pbBelt = MsgObject.PbBelts.newBuilder();
				pbBelt.setEnterId(belt.enterId);
				pbBelt.setExitId(belt.exitId);
				pbBelt.addAllBodies(belt.bodies);
				pbBelts.add(pbBelt.build());
			}
		}
		builder.addAllBelts(pbBelts);

		List<MsgObject.PbBlock> pbBlocks = new ArrayList<>();
		if (Objects.nonNull(stage.blocks)) {
			for (PbBlock block : stage.blocks) {
				MsgObject.PbBlock.Builder pbBlock = MsgObject.PbBlock.newBuilder();
				pbBlock.setId1(block.getId1());
				pbBlock.setId2(block.getId2());
				pbBlock.setId3(block.getId3());
				pbBlock.setId4(block.getId4());
				pbBlock.setId5(block.getId5());
				pbBlock.setId6(block.getId6());
				pbBlock.setId7(block.getId7());
				pbBlocks.add(pbBlock.build());
			}
		}
		builder.addAllBlocks(pbBlocks);

		builder.setCanCreateFish(stage.createFish);

		List<MsgObject.PbCaterpillar> caterpillars = new ArrayList<>();
		if (Objects.nonNull(stage.caterpillar)) {
			for (List<Integer> caterpillar : stage.caterpillar) {
				MsgObject.PbCaterpillar.Builder pbCaterpillar = MsgObject.PbCaterpillar.newBuilder();
				pbCaterpillar.addAllBody(caterpillar);
				caterpillars.add(pbCaterpillar.build());
			}

		}
		builder.addAllCaterpillars(caterpillars);

		builder.setChallenageId(stage.cId);// TODO 大概是，待确认
		builder.setChallenageKey(stage.cKey);// TODO 大概是，待确认
		builder.setColorNum(0);// 已废弃
		builder.setCost(stage.cost);

		List<MsgObject.PbDropBlock> pbDropBlocks = new ArrayList<>();
		if (Objects.nonNull(stage.dropBlocks)) {
			for (PbDropBlock dropBlock : stage.dropBlocks) {
				MsgObject.PbDropBlock.Builder pbDropBlock = MsgObject.PbDropBlock.newBuilder();
				pbDropBlock.addAllDropIds(dropBlock.dropIds);
				pbDropBlocks.add(pbDropBlock.build());
			}
		}
		builder.addAllDropBlocks(pbDropBlocks);

		builder.setDropModel(stage.dropModel);
		builder.addAllDropModels(stage.dropModels);
		builder.setFishNum(stage.fishNum);

		List<MsgObject.PbHidden> pbHiddens = new ArrayList<>();
		if (Objects.nonNull(stage.hiddens)) {
			for (PbHidden hidden : stage.hiddens) {
				MsgObject.PbHidden.Builder pbHidden = MsgObject.PbHidden.newBuilder();
				pbHidden.addAllBody(Objects.isNull(hidden.body)?new ArrayList<>():hidden.body);
				pbHidden.setBirthIndex(Objects.isNull(hidden.index)?0:hidden.index);
				pbHidden.setBlockId(Objects.isNull(hidden.blockId)?0:hidden.blockId);
				pbHiddens.add(pbHidden.build());
			}
		}
		builder.addAllHiddens(pbHiddens);

		builder.setIsAddInfectJudge(stage.isAddInfectJudge);
		builder.setIsAddTarget(stage.isTargetAdd);
		builder.setIsCanUseProp(true);// TODO 待确认
		builder.setIsChangeedDropStage(stage.isChangeedDropStage);
		builder.setIsConditionLimit(stage.isConditionLimit);
		builder.setIsDefaultMotif(stage.isDefaultMotif);
		builder.setIsElementLimit1(stage.isElementLimit1);
		builder.setIsElementLimit2(stage.isElementLimit2);
		builder.setIsLimitUsePropCount(stage.isLimitUsePropCount);
		builder.setIsOfficial(stage.isOfficial);
		builder.setIsStepLimit(stage.isStepLimit);
		builder.setIsSubmitPKShop(stage.isSubmitPkShop);
		builder.setIsSubmitShop(stage.isSubmitShop);
		builder.setIsSyncCollectTarget(stage.isSyncCollectTarget);
		builder.setIsTimeLimit(stage.isTimeLimit);
		builder.setIsUsePropInBag(stage.usePropInBag);

		List<Item> items = stage.getItems();
		if (Objects.nonNull(items) && !items.isEmpty()) {
			List<Integer> ids = new ArrayList<Integer>();
			List<Integer> nums = new ArrayList<Integer>();
			for (Item item : items) {
				if (Objects.isNull(item.itemId)) {
					continue;
				}
				ids.add(item.itemId);
				if (Objects.isNull(item.num)) {
					nums.add(0);
				}else {
					nums.add(item.num);
				}
			}
			if (ids.size()>0 && nums.size()>0) {
				builder.addAllItemIds(ids);
				builder.addAllItemNums(nums);
			}
		}

		List<MsgObject.PbItemMotif> pbItemMotifs = new ArrayList<>();

		if (Objects.nonNull(stage.itemMotifs)) {
			for (ItemMotif itemMotif : stage.itemMotifs) {
				MsgObject.PbItemMotif.Builder pbItemMotif = MsgObject.PbItemMotif.newBuilder();
				pbItemMotif.setId(itemMotif.itemMotifId);
				pbItemMotif.setTexture(itemMotif.tex);
				pbItemMotifs.add(pbItemMotif.build());
			}
		}
		builder.addAllItemMotifs(pbItemMotifs);

		builder.setLinkNum(stage.linkNum);

		List<MsgObject.PbMagicHat> pbMagicHats = new ArrayList<>();
		if (Objects.nonNull(stage.magicHats)) {
			for (PbMagicHat magicHat : stage.magicHats) {
				MsgObject.PbMagicHat.Builder pbMagicHat = MsgObject.PbMagicHat.newBuilder();
				pbMagicHat.setHitTimes(magicHat.hit);
				pbMagicHat.setIdx(magicHat.idx);
				pbMagicHat.setOutPutPropCount(magicHat.outPutPropCount);
				pbMagicHat.addAllElementIds(magicHat.elements);

				List<MsgObject.PbPropChest> pbPropChests = new ArrayList<>();
				if (Objects.nonNull(magicHat.propChests)) {
					for (PbPropChest propChest : magicHat.propChests) {
						MsgObject.PbPropChest.Builder pbPropChest = MsgObject.PbPropChest.newBuilder();
						pbPropChest.setGetByRamdom(propChest.randomType);
						pbPropChest.setHitTimes(propChest.hit);
						pbPropChest.setIdx(propChest.getIdx());
						pbPropChest.addAllPropIds(propChest.propIds);
						pbPropChest.addAllPropNums(propChest.propNums);
						pbPropChest.addAllPropWeights(propChest.propWeights);
						pbPropChests.add(pbPropChest.build());
					}
				}
				pbMagicHat.addAllPropChests(pbPropChests);
				pbMagicHats.add(pbMagicHat.build());
			}
		}
		builder.addAllMagicHats(pbMagicHats);

		builder.setMoneyType(stage.moneyType);
		builder.setMotifGroupId(stage.motifGroupId);
		builder.setMotifGroupName(stage.motifGroupName);
		builder.setName(stage.name);
		builder.setPermitCreateCompetition(stage.permitCreateCompetition);
		builder.setPermitCreateStory(0);// TODO
		builder.setPropNum(stage.propNum);

		List<MsgObject.PbPropChest> pbPropChests = new ArrayList<>();
		if (Objects.nonNull(stage.propChests)) {
			for (PbPropChest propChest : stage.propChests) {
				MsgObject.PbPropChest.Builder pbPropChest = MsgObject.PbPropChest.newBuilder();
				pbPropChest.setGetByRamdom(propChest.randomType);
				pbPropChest.setHitTimes(propChest.hit);
				pbPropChest.setIdx(propChest.idx);
				pbPropChest.addAllPropIds(propChest.propIds);
				pbPropChest.addAllPropNums(propChest.propNums);
				pbPropChest.addAllPropWeights(propChest.propWeights);
				pbPropChests.add(pbPropChest.build());
			}
		}
		builder.addAllPropChests(pbPropChests);
		
		builder.setRollVictory(stage.rollVictory);
		builder.setRuleType(stage.ruleType);
		builder.addAllSetColorNumElement(stage.baseColor);//TODO 待确定
		builder.setSingelModel(stage.singelModel);
		builder.setStageId(stage.stageId);
		builder.setStageVictoryType(0);//TODO 待确定
		builder.setStep(stage.step);
		builder.setStoryType(Objects.isNull(tree.getStoryType())?0:tree.getStoryType());
		builder.setTextureUrl(Objects.isNull(stage.textureUrl)?"":stage.textureUrl);
		builder.setTime(stage.time);
		builder.setTimeLimitLength(stage.timeLimitLength);
		builder.setVoiceTime(stage.voiceTime);
		builder.setVoiceUrl(Objects.isNull(stage.voiceUrl)?"":stage.voiceUrl);
		
		List<MsgObject.PbTaskTarget> targets1 = new ArrayList<>();
		if (Objects.nonNull(stage.targets1)) {
			for (PbTaskTarget target : stage.targets1) {
				MsgObject.PbTaskTarget.Builder tasktarget = MsgObject.PbTaskTarget.newBuilder();
				tasktarget.setCur(target.cur);
				tasktarget.setNum(target.num);
				tasktarget.setTarget(target.target);
				targets1.add(tasktarget.buildPartial());
			}
		}
		builder.addAllTargets1(targets1);

		List<MsgObject.PbTaskTarget> targets2 = new ArrayList<>();
		if (Objects.nonNull(stage.targets2)) {
			for (PbTaskTarget target : stage.targets2) {
				MsgObject.PbTaskTarget.Builder tasktarget = MsgObject.PbTaskTarget.newBuilder();
				tasktarget.setCur(target.cur);
				tasktarget.setNum(target.num);
				tasktarget.setTarget(target.target);
				targets2.add(tasktarget.buildPartial());
			}
		}
		builder.addAllTargets2(targets2);
		
		return builder.build();
	}
}
