package com.xmbl.h5.web.game.entity.tree.player;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Document(collection = "player_tree_info")
public class PlayerTreeInfo {
	@Id
	private String id;
	private String playerId;// 玩家Id
	private long treeId;// 关卡集ID
	private int energy = 0;// 体力值
	private int progress = 0;// 进度
	private ConditionLimitInfo conditionLimitInfo;
	private int returnEnergyFlag = 1;// 是否在首次完成关卡后返还体力值,0表示不返还，1表示返还
	private int shareTime;// 分享次数
	private int rank = -1;
	private int passNodeNum = 0;
	private int skipStageNum = 0;
	private Map<Long, NodeInfo> nodeInfos;
	private List<ElementInfo> element1Infos;
	private List<ElementInfo> element2Infos;
	private int passStageNum;
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class NodeInfo {
		long nodeId;
		/**0表示未完成,1表示已完成,2已跳过*/
		int status;
	}
	
	public static class ElementInfo {
		private int target;//元素id
		private int num;//可使用数量
		private int cur;//当前剩余数量
		private int surplus;//commit时候使用的步数……
		
		@JSONField(name = "Target")
		public int getTarget() {
			return target;
		}
		public void setTarget(int target) {
			this.target = target;
		}
		@JSONField(name = "Cur")
		public int getCur() {
			return cur;
		}
		public void setCur(int cur) {
			this.cur = cur;
		}
		@JSONField(name = "Num")
		public int getNum() {
			return num;
		}
		public void setNum(int num) {
			this.num = num;
		}
		@JSONField(name = "Surplus")
		public int getSurplus() {
			return surplus;
		}
		public void setSurplus(int surplus) {
			this.surplus = surplus;
		}
		
		
	}
	
	public static class ConditionLimitInfo {
		private int type = -1;//-1表示不限制, 0步数, 1时间
		private int num;//总数
		private int cur;//当前剩余
		
		@JSONField(name = "Type")
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		@JSONField(name = "Cur")
		public int getCur() {
			return cur;
		}
		public void setCur(int cur) {
			this.cur = cur;
		}
		@JSONField(name = "Num")
		public int getNum() {
			return num;
		}
		public void setNum(int num) {
			this.num = num;
		}
	}
}
