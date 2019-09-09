package com.xmbl.h5.web.game.entity.tree.base;

import lombok.Data;

@Data
public class StageLable {
	private String name;//标签的名字
	private int curCount;//当前拥有
	private int maxCount;//最大拥有
	private String texUrls;//前4张封面
}
