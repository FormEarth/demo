package com.example.demo.controller;

import com.example.demo.aop.annotation.StaticURL;
import com.example.demo.common.Dict;
import com.example.demo.common.FileSourceEnum;
import com.example.demo.common.SequenceNumber;
import com.example.demo.entity.*;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.service.CommentService;
import com.example.demo.service.ImageService;
import com.example.demo.service.TagService;
import com.example.demo.service.UserService;
import com.mongodb.client.result.UpdateResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 图集相关接口
 *
 * @author raining_heavily
 * @date 2019/11/3 22:04
 **/
@RestController
public class AtlasController {

    private final static Logger logger = LoggerFactory.getLogger(AtlasController.class);
    /**
     * 分页大小
     **/
    private static final int PAGE_SIZE = 5;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    ImageService imageService;
    @Autowired
    UserService userService;
    @Autowired
    TagService tagService;
    @Autowired
    CommentService commentService;

    /**
     * 创建图集
     *
     * @param files
     * @param writing
     * @return
     * @throws SystemException
     */
    @RequestMapping(value = "/atlas", method = RequestMethod.POST)
    public JSONResult createOneAtlas(@RequestParam("images") MultipartFile[] files, Writing writing) throws SystemException {
        List<String> imageList = new ArrayList<>();
        if (null != files && files.length > 0) {
            //多图片处理
            for (MultipartFile file : files) {
                String relativePath = imageService.singleImageUpload(file, Dict.GLOBAL_WATERMARK, FileSourceEnum.ATLAS);
                imageList.add(relativePath);
            }
        }
        logger.info("图片数量:" + files.length + ",图片相对路径：" + imageList);
        Date nowDate = new Date();
        writing.setAtlasPictures(imageList);
        User currentLoginUser = (User) SecurityUtils.getSubject().getPrincipal();
        //处理标签，新建或者更新
        List<Tag> tagList = writing.getTags();
        if (tagList != null) {
            tagService.handleTagList(tagList, true);
        } else {
            //空时添加空list
            writing.setTags(new ArrayList<>());
        }
        writing.setWritingId(SequenceNumber.getInstance().getNextSequence(Dict.SEQUENCE_TYPE_WRITING));
        writing.setType(Dict.WRITING_TYPE_ATLAS);
        writing.setStatus(Dict.WRITING_STATUS_NORMAL);
        writing.setCreatorId(currentLoginUser.getUserId());
        writing.setSendTime(nowDate);
        mongoTemplate.insert(writing);
        String objectId = writing.getWritingId();
        return new JSONDataResult().add("id", objectId);
    }

    /**
     * 批量查询图集接口（初始化和滑倒底部加载更多）
     * 有用户id参数时查指定用户
     *
     * @param currentPage 当前页
     * @param userId      用户id，允许为空
     * @return
     */
    @StaticURL
    @RequestMapping(value = "/atlases/{currentPage}", method = RequestMethod.GET)
    public JSONResult queryAtlasByPage(@PathVariable int currentPage, @RequestParam(required = false) Long userId) throws SystemException {

        Query query = new Query();
        //正常状态
        query.addCriteria(Criteria.where("atlasStatus").is(1));
        //是否为已登录用户查询自己资源
        boolean personal = false;
        User currentLoginUser = (User) SecurityUtils.getSubject().getPrincipal();
        //查询的用户信息
        User queryUser = new User();
        if (userId != null) {
            queryUser = userService.queryCommonInfo(userId);
            //已登录用户查询自己的数据，显示仅自己可见数据
            if (currentLoginUser != null) {
                if (userId.equals(currentLoginUser.getUserId())) {
                    personal = true;
                }
            }
            //查指定用户的图集信息
            query.addCriteria(Criteria.where("creater").is(userId));
        }
        if (!personal) {
            query.addCriteria(Criteria.where("personal").is(false));
        }
        //第一次查询时返回总页数
        JSONDataResult result = new JSONDataResult();
        if (currentPage == 0) {
            long total = mongoTemplate.count(query, Atlas.class);
            result.add("total", total);
        }
        query.with(new Sort(Sort.Direction.DESC, "sendTime"));
        query.skip(currentPage * PAGE_SIZE).limit(PAGE_SIZE);
        List<Atlas> atlases = mongoTemplate.find(query, Atlas.class);
        if (userId != null) {
            for (Atlas atlas : atlases) {
                atlas.setUser(queryUser);
            }
        } else {
            for (Atlas atlas : atlases) {
                User user = userService.queryCommonInfo(atlas.getCreater());
                atlas.setUser(user);
            }
        }
        result.add("atlases", atlases);
        return result;
    }

    /**
     * 加载最新图集（顶部下拉刷新）
     *
     * @param lastRefreshTime
     * @return
     * @throws SystemException
     * @deprecated 该接口暂时没有使用
     */
    @StaticURL
    @RequestMapping(value = "/atlases/refresh/{lastRefreshTime}", method = RequestMethod.GET)
    public JSONResult queryAtlasByTime(@PathVariable String lastRefreshTime) throws SystemException {
        Assert.notNull(lastRefreshTime, "刷新时间不能为空！");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = sdf.parse(lastRefreshTime);
        } catch (ParseException e) {
            logger.error("the param:lastRefreshTime format error");
            throw new SystemException(ExceptionEnums.FILED_VALIDATOR_ERROR);
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("personal").is(false));
        query.addCriteria(Criteria.where("atlasStatus").is(1));
        //大于上次刷新时间
        query.addCriteria(Criteria.where("sendTime").gt(date));
        query.with(new Sort(Sort.Direction.DESC, "sendTime"));
        query.limit(PAGE_SIZE);
        List<Atlas> atlases = mongoTemplate.find(query, Atlas.class);
        for (Atlas atlas : atlases) {
            User user = userService.queryCommonInfo(atlas.getCreater());
            atlas.setUser(user);
        }
        return new JSONDataResult().add("atlases", atlases);
    }

    /**
     * 根据作品id删除作品
     * TODO 删除图片文件
     *
     * @param writingId
     * @return
     * @throws SystemException
     * @deprecated 已废弃
     */
    @RequestMapping(value = "/atlas/{writingId}", method = RequestMethod.DELETE)
    public JSONResult removeAtlasByAtlasId(@PathVariable String writingId) throws SystemException {
        User currentLoginUser = (User) SecurityUtils.getSubject().getPrincipal();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(writingId));
        query.addCriteria(Criteria.where("creatorId").is(currentLoginUser.getUserId()));
        UpdateResult result = mongoTemplate.updateFirst(query, new Update().set("status", Dict.WRITING_STATUS_DELETED), "writing");
        if (result.getModifiedCount() < 1) {
            throw new SystemException(ExceptionEnums.DATA_DELETE_FAIL);
        }
        return JSONDataResult.success();
    }

    /**
     * 图集编辑<br>
     * tip:不允许图片修改
     *
     * @param
     * @return
     * @throws SystemException
     */
    @RequestMapping(value = "/atlas", method = RequestMethod.PUT)
    public JSONResult modifyAtlas(@RequestBody Writing writing) throws SystemException {
        User currentLoginUser = (User) SecurityUtils.getSubject().getPrincipal();
        tagService.handleTagList(writing.getTags(), false);
        Query query = new Query().addCriteria(Criteria.where("_id").is(writing.getWritingId())).addCriteria(Criteria.where("creatorId").is(currentLoginUser.getUserId()));
        //向数组末尾增加元素
        Update update = new Update().set("content", writing.getContent())
                .set("tags", writing.getTags())
                .set("updater", currentLoginUser.getUserId())
                .set("updateTime", new Date())
                .set("personal", writing.getPersonal())
                .set("comment", writing.getComment())
                .set("modified", true)
                .set("modifiedTime", new Date());
        UpdateResult result = mongoTemplate.updateFirst(query, update, Writing.class);
        if (result.getModifiedCount() < 1) {
            throw new SystemException(ExceptionEnums.DATA_UPDATE_FAIL);
        }
        return JSONDataResult.success();
    }


}
