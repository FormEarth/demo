package com.example.demo.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @description 自定义密码校验方式
 * @author raining_heavily
 * @date 2019年2月28日
 */
public class CredentialMatcher extends SimpleCredentialsMatcher{

	private static final Logger logger = LoggerFactory.getLogger(CredentialMatcher.class);
	
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		//UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		//String password =new String((char[]) token.getCredentials());
        //String password = new String(usernamePasswordToken.getPassword());//用户输入的密码
        //String dbPassword  = (String) info.getPrincipals().getPrimaryPrincipal();
        //String dbPassword = user.getPassword();
		String tokenPassword = String.valueOf((char[]) token.getCredentials());
		String infoPassword = String.valueOf(info.getCredentials());
		logger.info("tokenPassword"+tokenPassword);
		logger.info("infoPassword"+infoPassword);
		logger.info("info.getPrincipals()"+info.getPrincipals());
		
        return this.equals(tokenPassword, infoPassword);
	}

	
}
