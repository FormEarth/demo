package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 该controller写那些返回的非json数据接口
 * @description
 * @author raining_heavily
 * @date 2019年5月28日
 */
@Controller
public class CommonController {

	/**
	 * 访问默认接口时转发到分页接口
	 * @return
	 */
	@RequestMapping(value= {"/articles"},method = RequestMethod.GET)
	public String getArticles() {
		//默认接口转发到第一页
		return "forward:/demo/api/articles/1";
	}
	
}
