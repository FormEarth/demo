package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.aop.annotation.StaticURL;
import com.example.demo.common.Dict;
import com.example.demo.common.FileSourceEnum;
import com.example.demo.entity.*;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.service.*;
import com.example.demo.util.Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    TagService tagService;
    @Autowired
    ApiService apiService;

    /**
     * 创建长文
     * @param article
     * @return
     * @throws SystemException
     */
    @RequestMapping(value="/article",method = RequestMethod.POST)
    public JSONResult createNewArticle(@RequestParam(value = "image",required = false) MultipartFile file, Article article) throws SystemException {

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(file == null){
            article.setFrontCover(user.getFrontCover());
        }else{
            //上传
            String relativePath = apiService.singleImageUploadWithoutWatermark(file, FileSourceEnum.ARTICLE);
            article.setFrontCover(relativePath);
        }
        //标签处理
        tagService.handleTagList(article.getTags(),true);
        //文章处理
        articleService.articleHandle(article);
        //新增后返回articleId
        return new JSONDataResult().add("articleId", article.getArticleId());
    }

    /**
     * 获取长文(首页数据加载、分页)
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
        JSONDataResult result = new JSONDataResult();
        int total = 0;
        if(currentPage==1){
            total = articleService.total(queryWrapper);
            result.add("total",total);
        }
        List<Article> articles = articleService.queryArticleInHome(currentPage);
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
        Article article = articleService.queryArticleDetailById(articleId);
        List<Comment> comments = new ArrayList<>();
        //允许评论时查询评论数据
        if(article.getComment()) {
            comments = commentService.queryCommentsWithUser(articleId);
        }
        return new JSONDataResult().add("article", article).add("comments", comments);

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
