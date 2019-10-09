package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exception.SystemException;

public interface ApiService {
	
	/**
	 **    单图片文件上传
	 * @param file 文件
	 * @return 文件的访问地址
	 * @throws SystemException
	 */
	public String singleImageUpload(MultipartFile file) throws SystemException;
	/**
	 **    单图片文件上传(自定义水印)
	 * @param file 文件
	 * @return 文件的访问地址
	 * @throws SystemException
	 */
	public String singleImageUpload(MultipartFile file,String watermark) throws SystemException;
	/**
	 **    单图片文件上传(无水印)
	 * @param file 文件
	 * @return 文件的访问地址
	 * @throws SystemException
	 */
	public String singleImageUploadWithoutWatermark(MultipartFile file) throws SystemException;
	
}
