package com.example.demo.entity;

import com.example.demo.aop.annotation.StaticURL;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.util.Date;
import java.util.List;

/**
 * 作品
 * @author raining_heavily
 * @date 2020/3/19 15:00
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Writing {
    /** 作品id,mongoDB的主键 **/
    @Id
    private String writingId;
    /** 作品类型，1：文章；2：动态 **/
    private Integer type;
    /** 标签 **/
    private List<Tag> tags;
    /** 文章标题 **/
    private String title;
    /** 文章封面图 **/
    @StaticURL
    private String frontCover;
    /** 文章摘要 **/
    private String summary;
    /** 图片 **/
    @StaticURL
    private List<String> atlasPictures;
    /** 文章是否生成md文件 **/
    private Boolean saveToFile;
    /** 内容 **/
    @TextIndexed
    private String content;
    /** 私人作品 **/
    private Boolean personal;
    /** 来源 **/
    private String source;
    /** 位置 **/
    private String location;
    /** 匿名 **/
    private Boolean anonymous;
    /** 允许评论 **/
    private Boolean comment;
    /** 浏览量 **/
    private Long pageview;
    /** 状态，0-创建；1-发布；2-未发布(草稿箱)；3-删除 **/
    private Integer status;
    /** 创建者id **/
    private Long creatorId;
    /** 创建者id对应的user对象，仅查询使用 **/
    private transient User user;
    /** 是否编辑过 **/
    private Boolean modified;
    /** 上次编辑时间 **/
    private Date modifiedTime;
    /** 修改者id **/
    private Long updaterId;
    /** *创建时间 */
    private Date createTime;
    /** 发布时间 **/
    private Date sendTime;
    /** 修改时间 **/
    private Date updateTime;


    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getSendTime() { return sendTime; }
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getCreateTime() {
        return createTime;
    }
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getUpdateTime() {
        return updateTime;
    }
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getModifiedTime() { return modifiedTime; }
}
