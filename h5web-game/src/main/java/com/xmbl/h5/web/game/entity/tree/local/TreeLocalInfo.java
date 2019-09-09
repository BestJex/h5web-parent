package com.xmbl.h5.web.game.entity.tree.local;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection="tree_local_info")
public class TreeLocalInfo {
	@Id
	private long treeId;
	private long numOfPlayer;
	private long numOfPassed;
	

}
