package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.JSONResult;

/**
 * @author raining_heavily
 * @date 2019/12/23 21:27
 **/
@RestController
@RequestMapping("/manager")
public class ManagerController {


	/**
	 * 锁定
	 * @return
	 */
	public JSONResult locked() {
		return null;
		
	}
}
