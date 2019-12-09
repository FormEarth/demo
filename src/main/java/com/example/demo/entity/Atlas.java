package com.example.demo.entity;

import com.example.demo.aop.annotation.StaticURL;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

/**
 * 图集对象
 * @author raining_heavily
 * @date 2019/11/3 22:19
 **/
public class Atlas {

    @Id
    /** mongoDB的主键 **/
    private String atlasId;
    private Integer atlasStatus;
    /** 图片 **/
    @StaticURL
    private List<String> atlasPictures;
    /** 描述 **/
    private List<String> atlasContent;
    /** 标签 **/
    private List<Tag> atlasTags;
    /** 来源 **/
    private String source;
    /** 位置 **/
    private String location;
    /** 图片比例是否一致 **/
    private Boolean identical;
    /** 允许评论 **/
    private Boolean comment;
    /** 仅自己可见 **/
    private Boolean personal;
    /** 发布时间 **/
    private Date sendTime;
    /** 创建人 **/
    private Long creater;
    /** 创建时间 **/
    private Date createTime;
    /** 更新人 **/
    private Long updater;
    /** 更新时间 **/
    private Date updateTime;
    /** 关联的用户信息 **/
    private User user;

    @Override
    public String toString() {
        return "Atlas{" +
                "atlasId='" + atlasId + '\'' +
                ", atlasStatus=" + atlasStatus +
                ", atlasPictures=" + atlasPictures +
                ", atlasContent='" + atlasContent + '\'' +
                ", atlasTags=" + atlasTags +
                ", source='" + source + '\'' +
                ", location='" + location + '\'' +
                ", identical=" + identical +
                ", comment=" + comment +
                ", personal=" + personal +
                ", sendTime=" + sendTime +
                ", creater=" + creater +
                ", createTime=" + createTime +
                ", updater=" + updater +
                ", updateTime=" + updateTime +
                ", user=" + user +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAtlasId() {
        return atlasId;
    }

    public void setAtlasId(String atlasId) {
        this.atlasId = atlasId;
    }

    public Boolean isPersonal() {
        return personal;
    }

    public Boolean isIdentical() {
        return identical;
    }

    public void setIdentical(Boolean identical) {
        this.identical = identical;
    }

    public void setPersonal(Boolean personal) {
        this.personal = personal;
    }

    public int getAtlasStatus() {
        return atlasStatus;
    }

    public void setAtlasStatus(int atlasStatus) {
        this.atlasStatus = atlasStatus;
    }

    public List<String> getAtlasPictures() {
        return atlasPictures;
    }

    public void setAtlasPictures(List<String> atlasPictures) {
        this.atlasPictures = atlasPictures;
    }

    public List<String> getAtlasContent() {
        return atlasContent;
    }

    public void setAtlasContent(List<String> atlasContent) {
        this.atlasContent = atlasContent;
    }

    public List<Tag> getAtlasTags() {
        return atlasTags;
    }

    public void setAtlasTags(List<Tag> atlasTags) {
        this.atlasTags = atlasTags;
    }

    public Boolean isComment() {
        return comment;
    }

    public void setAtlasStatus(Integer atlasStatus) {
        this.atlasStatus = atlasStatus;
    }

    public Boolean getIdentical() {
        return identical;
    }

    public Boolean getComment() {
        return comment;
    }

    public Boolean getPersonal() {
        return personal;
    }

    public void setComment(Boolean comment) {
        this.comment = comment;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Long getCreater() {
        return creater;
    }

    public void setCreater(Long creater) {
        this.creater = creater;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdater() {
        return updater;
    }

    public void setUpdater(Long updater) {
        this.updater = updater;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
