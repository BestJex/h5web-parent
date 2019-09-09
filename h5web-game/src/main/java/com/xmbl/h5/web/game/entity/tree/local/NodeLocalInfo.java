package com.xmbl.h5.web.game.entity.tree.local;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection="tree_node_local_info")
public class NodeLocalInfo {
	@Id
	private String id;
	private Long treeId;
	private Long nodeId;
	private int playCount;
	private int passCount;

}
