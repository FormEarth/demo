package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 文章和标签的关联表
 *
 * @author raining_heavily
 * @date 2019/12/11 12:16
 **/
@TableName(value = "demo_article_tag")
public class ArticleTag {
    private Long articleId;
    private Long tagId;

    public ArticleTag() {
    }

    public ArticleTag(Long articleId, Long tagId) {
        this.articleId = articleId;
        this.tagId = tagId;
    }

    @Override
    public String toString() {
        return "ArticleTag{" +
                "articleId=" + articleId +
                ", tagId=" + tagId +
                '}';
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
}
