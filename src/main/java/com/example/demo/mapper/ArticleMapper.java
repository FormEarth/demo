package com.example.demo.mapper;

import com.example.demo.entity.Article;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article>{

    /**
     * 首页文章分页查询
     * @param currentPage 当前页
     * @return
     */
    List<Article> queryArticlesWithPage(long currentPage);

    /**
     * 根据id查文章详情
     * @param articleId
     * @return
     */
    Article queryArticleDetailById(long articleId);

}
