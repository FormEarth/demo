package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.example.demo.common.FileSourceEnum;
import com.example.demo.service.ApiService;
import com.example.demo.service.CommentService;
import com.example.demo.util.MarkdownParser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.aop.annotation.StaticURL;
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
import org.springframework.web.multipart.MultipartFile;

/**
 * @author raining_heavily
 */
@RestController
public class ArticleController {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    ArticleService articleService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    ApiService apiService;


    /**
     * 获取所有长文(首页数据加载、分页)
     * @param currentPage
     * @return
     * @throws SystemException
     */
    @StaticURL
    @RequestMapping(value= {"/articles/{currentPage}"},method = RequestMethod.GET)
    public JSONResult getArticlesWithPage( @PathVariable long currentPage) throws SystemException {
        //article article = new article();
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "1");
        queryWrapper.eq("personal", "0");
        queryWrapper.orderByDesc("send_time");
        JSONDataResult result = new JSONDataResult();
        int total = 0;
        if(currentPage==1){
            total = articleService.total(queryWrapper);
            result.add("total",total);
        }
        Page<Article> page = new Page<>(currentPage,5);
        List<Article> articles = articleService.getAll(page, queryWrapper);
        result.add("articles", articles);
        return result;

    }

    /**
     * 获取一个账号下的长文（个人主页数据，分页）
     * @param userId 用户id
     * @param currentPage 当前页
     * @return 分页数据和用户信息
     * @throws SystemException
     */
    @StaticURL
    @RequestMapping(value="/{userId}/articles/{currentPage}",method = RequestMethod.GET)
    public JSONResult getArticlesByAccount(@PathVariable long userId,@PathVariable long currentPage) throws SystemException {

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_id", userId);
        userQueryWrapper.select("user_status","user_name","sign","personal_profile","avatar");
        User user = userService.getOne(userQueryWrapper);
        Util.userVerify(user);
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        //查询已发布文章
        queryWrapper.eq("author", userId).eq("status", "1").orderByDesc("send_time");
        Page<Article> page = new Page<>(currentPage,5);
        List<Article> articles = articleService.getAll(page, queryWrapper);
        int total = articleService.total(queryWrapper);
        return new JSONDataResult().add("user", user).add("articles", articles).add("total", total);

    }

    /**
     * 根据Id获取文章详情
     * @param articleId
     * @return
     * @throws SystemException
     */
    @StaticURL
    @RequestMapping(value="/article/{articleId}",method = RequestMethod.GET)
    public JSONResult getArticle(@PathVariable long articleId) throws SystemException {

        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", articleId);
        Article article= articleService.getOne(queryWrapper);
        if(article == null) {
            throw new SystemException(ExceptionEnums.SOURCE_NOT_FOUND);
        }
        if(article.getPersonal()) {
            throw new SystemException(ExceptionEnums.SOURCE_NOT_PERMIT);
        }
        List<Comment> comments = new ArrayList<>();
        //允许评论时查询评论数据
        if(article.getComment()) {
            comments = commentService.queryCommentsWithUser(articleId);
        }
        //将阅读量+1
        article.setReaderNum(article.getReaderNum()+1);
        articleService.updateById(article);
        return new JSONDataResult().add("article", article).add("comments", comments);

    }

    /**
     * 创建长文
     * @param article
     * @return
     * @throws SystemException
     */
    @RequestMapping(value="/article",method = RequestMethod.POST)
    public JSONResult createArticle(@RequestParam(value = "image",required = false) MultipartFile file, Article article) throws SystemException {
        Date date = new Date();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(file == null){
            article.setFrontCover(user.getFrontCover());
        }else{
            //上传
            String relativePath = apiService.singleImageUploadWithoutWatermark(file, FileSourceEnum.ARTICLE);
            article.setFrontCover(relativePath);
        }
        //TODO tags的处理有点问题
        article.setSummary(MarkdownParser.toText(article.getContent(),200));
        article.setAuthor(user.getUserId());
        article.setAuthorName(user.getUserName());
        article.setCreateTime(date);
        article.setStatus(Dict.ARTICLE_STATUS_NORMAL);
        article.setSendTime(date);
        articleService.add(article);
        //新增后返回articleId
        return new JSONDataResult().add("articleId", article.getArticleId());

    }

    /**
     * 更新长文
     * @param article
     * @return
     * @throws SystemException
     */
    @RequestMapping(value="/article",method = RequestMethod.PUT)
    public JSONResult updateArticle(@RequestBody Article article) throws SystemException {

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
     * @param articleId
     * @return
     * @throws SystemException
     */
    @RequestMapping(value="/article",method = RequestMethod.DELETE)
    public JSONResult deleteArticle(String articleId) throws SystemException {
        logger.info(articleId);
        QueryWrapper<Article> queryWrapper = new QueryWrapper<Article>().eq("article_id", articleId);
        articleService.delete(queryWrapper);
        return JSONResult.success();

    }
}
