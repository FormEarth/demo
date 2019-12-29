package com.example.demo.service;

import com.example.demo.entity.Article;
import com.example.demo.exception.SystemException;

import java.util.List;

/**
 * @author raining_heavily
 */
public interface ArticleService extends BaseService<Article> {

    /**
     * 创建文章时的文章处理
     * @param article
     * @throws SystemException
     */
    void articleHandle(Article article) throws SystemException;

    /**
     * 首页文章查询
     * @param currentPage
     * @return
     */
    List<Article> queryArticleInHome(long currentPage);

    /**
     * 根据Id查询文章
     * @param articleId
     * @return
     * @throws SystemException
     */
    Article queryArticleDetailById(long articleId) throws SystemException;

}
