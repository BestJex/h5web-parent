package com.xmbl.h5.web.game.dto;

import java.util.Date;

import lombok.Data;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  StageRank 
 * @创建时间:  2018年9月11日 下午12:34:32
 * @修改时间:  2018年9月11日 下午12:34:32
 * @类说明:
 */
@Data
public class StageRankDto {
	private String _id;
	private String createdBy;
	private Date createdDate;
	private String createdDateStr;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String lastModifiedDateStr;
	private String stageId;
	private String playerId;
	private String playerName;
	private int playerSex;
	private String playerImg;
	private String isFirstWin;
	private Integer sortOrder;
	private Long usedStepNum;
	private Long usedTime;
	private Long removeBlockNum;
	private Long scoreNum;
}
