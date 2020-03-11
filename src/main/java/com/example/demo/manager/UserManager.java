package com.example.demo.manager;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.example.demo.entity.User;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
/**
 * @author raining_heavily
 * @Description 用户管理
 * @date 2020年3月10日
 */
public class UserManager {

	private UserManager() { };

	private static ConcurrentHashMap<Long, UserInfo> loginUsers;

	private static class UserManagerHelper {
		static UserManager userManager = new UserManager();
	}

	public static UserManager getInstance() {
		// 移除超时用户
		if(loginUsers != null) {
			for (Entry<Long, UserInfo> entry : loginUsers.entrySet()) {
				UserInfo userInfo = entry.getValue();
				loginUsers.remove(entry.getKey());
			}
		}
		return UserManagerHelper.userManager;
	}

	/**
	 * 根据id获取用户信息
	 * 
	 * @param userId
	 * @return
	 * @throws SystemException
	 */
	public UserInfo getUserInfo(long userId) throws SystemException {
		UserInfo userInfo = loginUsers.get(userId);
		if(userInfo == null) throw new SystemException(ExceptionEnums.USER_NOT_LOGIN);
		LocalDateTime ldt = LocalDateTime.now().plusDays(3);
		LocalDateTime tokenDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(userInfo.getTime()),
				ZoneId.systemDefault());
		if (tokenDateTime.isBefore(ldt)) {
			loginUsers.remove(userInfo.getUserId());
			throw new SystemException(ExceptionEnums.LOGIN_STATUS_TIMEOUT);
		}
		return userInfo;
	}

	/**
	 * 设置用户信息
	 * 
	 * @param user
	 */
	public void setUserInfo(User user) {
		UserInfo userInfo = (UserInfo) user;
		userInfo.setTime(System.currentTimeMillis());
		loginUsers.put(user.getUserId(), userInfo);
	}

	public class UserInfo extends User {

		private static final long serialVersionUID = 1L;
		/** 是否记住我 **/
		private boolean rememberMe;
		/** token **/
		private String token;
		/** 时间戳 **/
		private long time;

		public boolean isRememberMe() {
			return rememberMe;
		}

		public void setRememberMe(boolean rememberMe) {
			this.rememberMe = rememberMe;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public long getTime() {
			return time;
		}

		public void setTime(long time) {
			this.time = time;
		}

	}
}
