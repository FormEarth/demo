package com.example.demo.controller;

import com.example.demo.aop.annotation.StaticURL;
import com.example.demo.common.Dict;
import com.example.demo.entity.JSONDataResult;
import com.example.demo.entity.JSONResult;
import com.example.demo.entity.User;
import com.example.demo.entity.Writing;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.service.UserService;
import com.mongodb.client.result.UpdateResult;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author raining_heavily
 * @date 2020/3/19 21:54
 **/
@RestController
public class WritingController {

    private final static Logger logger = LoggerFactory.getLogger(WritingController.class);
    /**
     * 分页大小
     **/
    private static final int PAGE_SIZE = 5;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    UserService userService;

    /**
     * 分页查询指定用户id（若有）的所有作品
     *
     * @param page   当前页（可选，默认1）
     * @param userId 用户id（可选，没有查的是全部）
     * @param type   类型 0全部 1文章 2动态（可选，默认0）
     * @return
     */
    @StaticURL
    @RequestMapping(value = "/writings", method = RequestMethod.GET)
    public JSONResult getWritingsByPage(
            @RequestParam(required = false) Integer page, @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer type) {
        System.out.println(page + "," + userId + "," + type);
        Query query = new Query();
        //正常状态
        query.addCriteria(Criteria.where("status").is(Dict.WRITING_STATUS_NORMAL));
        if (type != null && type.compareTo(0) > 0) {
            query.addCriteria(Criteria.where("type").is(type));
        }
        User currentLoginUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (currentLoginUser != null && userId != null) {
            //查自己内容
            if (currentLoginUser.getUserId().equals(userId)) {
//                query.addCriteria(Criteria.where("personal").is(true));
                query.addCriteria(Criteria.where("creatorId").is(userId));
            }
        } else {
            query.addCriteria(Criteria.where("personal").is(false));
        }
        //第一次查询时返回总页数
        JSONDataResult result = new JSONDataResult();
        page = (page == null || page <= 0) ? 1 : page;
        if (page == 1) {
            long total = mongoTemplate.count(query, Writing.class);
            result.add("total", total);
        }
        query.with(new Sort(Sort.Direction.DESC, "sendTime"));
        query.skip((page - 1) * PAGE_SIZE).limit(PAGE_SIZE);
        List<Writing> writings = mongoTemplate.find(query, Writing.class);
        Map<Long, User> users = new HashMap<>();
        writings.stream().map(w -> w.getCreatorId()).distinct().forEach(id -> {
            try {
                users.put(id, userService.queryCommonInfo(id));
            } catch (SystemException e) {
                logger.error("query user info failed by userId:{}!", currentLoginUser.getUserId(), userId);
            }
        });
        for (Writing writing : writings) {
            writing.setUser(users.get(writing.getCreatorId()));
        }
        result.add("writings", writings);
        return result;
    }

    /**
     * 根据作品Id查看详情
     *
     * @param writingId 作品id
     * @param type      类型：edit：编辑；view：查看
     * @return
     * @throws SystemException
     */
    @StaticURL
    @RequestMapping(value = "/writing", method = RequestMethod.GET)
    public JSONResult queryWritingById(String writingId, String type) throws SystemException {

        Writing writing = mongoTemplate.findById(writingId, Writing.class);
        if (writing == null) {
            throw new SystemException(ExceptionEnums.SELECT_DATA_IS_EMPTY);
        }
        //如果是仅个人可见
        if (writing.getPersonal()) {
            User currentLoginUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (currentLoginUser == null || !writing.getCreatorId().equals(currentLoginUser.getUserId())) {
                throw new SystemException(ExceptionEnums.SOURCE_NOT_PERMIT);
            }
        }

        if (!"edit".equals(type)) {
            long pageView = writing.getPageview() == null ? 0 : writing.getPageview();
            mongoTemplate.updateFirst(new Query().addCriteria(Criteria.where("_id").is(writingId)), new Update().set("pageview", pageView + 1), "writing");
        }

        User user = userService.queryCommonInfo(writing.getCreatorId());
        writing.setUser(user);
        return new JSONDataResult().add("writing", writing);
    }

    /**
     * 删除指定作品
     * //TODO 同时删除图片
     *
     * @param writingId
     * @return
     * @throws SystemException
     */
    @RequestMapping(value = "/writing/{writingId}", method = RequestMethod.DELETE)
    public JSONResult removeAtlasByAtlasId(@PathVariable String writingId) throws SystemException {
        User currentLoginUser = (User) SecurityUtils.getSubject().getPrincipal();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(writingId));
        query.addCriteria(Criteria.where("creatorId").is(currentLoginUser.getUserId()));
        //更新状态
        UpdateResult result = mongoTemplate.updateFirst(query, new Update().set("status", Dict.WRITING_STATUS_DELETED), "atlas");
        if (result.getModifiedCount() < 1) {
            logger.error("{} delete {} failed!", currentLoginUser.getUserId(), writingId);
            throw new SystemException(ExceptionEnums.DATA_DELETE_FAIL);
        }
        return JSONDataResult.success();
    }

    @RequestMapping(value = "/writing/search", method = RequestMethod.GET)
    public JSONResult search(@RequestParam String keyWord, @RequestParam Long page, @RequestParam Long user_id) {
        User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
        page = page == null ? 0 : page;
        Query query = new Query().skip(page).limit(5);
        if (user_id != null) {
            query.addCriteria(Criteria.where("creator").is(user_id));
            if (loginUser != null && loginUser.equals(user_id)) {
                //非公开且创建者是自己的
                query.addCriteria(Criteria.where("personal").is(true));
            } else {
                query.addCriteria(Criteria.where("personal").is(false));
            }
        }

        //忽略大小写
        query.addCriteria(Criteria.where("content").regex(keyWord, "i"));
        query.with(Sort.by(Sort.Direction.DESC, "sendTime"));
        List<Writing> writings = mongoTemplate.find(query, Writing.class);
        return new JSONDataResult().add("writings", writings);
    }

}
