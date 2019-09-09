package com.xmbl.h5.web.game.consts;
/**
 * 各个浏览器
 */
public enum EnumBrowser {

	qq(1),
	wx(2),
	other(3),
	;
	
	EnumBrowser(int id) {
		this.id = id;
	}
	
	public int id;
	
	public static final EnumBrowser getByName(String name) {
		return EnumBrowser.valueOf(name);
	}
	
	public static void main(String[] args) {
		System.out.println(EnumBrowser.getByName("qq").id);
	}
}
