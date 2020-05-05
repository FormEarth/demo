package com.example.demo.service.impl;

import com.example.demo.common.Dict;
import com.example.demo.common.SequenceNumber;
import com.example.demo.entity.*;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.mapper.ArticleMapper;
import com.example.demo.mapper.ArticleTagMapper;
import com.example.demo.util.MarkdownUtil;
import com.example.demo.util.Util;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.service.ArticleService;

import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * @author raining_heavily
 */
@Service
public class ArticleServiceImpl extends BaseServiceImpl<Article> implements ArticleService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

//    private static String blogPath;
//    private static String accessPref;
    @Value(value = "${static.blog.generate.shell}")
    private String[] shell;
    @Autowired
    ArticleTagMapper articleTagMapper;
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void articleHandle(Writing article) throws SystemException {
        Date date = new Date();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        article.setWritingId(SequenceNumber.getInstance().getNextSequence(Dict.SEQUENCE_TYPE_WRITING));
        article.setSummary(MarkdownUtil.toText(article.getContent(), 200));
        article.setCreateTime(date);
        article.setType(Dict.WRITING_TYPE_ARTICLE);
        article.setSendTime(date);
        article.setStatus(Dict.WRITING_STATUS_NORMAL);
        article.setCreatorId(user.getUserId());
        article.setPageview(0L);
        mongoTemplate.insert(article);
        if (article.getSaveToFile()) {
            try {
                MarkdownUtil.writeContentToFile(article);
            } catch (IOException e) {
                throw new SystemException(ExceptionEnums.FILE_WRITE_FAIL);
            }
            //调用shell使hexo重新生成静态页面
            Util.shellExecute(shell);
        }
    }

    @Override
    public List<Article> queryArticleInHome(long currentPage) {
        return articleMapper.queryArticlesWithPage((currentPage-1)*7);
    }

    @Override
    public Article queryArticleDetailById(long articleId) throws SystemException {
        Article article = articleMapper.queryArticleDetailById(articleId);
        if(article==null){
            throw new SystemException(ExceptionEnums.SOURCE_NOT_FOUND);
        }else if(article.getPersonal()){
            throw new SystemException(ExceptionEnums.SOURCE_NOT_PERMIT);
        }else{
            //阅读量+1
            Article article1 = new Article();
//            article1.setArticleId(article.getArticleId());
//            article1.setReaderNum(article.getReaderNum()+1);
            articleMapper.updateById(article1);
        }
        return article;
    }


}
