package com.example.demo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @description 文章
 * @author raining_heavily
 * @date 2019年3月31日
 */

@TableName(value = "artice")
public class Artice {

	/** 文章id **/
	@TableId(value = "artice_id", type = IdType.AUTO) // 指定该字段为自增策略
	private long articeId;
	/** 作者姓名 **/
	private String authorName;
	/** 作者id(即用户id) **/
	private long author;
	/** 文章标题 **/
	private String title;
	/** 文章封面 **/
	private String frontCover;
	/** 文章摘要 **/
	private String summary;
	/** 文章内容 **/
	private String content;
	/** 文章标签 **/
	private String tags;
	/** 代码样式 **/
	private String codeStyle;
	/** 非公开文章 **/
	private boolean personal;
	/** 匿名 **/
	private boolean anonymous;
	/** 允许评论 **/
	private boolean comment;
	/** 发布时间 **/
	private Date sendTime;
	/** 状态，0-创建；1-发布；3-未发布(草稿箱)；4-删除 **/
	private String status;
	/** 浏览量 **/
	private int readerNum;
	/** 赞成 **/
	private int approval;
	/** 反对 **/
	private int oppose;

	/** 创建人 **/
	private long creater;
	/** 创建时间 **/
	private Date createTime;
	/** 更改人 **/
	private long updater;
	/** 更改时间 **/
	private Date updateTime;

	@Override
	public String toString() {
		return "Artice [articeId=" + articeId + ", authorName=" + authorName + ", author=" + author + ", title=" + title
				+ ", frontCover=" + frontCover + ", summary=" + summary + ", content=" + content + ", tags=" + tags
				+ ", codeStyle=" + codeStyle + ", personal=" + personal + ", anonymous=" + anonymous + ", comment="
				+ comment + ", sendTime=" + sendTime + ", status=" + status + ", readerNum=" + readerNum + ", approval="
				+ approval + ", oppose=" + oppose + ", creater=" + creater + ", createTime=" + createTime + ", updater="
				+ updater + ", updateTime=" + updateTime + "]";
	}

	// @JsonFormat，解决mybatis查到的Date数据为long型的问题
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

	public long getArticeId() {
		return articeId;
	}

	public void setArticeId(long articeId) {
		this.articeId = articeId;
	}

	public long getAuthor() {
		return author;
	}

	public void setAuthor(long author) {
		this.author = author;
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

	public boolean isPersonal() {
		return personal;
	}

	public void setPersonal(boolean personal) {
		this.personal = personal;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}

	public boolean isComment() {
		return comment;
	}

	public void setComment(boolean comment) {
		this.comment = comment;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getReaderNum() {
		return readerNum;
	}

	public void setReaderNum(int readerNum) {
		this.readerNum = readerNum;
	}

	public int getApproval() {
		return approval;
	}

	public void setApproval(int approval) {
		this.approval = approval;
	}

	public int getOppose() {
		return oppose;
	}

	public void setOppose(int oppose) {
		this.oppose = oppose;
	}

	public long getCreater() {
		return creater;
	}

	public void setCreater(long creater) {
		this.creater = creater;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public long getUpdater() {
		return updater;
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
