package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.example.demo.common.Dict;
import com.example.demo.common.SequenceNumber;
import com.example.demo.entity.*;
import com.example.demo.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.demo.service.PermissionService;
import com.example.demo.service.RoleService;

@RestController
public class TestController {
	
	@Autowired
    private JavaMailSender javaMailSender;
	
	@Autowired
    private TemplateEngine templateEngine;

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	PermissionService permissionService;

	@RequestMapping(value = "/ttest", method = RequestMethod.GET)
	public JSONResult modify() {
		List<Writing> atlases = mongoTemplate.findAll(Writing.class);
		for (Writing atlas : atlases) {
			if(atlas.getTags()==null){
				Query query = new Query();
				query.addCriteria(Criteria.where("_id").is(atlas.getWritingId()));
				mongoTemplate.updateFirst(query, new Update().set("tags", new ArrayList<>()), Writing.class);
			}

		}

		return JSONDataResult.success();
	}

	/**
	 * 测试接口
	 * @return
	 */
	@RequestMapping(value="/test",method=RequestMethod.GET)
	public JSONResult test() throws SystemException {
		String image = SequenceNumber.getInstance().getNextSequence(Dict.SEQUENCE_TYPE_IMAGE);
		String writing = SequenceNumber.getInstance().getNextSequence(Dict.SEQUENCE_TYPE_WRITING);
		return new JSONDataResult().add("image_id", image).add("writing_id",writing);
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
			//设置参数
	        Context context = new Context();
			//emailTemplate为模板文件的文件名，即html demo的文件名
	        context.setVariables(map);
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
		list.stream().forEach(System.out::print);
		return new JSONDataResult().add("permissions", list);
	}
}
