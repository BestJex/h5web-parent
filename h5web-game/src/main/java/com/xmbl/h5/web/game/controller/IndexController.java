package com.xmbl.h5.web.game.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmbl.h5.web.common.logic.AbstractController;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名: IndexController
 * @创建时间: 2018年7月31日 下午6:34:14
 * @修改时间: 2018年7月31日 下午6:34:14
 * @类说明:
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/game")
public class IndexController extends AbstractController {

	@ResponseBody
	@RequestMapping("/")
	public String index() {
		return "WX login";
	}

}
