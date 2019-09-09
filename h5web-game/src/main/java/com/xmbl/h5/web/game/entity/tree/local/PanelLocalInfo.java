package com.xmbl.h5.web.game.entity.tree.local;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection="panel_local_info")
public class PanelLocalInfo {
	@Id
	private String id;
	private Long treeId;
	private Long nodeId;
	private Integer index;
	private Long stageId;
	
	private Long numOfPlayer;
	private Long numOfFinished;

}
