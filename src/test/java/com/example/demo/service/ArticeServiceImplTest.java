package com.example.demo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.DemoApplicationTests;
import com.example.demo.controller.ArticleController;
import com.example.demo.entity.Article;
import com.example.demo.exception.SystemException;
/**
 * 
 * @description 文章service测试类
 * @author raining_heavily
 * @date 2019年2月23日
 */
public class ArticeServiceImplTest extends DemoApplicationTests{

	private static final Logger logger = LoggerFactory.getLogger(ArticeServiceImplTest.class);
	
	@Autowired
	ArticleService articeService;
	
//	@Test
//    public void testUserLogin() throws SystemException{
//		User user = userService.userLogin("wcy", "123");
//		System.out.println(user);
//		Assert.assertNotNull(user);
//    }
//	
//	@Test
//	public void getOne() throws SystemException{
//		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//		queryWrapper.eq("user_id", 7);
//		User user = userService.getOne(queryWrapper);
//		System.out.println(user);
//		Assert.assertNotNull(user);
//    }
	
	public void createArtice() throws SystemException{
		
		List<Article> list = new ArrayList<>();
		for(int i = 0;i < 8;i++) {
			Date date = new Date();
			System.out.println(date);
			Article artice = new Article();
			artice.setTitle("this is test"+i);
			artice.setContent("this is content"+i);
			artice.setCodeStyle("github");
			
			artice.setAuthor(1001);
			artice.setCreater(1001);
			artice.setCreateTime(date);
			artice.setUpdater(1001);
			artice.setUpdateTime(date);
			
			list.add(artice);
		}
		
		for(Article artice:list) {
			articeService.add(artice);
		}	
		
    }
	
	//@Test
	public void updateArtice() throws SystemException{
		Article artice = new Article();
		artice.setArticleId(1012);
		artice.setTitle("是个测试");
		artice.setContent("> 没有什么是永恒的\n\n散落在指尖的阳光，我试着轻轻抓住光影的踪迹，它却在眉宇间投下一片淡淡的阴影.调皮的阳光掀动了四月的心帘，温暖如约的歌声渐起。似乎在诉说着，我也可以在漆黑的角落里，找到阴影背后的阳光，找到阳光与阴影奏出和谐的旋律。我要用一颗敏感赤诚的心迎接每一缕滑过指尖的阳光！\n```Java\n\nString str = \"Hello World!\" \n\nSystem.out.println(str);\n```\n\n**文字加粗了**");
		artice.setCreater(1001);
		artice.setUpdater(1001);
		artice.setUpdateTime(new Date());
		articeService.updateById(artice);
    }
	
	@Test
	public void getAll() throws SystemException{
		QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("status", "0").eq("author", 1001).eq("personal", false);
		long current = 3;
		long size = 5;
		Page<Article> page = new Page<>(current,size);
		//page.setSize(5);
		List<Article> artices = articeService.getAll(page,queryWrapper);
		for(Article artice:artices) {
			logger.info("---"+artice);
		}
		Assert.assertNotNull(artices);
    }
}
