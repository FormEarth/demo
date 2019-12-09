package com.example.demo.common;
/**
 * 
 * @description 公共的静态变量
 * @author raining_heavily
 * @date 2019年3月5日
 */
public class Dict {

	/** 已登录用户数据 **/
	public static final String CURRENT_USER_DATA = "current_user_data";
	/** 文件资源的访问前缀 **/
	public static final String ACCESS_PREF = "access_pref";
	/** 用户状态：正常   **/
	public static final String NORMAL_STATUS = "0";
	/** 用户状态：锁定   **/
	public static final String INVALID_STATUS = "1";
	/** 用户状态：注销   **/
	public static final String LOCKED_STATUS = "2";
	/** 标签状态：正常   **/
	public static final int TAG_STATUS_NORMAL = 1;
	/** 标签状态：屏蔽  **/
	public static final int TAG_STATUS_SHIELD = 2;
	/** 标签状态：已删除   **/
	public static final int TAG_STATUS_DELETED = 3;
	/** 全局水印 **/
	public static String GLOBAL_WATERMARK = "demooo";
	/** 标签ID **/
	public static final String TAG_ID = "tagId";
	/** 标签文本 **/
	public static final String TAG_TEXT = "tagText";
	/** 请求方法get **/
	public static final String GET = "GET";
	/** 登录用户的token **/
	public static final String LOGIN_USER_TOKEN = "login_user_token";
	/** 头部保存sessionId的字段 **/
	public static final String AUTHORIZATION = "Authorization-Sessionid";
	/** 文章状态：创建   **/
	public static final int ARTICLE_STATUS_CREATE = 0;
	/** 文章状态：正常   **/
	public static final int ARTICLE_STATUS_NORMAL = 1;
	/** 标签状态：屏蔽  **/
	public static final int ARTICLE_STATUS_BLOCK = 2;
	/** 标签状态：已删除   **/
	public static final int ARTICLE_STATUS_DELETED = 3;
	/** 头像   **/
	public static final String AVATAR = "AVATAR";
	/** 个人封面  **/
	public static final String FRONTCOVER = "FRONTCOVER";
}
