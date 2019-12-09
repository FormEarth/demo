package com.example.demo.exception;
/**
 * 
 * @Description 异常枚举类,可以预知的异常都定义在这里
 * @author raining_heavily
 * @date 2019年2月17日
 */
public enum ExceptionEnums {

	/** 用户不存在 */
	USER_NOT_EXIT("1000","账号不存在"),
	/** 密码错误 */
	PASSWORD_ERROR("1001","密码错误"),
	/** 用户未登录 */
	USER_NOT_LOGIN("1002","用户未登录"),
	/** 登录状态超时 */
	LOGIN_STATUS_TIMEOUT("1003","登录状态已失效"),
	/** 登录失败 */
	LOGIN_FAIL("1004","登录失败"),
	/** 未知异常 */
	UNKNOWN_ERROR("1005","出错啦，请稍后再试"),
	/** 权限不足 */
	UNAUTH_ERROR("1006","您没有权限访问该功能"),
	/** 用户被锁定 */
	USER_STATUS_IS_LOCK("1007","当前用户被锁定"),
	/** 用户已注销 */
	USER_STATUS_IS_INVALID("1008","用户已注销"),
	/** 404 */
	API_NOT_FOUND("404","请求地址不存在"),
	/** 文件写入失败 */
	FILE_WRITE_FAIL("1009","文件写入失败"),
	/** 图片文件为空 */
	IMAGE_FILE_NULL("1010","图片文件为空"),
	/** 图片压缩出错 */
	IMAGE_FILE_COMPRESSION_FAIL("1011","图片压缩失败"),
	/** 图片上传失败 */
	IMAGE_FILE_UPLOAD_FAIL("1012","图片上传失败"),
	/** 数据插入失败 */
	DATA_INSERT_FAIL("1013","数据插入失败"),
	/** 数据删除失败 */
	DATA_DELETE_FAIL("1014","数据删除失败"),
	/** 数据查询失败 */
	DATA_SELECT_FAIL("1015","数据查询失败"),
	/** 数据更新失败 */
	DATA_UPDATE_FAIL("1016","数据更新失败"),
	/** 登出失败 */
	LOGOUT_FAIL("1017","登出失败"),
	/** 访问的资源不存在  */
	SOURCE_NOT_FOUND("1018","访问的资源不存在"),
	/** 无权访问当前资源  */
	SOURCE_NOT_PERMIT("1019","当前资源不允许访问"),
	/** 未查询到指定数据  */
	SELECT_DATA_IS_EMPTY("1020","未查询到相关结果"),
	/** 请求数据非法  */
	REQUEST_DATA_IS_ILLEGAL("1021","请求数据非法"),
	/** 数据验证失败 */
	FILED_VALIDATOR_ERROR("1111","数据验证失败"),
	/** 在唯一索引行内插入重复数据 */
	SQL_DUPLICATE_ENTRY("1112","数据已存在"),
	/** 默认的成功 */
	DEFAULT_SUCCESS("2000","请求成功"),
	/** 服务器异常 */
	DEFAULT_FAIL("5000","服务器异常");
	
    private String code;
    private String message;

    ExceptionEnums(String code, String message) {
        this.code = code;
        this.message = message;
    }

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

    public static ExceptionEnums statOf(String code) {
        for (ExceptionEnums state : values()) {
			if (state.getCode().equals(code)) {
				return state;
			}
		}
        return null;
    }
    
}
