package com.example.demo.controller;


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

@RestController
@RequestMapping(value="/demo/api")
public class ApiController {
	
	//private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
	
	@Autowired
	ApiService apiService;
	
	@RequestMapping(value="/image",method=RequestMethod.POST)
	public JSONResult imageUpload(@RequestParam("image") MultipartFile file) throws SystemException {
		
		String imageURL = apiService.singleImageUploadWithoutWatermark(file);
		return new JSONDataResult().add("imageURL", imageURL);
	}
}
