package com.example.demo.controller;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Dict;
import com.example.demo.entity.Artice;
import com.example.demo.entity.JSONDataResult;
import com.example.demo.entity.JSONResult;
import com.example.demo.entity.User;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.service.ArticeService;

@RestController
@RequestMapping(value="/demo/api")
public class ArticeController {

	private static final Logger logger = LoggerFactory.getLogger(ArticeController.class);

	@Autowired
	ArticeService articeService;
	
	/**
	 * 获取所有长文(首页数据加载、分页)
	 * @param currentPage
	 * @return
	 * @throws SystemException 
	 */
	@RequestMapping(value= {"/artices/{currentPage}"},method = RequestMethod.GET)
	public JSONResult getArticesWithPage(@PathVariable String currentPage) throws SystemException {
		
		//Artice artice = new Artice();
		QueryWrapper<Artice> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("status", "1");
		
		long current = Long.valueOf(currentPage);
		
		Page<Artice> page = new Page<>(current,5);
		//TODO 总页数
		List<Artice> artices = articeService.getAll(page, queryWrapper);
		return new JSONDataResult().add("artices", artices);
		
	}
	
	/**
	 * 获取一个账号下的长文（个人主页数据，分页）
	 * @param account 用户id
	 * @param currentPage 当前页
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value="/{userId}/artices/{currentPage}",method = RequestMethod.GET)
	public JSONResult getArticesByAccount(@PathVariable long userId,@PathVariable String currentPage) throws SystemException {
		logger.info(String.valueOf(userId));
		
		QueryWrapper<Artice> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("author", userId).eq("status", "1");
		
		long current = Long.valueOf(currentPage);
		
		Page<Artice> page = new Page<>(current,5);
		//TODO 总页数
		List<Artice> artices = articeService.getAll(page, queryWrapper);
		return new JSONDataResult().add("artices", artices);
		
	}
	
	/**
	 * 根据Id获取文章详情
	 * @param artice
	 * @return
	 * @throws SystemException 
	 */
	@RequestMapping(value="/artice/{articeId}",method = RequestMethod.GET)
	public JSONResult getArtice(@PathVariable String articeId) throws SystemException {
		
		QueryWrapper<Artice> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("artice_id", Long.valueOf(articeId));
		Artice artice= articeService.getOne(queryWrapper);
		return new JSONDataResult().add("artice", artice);
		
	}
	
	/**
	 * 创建长文
	 * @param artice
	 * @return
	 * @throws SystemException 
	 */
	@RequestMapping(value="/artice",method = RequestMethod.POST)
	public JSONResult createArtice(@RequestBody Artice artice) throws SystemException {
		
		Date date = new Date();
		Session session = SecurityUtils.getSubject().getSession();
		User user = (User) session.getAttribute(Dict.CURRENT_USER_DATA);
		if(user == null) throw new SystemException(ExceptionEnums.USER_NOT_LOGIN);
		logger.info(user.toString());
		logger.info(artice.toString());
		String tags = artice.getTags();
		String str = "";
		if(StringUtils.isNotEmpty(tags)) {
			String[] tagArray = tags.replaceAll("，", ",").split(",");
			for(String tag : tagArray) {
				str += "#" + tag + "|";
			}
			artice.setTags(str);
		}
		artice.setAuthor(user.getUserId());
		artice.setCreater(user.getUserId());
		artice.setCreateTime(date);
		if ("1".equals(artice.getStatus())) {
			artice.setSendTime(date);
		}
		articeService.add(artice);
		return JSONResult.success();
		
	}
	
	/**
	 * 更新长文
	 * @param artice
	 * @return
	 * @throws SystemException 
	 */
	@RequestMapping(value="/artice",method = RequestMethod.PUT)
	public JSONResult updateArtice(@RequestBody Artice artice) throws SystemException {
		
		Date date = new Date();
		Session session = SecurityUtils.getSubject().getSession();
		User user = (User) session.getAttribute(Dict.CURRENT_USER_DATA);
		logger.info(user.toString());
		logger.info(artice.toString());
		String tags = artice.getTags();
		if(StringUtils.isNotEmpty(tags)) {
			String[] tagArray = tags.replaceAll("，", ",").split(",");
			for(String tag : tagArray) {
				tags = "#" + tag + "\\|";
			}
			artice.setTags(tags);
		}
		artice.setUpdater(user.getUserId());
		artice.setUpdateTime(date);
		logger.info(artice.toString());
		articeService.updateById(artice);
		return JSONResult.success();
		
	}
	
	/**
	 * 删除长文
	 * @param artice
	 * @return
	 * @throws SystemException 
	 */
	@RequestMapping(value="/artice",method = RequestMethod.DELETE)
	public JSONResult deleteArtice(String articeId) throws SystemException {
		logger.info(articeId);
		QueryWrapper<Artice> queryWrapper = new QueryWrapper<Artice>().eq("artice_id", articeId);
		articeService.delete(queryWrapper);
		return JSONResult.success();
		
	}
}
