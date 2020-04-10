package com.example.demo.controller;


import com.example.demo.aop.annotation.StaticURL;
import com.example.demo.common.Dict;
import com.example.demo.common.FileSourceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.JSONDataResult;
import com.example.demo.entity.JSONResult;
import com.example.demo.exception.SystemException;
import com.example.demo.service.ApiService;

/**
 * @author raining_heavily
 */
@RestController
public class ApiController {
	
	//private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
	/**
	 * 服务默认地址
	 * @return
	 */
	@RequestMapping(value= "/",method = RequestMethod.GET)
	public JSONResult index() {
		return JSONResult.success();
	}
	
	@Autowired
	ApiService apiService;

	@StaticURL
	@RequestMapping(value="/image",method=RequestMethod.POST)
	public JSONResult imageUpload(@RequestParam("image") MultipartFile file) throws SystemException {
		
		String relativePath = apiService.singleImageUpload(file, Dict.GLOBAL_WATERMARK, FileSourceEnum.ARTICLE);
		return new JSONDataResult().add("relativePath", relativePath);
	}
}
