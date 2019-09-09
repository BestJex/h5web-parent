package com.xmbl.h5.web.game.entity;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  StageInfo 
 * @创建时间:  2018年9月11日 上午10:20:27
 * @修改时间:  2018年9月11日 上午10:20:27
 * @类说明:
 */
@Data
@Document(collection ="stage_info")
public class StageInfo {
	
	/**
	 * 平台保存关卡 id
	 */
	@Id
	private String _id;
	
	/**
	 * 创建时间
	 */
	@CreatedDate
	@Field("created_date")
	@Indexed
	private Date createdDate;
	
	/**
	 * 修改时间
	 */
	@LastModifiedDate
	@Field("last_modified_date")
	@Indexed
	private Date LastModifiedDate;
	
	/**
	 * 排行榜关卡id
	 */
	@Field("stage_id")
	@Indexed
	private String stageId;
	
	/**
	 * 排序方式:
	 *  1  按步数排序 (消除模式)
	 *  2  按时间排序 (消除模式)
	 *  3  按分数排序 (消除模式)
	 *  4  按消除个数排序  个数相同按分数排序(收集模式)
	 */
	@Field("sort_order")
	private Integer sortOrder = 4;
	
}
