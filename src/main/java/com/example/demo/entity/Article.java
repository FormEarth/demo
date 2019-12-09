package com.example.demo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.aop.annotation.StaticURL;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 文章
 *
 * @author raining_heavily
 * @date 2019年3月31日
 */

@TableName(value = "article")
public class Article {

    /**
     * 文章id 指定该字段为自增策略
     **/
    @TableId(value = "article_id", type = IdType.AUTO)
    private Long articleId;
    /**
     * 作者姓名
     **/
    private String authorName;
    /**
     * 作者id(即用户id)
     **/
    private Long author;
    /**
     * 文章标题
     **/
    private String title;
    /**
     * 文章封面
     **/
    @StaticURL
    private String frontCover;
    /**
     * 文章摘要
     **/
    private String summary;
    /**
     * 文章内容
     **/
    private String content;
    /**
     * 文章标签
     **/
    private String tags;
    /**
     * 代码样式
     **/
    private String codeStyle;
    /**
     * 非公开文章
     **/
    private Boolean personal;
    /**
     * 匿名
     **/
    private Boolean anonymous;
    /**
     * 允许评论
     **/
    private Boolean comment;
    /**
     * 发布时间
     **/
    private Date sendTime;
    /**
     * 状态，0-创建；1-发布；2-未发布(草稿箱)；3-删除
     **/
    private Integer status;
    /**
     * 浏览量
     **/
    private Long readerNum;
    /**
     * 赞成
     **/
    private Long approval;
    /**
     * 反对
     **/
    private Long oppose;

    /**
     * 创建人
     **/
    private Long creater;
    /**
     * 创建时间
     **/
    private Date createTime;
    /**
     * 更改人
     **/
    private Long updater;
    /**
     * 更改时间
     **/
    private Date updateTime;

    @Override
    public String toString() {
        return "Article [articleId=" + articleId + ", authorName=" + authorName + ", author=" + author + ", title=" + title
                + ", frontCover=" + frontCover + ", summary=" + summary + ", content=" + content + ", tags=" + tags
                + ", codeStyle=" + codeStyle + ", personal=" + personal + ", anonymous=" + anonymous + ", comment="
                + comment + ", sendTime=" + sendTime + ", status=" + status + ", readerNum=" + readerNum + ", approval="
                + approval + ", oppose=" + oppose + ", creater=" + creater + ", createTime=" + createTime + ", updater="
                + updater + ", updateTime=" + updateTime + "]";
    }

    /**
     * 解决mybatis查到的Date数据为long型的问题
     **/

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getSendTime() {
        return sendTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getCreateTime() {
        return createTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getUpdateTime() {
        return updateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFrontCover() {
        return frontCover;
    }

    public void setFrontCover(String frontCover) {
        this.frontCover = frontCover;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCodeStyle() {
        return codeStyle;
    }

    public void setCodeStyle(String codeStyle) {
        this.codeStyle = codeStyle;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }


    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getAuthor() {
        return author;
    }

    public void setAuthor(Long author) {
        this.author = author;
    }

    public Boolean getPersonal() {
        return personal;
    }

    public void setPersonal(Boolean personal) {
        this.personal = personal;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

    public Boolean getComment() {
        return comment;
    }

    public void setComment(Boolean comment) {
        this.comment = comment;
    }

    public Long getReaderNum() {
        return readerNum;
    }

    public void setReaderNum(Long readerNum) {
        this.readerNum = readerNum;
    }

    public Long getApproval() {
        return approval;
    }

    public void setApproval(Long approval) {
        this.approval = approval;
    }

    public Long getOppose() {
        return oppose;
    }

    public void setOppose(Long oppose) {
        this.oppose = oppose;
    }

    public Long getCreater() {
        return creater;
    }

    public void setCreater(Long creater) {
        this.creater = creater;
    }

    public Long getUpdater() {
        return updater;
    }

    public void setUpdater(Long updater) {
        this.updater = updater;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setUpdater(long updater) {
        this.updater = updater;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

}
