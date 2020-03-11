package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

@TableName("demo_tag")
public class Tag {
    @TableId(type = IdType.AUTO)
    private Long tagId;
    private String tagCategory;
    private String tagText;
    private Integer tagStatus;
    private String tagWiki;
    private String tagColor;
    private Long creater;
    private Date createTime;
    private Long updater;
    private Date updateTime;
    private Long tagHot;

    @Override
    public String toString() {
        return "Tag{" +
                "tagId=" + tagId +
                ", tagCategory='" + tagCategory + '\'' +
                ", tagText='" + tagText + '\'' +
                ", tagStatus=" + tagStatus +
                ", tagWiki='" + tagWiki + '\'' +
                ", tagColor='" + tagColor + '\'' +
                ", creater=" + creater +
                ", createTime=" + createTime +
                ", updater=" + updater +
                ", updateTime=" + updateTime +
                ", tagHot=" + tagHot +
                '}';
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTagCategory() {
        return tagCategory;
    }

    public void setTagCategory(String tagCategory) {
        this.tagCategory = tagCategory;
    }

    public String getTagText() {
        return tagText;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText;
    }

    public Integer getTagStatus() {
        return tagStatus;
    }

    public void setTagStatus(int tagStatus) {
        this.tagStatus = tagStatus;
    }

    public String getTagWiki() {
        return tagWiki;
    }

    public void setTagWiki(String tagWiki) {
        this.tagWiki = tagWiki;
    }

    public String getTagColor() {
        return tagColor;
    }

    public void setTagColor(String tagColor) {
        this.tagColor = tagColor;
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

    public Long getTagHot() {
        return tagHot;
    }

    public void setTagHot(Long tagHot) {
        this.tagHot = tagHot;
    }
}
