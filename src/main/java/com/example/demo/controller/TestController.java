package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.demo.entity.JSONDataResult;
import com.example.demo.entity.JSONResult;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.service.PermissionService;
import com.example.demo.service.RoleService;

@RestController
@RequestMapping(value="/api/test")
public class TestController {
	
	@Autowired
    private JavaMailSender javaMailSender;
	
	@Autowired
    private TemplateEngine templateEngine;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	PermissionService permissionService;

	/**
	 * 测试接口
	 * @return
	 */
	@RequestMapping(value="/test",method=RequestMethod.GET)
	public JSONResult test() {
		String age = "123";
		List<String> list = new ArrayList<>();
		list.add("1111111");
		list.add("2222222");
		list.add("3333333");
		return new JSONDataResult().add("age", age).add("list", list);
	}
	
	/**
	 * 发送html邮件
	 * @return
	 */
	@RequestMapping(value="/sendEmail",method=RequestMethod.GET)
	public JSONResult sendEmail() {
	    MimeMessage message = null;
	    try {
	        message = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
	        helper.setFrom("1194539573@qq.com");
	        helper.setTo("18203649358@163.com");
	        helper.setSubject("标题：发送Html内容");

	        StringBuffer sb = new StringBuffer();
	        sb.append("<h1>大标题-h1</h1>")
	          .append("<p style='color:#F00'>红色字</p>")
	          .append("<p style='text-align:right'>右对齐</p>");
	        helper.setText(sb.toString(), true);
	    } catch (MessagingException e) {
	        e.printStackTrace();
	    }
	    javaMailSender.send(message);
		return JSONResult.success();
	}
	
	/**
	 * 发送模板邮件
	 * @return
	 */
	@RequestMapping(value="/sendTemplateEmail",method=RequestMethod.GET)
	public JSONResult sendTemplateEmail() {
	    MimeMessage message = null;
	    try {
	        message = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
	        helper.setFrom("1194539573@qq.com");
	        helper.setTo("18203649358@163.com");
	        helper.setSubject("【验证码】注册验证码");
	        Map<String, Object> map = new HashMap<>();
	        map.put("verificationCode", "12345");

	        Context context = new Context(); //设置参数        
	        context.setVariables(map);        //emailTemplate为模板文件的文件名，即html demo的文件名        
	        String tempContext = templateEngine.process("/email/verification_code", context);
	      
	        helper.setText(tempContext, true);
	        javaMailSender.send(message);
	    } catch (MessagingException e) {
	        e.printStackTrace();
	    }
		return JSONResult.success();
	}
	
	@RequestMapping(value="/roles",method=RequestMethod.GET)
	public JSONResult getRolesByAccount(@RequestParam String account) {
		
		List<Role> list = roleService.getRolesByAccount(account);
		return new JSONDataResult().add("roles", list);
	}
	
	@RequestMapping(value="/permissions",method=RequestMethod.GET)
	public JSONResult getPermissionsByAccount(@RequestParam String account) {
		
		List<Permission> list = new ArrayList<>();
		list =	permissionService.getPermissionsByAccount(account);
		System.out.println("+++++++"+list.size());
		list.stream().forEach(System.out::print);
		return new JSONDataResult().add("permissions", list);
	}
}
