package com.example.demo.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.service.ApiService;
import com.example.demo.util.ImageUtil;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

@Service
public class ApiServiceImpl implements ApiService {

	private static final Logger logger = LoggerFactory.getLogger(ApiServiceImpl.class);

	/** 图片文件保存的路径 **/
	@Value("${image.upload.path}")
	private String path;
	/** 图片访问的路径前缀 **/
	@Value("${image.access.url}")
	private String imageURL;

	@Override
	public String singleImageUpload(MultipartFile image) throws SystemException {
		return imageUpload(image,true,"");
	}

	@Override
	public String singleImageUpload(MultipartFile file, String waterMark) throws SystemException {
		return imageUpload(file,true,waterMark);
	}

	@Override
	public String singleImageUploadWithoutWatermark(MultipartFile file) throws SystemException {
		return imageUpload(file,false,null);
	}
	
	private String imageUpload(MultipartFile image,boolean addWatermark, String watermark) throws SystemException {
		boolean compression = true;
		if (image == null) {
			throw new SystemException(ExceptionEnums.IMAGE_FILE_NULL);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		// 年月日生成保存目录
//		String directory = sdf.format(new Date()) + "/";
		// uuid文件名
		String uuid = UUID.randomUUID().toString().replace("-", "");
		String fileName = image.getOriginalFilename();
		fileName = uuid + fileName.substring(fileName.lastIndexOf("."));
		String filePath = "original/" + fileName;
		logger.info("图片上传路径：" + path + filePath);
		File imageFile = new File(path + filePath);
		if (imageFile.getParentFile() != null || !imageFile.getParentFile().isDirectory()) {
			// 创建文件
			imageFile.getParentFile().mkdirs();
		}
		InputStream inputStream = null;
		FileOutputStream fileOutputStream = null;

		try {
			inputStream = image.getInputStream();
			fileOutputStream = new FileOutputStream(imageFile);
			IOUtils.copyLarge(inputStream, fileOutputStream);
		} catch (IOException e) {
			logger.error(ExceptionEnums.FILE_WRITE_FAIL.getMessage());
			throw new SystemException(ExceptionEnums.IMAGE_FILE_UPLOAD_FAIL);
		}

		//double scale = 0.8d;
		// 压缩图片地址
		String thumbnailFilePath = "thumbnail/" + fileName;
		File outFile = new File(path + thumbnailFilePath);
		logger.info("压缩图片路径"+path + thumbnailFilePath);
		if(!outFile.exists()) outFile.getParentFile().mkdirs();
		// 压缩
		//ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		if (compression) {
			try {
				if (addWatermark) {
					Thumbnails.of(path + filePath)
					.size(400, 500)
					.watermark(Positions.BOTTOM_LEFT, ImageUtil.waterMarkByText(watermark), 0.8f)
					.outputFormat("jpg")
					.toFile(outFile);
				}else {
					Thumbnails.of(path + filePath)
					.size(400, 500)
					.outputFormat("jpg")
					.toFile(outFile);
				}
			} catch (Exception ex) {
				logger.error(ExceptionEnums.IMAGE_FILE_COMPRESSION_FAIL.getMessage());
				ex.printStackTrace();
				throw new SystemException(ExceptionEnums.IMAGE_FILE_UPLOAD_FAIL);
			}
		}
		if(compression||addWatermark){
			return imageURL + thumbnailFilePath;
		}else {
			return imageURL + filePath;
		}	

	}
}


