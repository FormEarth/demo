package com.example.demo.controller;

import com.example.demo.common.FileSourceEnum;
import com.example.demo.entity.*;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.service.ImageService;
import com.example.demo.service.TagService;
import com.example.demo.service.impl.ArticleServiceImpl;
import com.example.demo.util.MarkdownUtil;
import com.example.demo.util.Util;
import com.mongodb.client.result.UpdateResult;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 文章相关
 * @author raining_heavily
 */
@RestController
public class ArticleController {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    ArticleServiceImpl articleService;
    @Autowired
    TagService tagService;
    @Autowired
    ImageService imageService;
    @Autowired
    MongoTemplate mongoTemplate;
    @Value(value = "${static.blog.generate.shell}")
    private String[] shell;

    /**
     * 创建长文章
     *
     * @param writing
     * @return
     * @throws SystemException
     */
    @RequestMapping(value = "/article", method = RequestMethod.POST)
    public JSONResult createNewArticle(@RequestParam(value = "image", required = false) MultipartFile file, Writing writing) throws SystemException {

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (file == null) {
            writing.setFrontCover(user.getFrontCover());
        } else {
            //上传
            String relativePath = imageService.singleImageUpload(file, FileSourceEnum.ARTICLE);
            writing.setFrontCover(relativePath);
        }
        //标签处理
        List<Tag> tagList = writing.getTags();
        if (tagList != null) {
            tagService.handleTagList(tagList, true);
        } else {
            //空时添加空list
            writing.setTags(new ArrayList<>());
        }
        tagService.handleTagList(writing.getTags(), true);
        //文章处理
        articleService.articleHandle(writing);
        logger.info("{} create article {}", writing.getCreatorId(), writing.getWritingId());
        //新增后返回articleId
        return new JSONDataResult().add("articleId", writing.getWritingId());
    }

    /**
     * 更新长文
     * 重新生成静态文件
     *
     * @param writing
     * @return
     * @throws SystemException
     */
    @RequestMapping(value = "/article", method = RequestMethod.PUT)
    public JSONResult updateArticle(@RequestParam(value = "image", required = false) MultipartFile file, Writing writing) throws SystemException, IOException {

        Date date = new Date();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        tagService.handleTagList(writing.getTags(), false);
        Query query = new Query().addCriteria(Criteria.where("_id").is(writing.getWritingId()));

        Update update = new Update()
                .set("title", writing.getTitle())
                .set("summary", MarkdownUtil.toText(writing.getContent(), 200))
                .set("content", writing.getContent())
                .set("tags", writing.getTags())
                .set("updater", user.getUserId())
                .set("updateTime", date)
                .set("personal", writing.getPersonal())
                .set("comment", writing.getComment())
                .set("modified", true)
                .set("modifiedTime", date);
        if (file != null) {
            update.set("frontCover", imageService.singleImageUploadWithoutWatermark(file, FileSourceEnum.FRONT_COVER));
        } else{
            //如果没有修改封面，还是使用原封面
           String frontCover = mongoTemplate.findById(writing.getWritingId(),Writing.class).getFrontCover();
           writing.setFrontCover(frontCover);
        }

        UpdateResult result = mongoTemplate.updateFirst(query, update, Writing.class);
        if (result.getModifiedCount() < 1) {
            throw new SystemException(ExceptionEnums.DATA_UPDATE_FAIL);
        }
        if (writing.getSaveToFile()) {
            articleService.writeContentToFile(writing);
            Util.shellExecute(shell);
        }
        return JSONResult.success();

    }


}
