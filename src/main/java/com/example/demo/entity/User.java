package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.aop.annotation.StaticURL;
import com.example.demo.exception.SystemException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;


/**
 * @author raining_heavily
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName(value = "user")
public class User implements Serializable, Cloneable {

    // 若没有开启驼峰命名，或者表中列名不符合驼峰规则，可通过该注解指定数据库表中的列名，exist标明数据表中有没有对应列
    // @TableField(value = "sex",exist = true)
    /**
     * 用户id,该字段为自增策略
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;
    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空")
    String account;
    /**
     * 密码,,该字段不序列化
     */
    private String password;
    /**
     * 盐值
     */
    private String salt;
    @Size(min = 3, max = 10, message = "用户名在{min}~{max}之间")
    /** 用户姓名 */
    private String userName;
    /**
     * 个性签名
     */
    private String sign;
    /**
     * 个人简介
     */
    private String personalProfile;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 头像路径
     */
    @StaticURL
    private String avatar;
    /**
     * 个人封面图路径
     */
    @StaticURL
    private String frontCover;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String mail;
    /**
     * 用户状态<br>
     * 账号状态（0：正常；1：锁定；2：删除）
     */
    private String userStatus;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新人id
     */
    private Long updater;
    /**
     * 更新时间
     */
    private Timestamp updateTime;

    @Override
    public User clone() throws CloneNotSupportedException {
        return (User) super.clone();
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", account=" + account + ", password=" + password
                + ", salt=" + salt + ", userName=" + userName + ", sign=" + sign + ", personalProfile="
                + personalProfile + ", birthday=" + birthday + ", sex=" + sex + ", avatar=" + avatar + ", frontCover="
                + frontCover + ", phone=" + phone + ", mail=" + mail + ", userStatus=" + userStatus + ", createTime="
                + createTime + ", updater=" + updater + ", updateTime=" + updateTime + "]";
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

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getUpdater() {
        return updater;
    }

    public void setUpdater(Long updater) {
        this.updater = updater;
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getFrontCover() {
        return frontCover;
    }

    public void setFrontCover(String frontCover) {
        this.frontCover = frontCover;
    }

}
