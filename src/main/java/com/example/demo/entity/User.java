package com.example.demo.entity;

import java.sql.Date;
import java.sql.Timestamp;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "user")
public class User {

	// 若没有开启驼峰命名，或者表中列名不符合驼峰规则，可通过该注解指定数据库表中的列名，exist标明数据表中有没有对应列
	// @TableField(value = "sex",exist = true)
	/** 用户id */
	@TableId(value = "user_id", type = IdType.AUTO) // 指定该字段为自增策略
	private long userId;
	/** 账号 */
	@NotBlank(message = "账号不能为空")
	private String account;
	/** 密码 */
	@JSONField(serialize = false)//设置该字段不序列化
	private String password;
	// 该字段在数据库中不存在
	@TableField(exist = false)
	/** 记住我 */
	private boolean remberMe;
	/** 盐值 */
	@JSONField(serialize = false)
	private String salt;
	@Size(min = 3, max = 10, message = "用户名在{min}~{max}之间")
	/** 用户姓名 */
	private String userName;
	/** 个性签名 */
	private String sign;
	/** 个人简介 */
	private String personalProfile;
	/** 生日 */
	private Date birthday;
	/** 性别 */
	private boolean sex;
	/** 头像路径 */
	private String avatar;
	/** 手机号 */
	private String phone;
	/** 邮箱 */
	@Email(message = "邮箱格式不正确")
	private String mail;
	/**
	 * 用户状态<br>
	 * 账号状态（0：正常；1：锁定；2：删除）
	 */
	private String userStatus;
	/** 创建时间 */
	private Date createTime;
	/** 更新时间 */
	private Timestamp updateTime;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRemberMe() {
		return remberMe;
	}

	public void setRemberMe(boolean remberMe) {
		this.remberMe = remberMe;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getPersonalProfile() {
		return personalProfile;
	}

	public void setPersonalProfile(String personalProfile) {
		this.personalProfile = personalProfile;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public boolean isSex() {
		return sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	public String getAvatar() {
		return "http://192.168.149.110:9090/static" + avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", account=" + account + ", password=" + password + ", remberMe=" + remberMe
				+ ", salt=" + salt + ", userName=" + userName + ", sign=" + sign + ", personalProfile="
				+ personalProfile + ", birthday=" + birthday + ", sex=" + sex + ", avatar=" + avatar + ", phone="
				+ phone + ", mail=" + mail + ", userStatus=" + userStatus + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + "]";
	}

}
