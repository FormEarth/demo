package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Dict;
import com.example.demo.entity.Article;
import com.example.demo.entity.Comment;
import com.example.demo.entity.JSONDataResult;
import com.example.demo.entity.JSONResult;
import com.example.demo.entity.User;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.mapper.CommentMapper;
import com.example.demo.service.ArticleService;
import com.example.demo.service.UserService;
import com.example.demo.util.Util;

@RestController
@RequestMapping(value="/demo/api")
public class ArticleController {

	private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

	@Value("${image.access.url}")
	private String imageAccessPref;
	
	@Autowired
	ArticleService articleService;
	@Autowired
	UserService userService;
	@Autowired
	CommentMapper commentMaper;
	
	/**
	 * 获取所有长文(首页数据加载、分页)
	 * @param currentPage
	 * @return
	 * @throws SystemException 
	 */
	@RequestMapping(value= {"/articles/{currentPage}"},method = RequestMethod.GET)
	public JSONResult getarticlesWithPage( @PathVariable String currentPage) throws SystemException {
		//article article = new article();
		QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("status", "1");
		queryWrapper.eq("personal", "0");
		queryWrapper.orderByDesc("send_time");
		long current = Long.valueOf(currentPage);
		
		Page<Article> page = new Page<>(current,5);
		//TODO 总页数
		int total = articleService.total(queryWrapper);
		List<Article> articles = articleService.getAll(page, queryWrapper);
		logger.info("articles:"+articles);
		return new JSONDataResult().add("articles", articles).add("total", total);
		
	}
	
	/**
	 * 获取一个账号下的长文（个人主页数据，分页）
	 * @param account 用户id
	 * @param currentPage 当前页
	 * @return 分页数据和用户信息
	 * @throws SystemException
	 */
	@RequestMapping(value="/{userId}/articles/{currentPage}",method = RequestMethod.GET)
	public JSONResult getarticlesByAccount(@PathVariable long userId,@PathVariable String currentPage) throws SystemException {

		QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
		userQueryWrapper.eq("user_id", userId);
		userQueryWrapper.select("user_status","user_name","sign","personal_profile","avatar");
		User user = userService.getOne(userQueryWrapper);
		Util.userVerify(user);
		QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
		//查询已发布文章
		queryWrapper.eq("author", userId).eq("status", "1").orderByDesc("send_time");
		long current = Long.valueOf(currentPage);
		Page<Article> page = new Page<>(current,5);
		List<Article> articles = articleService.getAll(page, queryWrapper);
		int total = articleService.total(queryWrapper);
		return new JSONDataResult().add("user", user).add("articles", articles).add("total", total);
		
	}
	
	/**
	 * 根据Id获取文章详情
	 * @param article
	 * @return
	 * @throws SystemException 
	 */
	@RequestMapping(value="/article/{articleId}",method = RequestMethod.GET)
	public JSONResult getarticle(@PathVariable String articleId) throws SystemException {
		
		QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("article_id", Long.valueOf(articleId));
		Article article= articleService.getOne(queryWrapper);
		if(article == null) {
			throw new SystemException(ExceptionEnums.SOURCE_NOT_FOUND);
		}
		if(article.isPersonal()) {
			throw new SystemException(ExceptionEnums.SOURCE_NOT_PERMIT);
		}
		List<Comment> comments = new ArrayList<>();
		//允许评论时查询评论数据
		if(article.isComment()) {
			comments = commentMaper.queryCommentsWithRepies(Long.valueOf(articleId));
		}
		logger.info("comment:"+comments);
		return new JSONDataResult().add("article", article).add("comments", comments);
		
	}
	
	/**
	 * 创建长文
	 * @param article
	 * @return
	 * @throws SystemException 
	 */
	@RequestMapping(value="/article",method = RequestMethod.POST)
	public JSONResult createarticle(HttpServletRequest request, @RequestBody Article article) throws SystemException {
		Date date = new Date();
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		if(user == null) throw new SystemException(ExceptionEnums.USER_NOT_LOGIN);
		logger.info(user.toString());
		logger.info(article.toString());
		//TODO tags的处理有点问题
		article.setTags("#"+article.getTags());
		article.setAuthor(user.getUserId());
		article.setAuthorName(user.getUserName());
		article.setCreateTime(date);
		if ("1".equals(article.getStatus())) {
			article.setSendTime(date);
		}
		articleService.add(article);
		//新增后返回articleId
		logger.info("articleId:" + article.getArticleId());
		return new JSONDataResult().add("articleId", article.getArticleId());
		
	}
	
	/**
	 * 更新长文
	 * @param article
	 * @return
	 * @throws SystemException 
	 */
	@RequestMapping(value="/article",method = RequestMethod.PUT)
	public JSONResult updatearticle(@RequestBody Article article) throws SystemException {
		
		Date date = new Date();
		Session session = SecurityUtils.getSubject().getSession();
		User user = (User) session.getAttribute(Dict.CURRENT_USER_DATA);
		logger.info(user.toString());
		logger.info(article.toString());
		String tags = article.getTags();
		if(StringUtils.isNotEmpty(tags)) {
			String[] tagArray = tags.replaceAll("，", ",").split(",");
			for(String tag : tagArray) {
				tags = "#" + tag + "\\|";
			}
			article.setTags(tags);
		}
		article.setUpdater(user.getUserId());
		article.setUpdateTime(date);
		logger.info(article.toString());
		articleService.updateById(article);
		return JSONResult.success();
		
	}
	
	/**
	 * 删除长文
	 * @param article
	 * @return
	 * @throws SystemException 
	 */
	@RequestMapping(value="/article",method = RequestMethod.DELETE)
	public JSONResult deletearticle(String articleId) throws SystemException {
		logger.info(articleId);
		QueryWrapper<Article> queryWrapper = new QueryWrapper<Article>().eq("article_id", articleId);
		articleService.delete(queryWrapper);
		return JSONResult.success();
		
	}
}
