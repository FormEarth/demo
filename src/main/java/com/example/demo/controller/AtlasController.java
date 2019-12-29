package com.example.demo.controller;

import com.example.demo.aop.annotation.StaticURL;
import com.example.demo.common.Dict;
import com.example.demo.common.FileSourceEnum;
import com.example.demo.entity.*;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.service.ApiService;
import com.example.demo.service.TagService;
import com.example.demo.service.UserService;
import com.example.demo.util.Util;
import com.mongodb.client.result.UpdateResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
    ApiService apiService;
    @Autowired
    UserService userService;
    @Autowired
    TagService tagService;
    @Value("${image.access.url}")
    private String accessPref;

    /**
     * 创建图集
     *
     * @param files
     * @param atlas
     * @return
     * @throws SystemException
     */
    @RequestMapping(value = "/atlas", method = RequestMethod.POST)
    public JSONResult createOneAtlas(@RequestParam("images") MultipartFile[] files, Atlas atlas) throws SystemException {
        List<String> imageList = new ArrayList<>();
        if (null != files && files.length > 0) {
            //多图片处理
            for (MultipartFile file : files) {
                String relativePath = apiService.singleImageUpload(file, Dict.GLOBAL_WATERMARK, FileSourceEnum.ATLAS);
                imageList.add(relativePath);
            }
        }
        logger.info("图片数量:" + files.length + ",图片相对路径：" + imageList);
        Date nowDate = new Date();
        atlas.setAtlasPictures(imageList);
        atlas.setIdentical(Util.checkImagesProportion(files));
        User currentLoginUser = (User) SecurityUtils.getSubject().getPrincipal();
        //处理标签，新建或者更新
        List<Tag> tagList = atlas.getAtlasTags();
        if (tagList != null) {
            tagService.handleTagList(tagList,true);
        } else {
            //空时添加空list
            atlas.setAtlasTags(new ArrayList<>());
        }
        atlas.setAtlasStatus(Dict.TAG_STATUS_NORMAL);
        atlas.setCreater(currentLoginUser.getUserId());
        atlas.setSendTime(nowDate);
        mongoTemplate.insert(atlas);
        String objectId = atlas.getAtlasId();
        //TODO 需要将id存入mysql
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
     * @deprecated
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
     * 根据图集Id查看详情//TODO 编辑和查看未作区分
     *
     * @param atlasId
     * @return
     * @throws SystemException
     */
    @StaticURL
    @RequestMapping(value = "/atlas/{atlasId}", method = RequestMethod.GET)
    public JSONResult queryAtlasDetailById(@PathVariable String atlasId) throws SystemException {

        Atlas atlas = mongoTemplate.findById(atlasId, Atlas.class);
        if (atlas == null) {
            throw new SystemException(ExceptionEnums.SELECT_DATA_IS_EMPTY);
        }
        //如果是仅个人可见
        if (atlas.isPersonal()) {
            User currentLoginUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (currentLoginUser == null || !atlas.getCreater().equals(currentLoginUser.getUserId())) {
                throw new SystemException(ExceptionEnums.SOURCE_NOT_PERMIT);
            }
        }
        //如果允许评论，查询评论
//        if(atlas.isComment()){
//            List<Comment> comments = new ArrayList<>();
//        }
//        User currentLoginUser = (User) SecurityUtils.getSubject().getPrincipal();
        User user = userService.queryCommonInfo(atlas.getCreater());
        atlas.setUser(user);
        return new JSONDataResult().add("atlas", atlas);
    }

    /**
     * 删除指定图集
     *
     * @param atlasId
     * @return
     * @throws SystemException
     */
    @RequestMapping(value = "/atlas/{atlasId}", method = RequestMethod.DELETE)
    public JSONResult removeAtlasByAtlasId(@PathVariable String atlasId) throws SystemException {
        User currentLoginUser = (User) SecurityUtils.getSubject().getPrincipal();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(atlasId));
        query.addCriteria(Criteria.where("creater").is(currentLoginUser.getUserId()));
        UpdateResult result = mongoTemplate.updateFirst(query, new Update().set("atlasStatus", Dict.TAG_STATUS_DELETED), "atlas");
        if (result.getModifiedCount() < 1) {
            throw new SystemException(ExceptionEnums.DATA_DELETE_FAIL);
        }
        return JSONDataResult.success();
    }

    /**
     * 图集修改<br>
     * 不允许图片修改
     *
     * @param
     * @return
     * @throws SystemException
     */
    @RequestMapping(value = "/atlas", method = RequestMethod.PUT)
    public JSONResult modifyAtlas(@RequestBody Atlas atlas) throws SystemException {
        User currentLoginUser = (User) SecurityUtils.getSubject().getPrincipal();
        if(atlas.getAtlasTags()!=null){
            //TODO 不应该再把标签热度+1，tip：是否只
            tagService.handleTagList(atlas.getAtlasTags(),false);
        }
        Query query = new Query().addCriteria(Criteria.where("_id").is(atlas.getAtlasId())).addCriteria(Criteria.where("creater").is(currentLoginUser.getUserId()));
        //向数组末尾增加元素
        Update update = new Update().push("atlasContent",atlas.getAtlasContent().get(atlas.getAtlasContent().size()-1));
        update.set("atlasTags",atlas.getAtlasTags());
        update.set("updater",currentLoginUser.getUserId());
        update.set("updateTime",new Date());
        update.set("personal",atlas.isPersonal());
        update.set("comment",atlas.isComment());
        UpdateResult result = mongoTemplate.updateFirst(query,update,Atlas.class);
        if (result.getModifiedCount() < 1) {
            throw new SystemException(ExceptionEnums.DATA_UPDATE_FAIL);
        }
        return JSONDataResult.success();
    }

    public JSONResult modify() {
        List<Atlas> atlases = mongoTemplate.findAll(Atlas.class);
        for (Atlas atlas : atlases) {
            List<String> list = new ArrayList<>();
            list.add(atlas.getAtlasContent().toString());
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(atlas.getAtlasId()));
            mongoTemplate.updateFirst(query, new Update().set("atlasContent", list), Atlas.class);
        }

        return JSONDataResult.success();
    }

}
