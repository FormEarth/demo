package com.example.demo.service;

import com.example.demo.common.FileSourceEnum;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exception.SystemException;

/**
 * @author raining_heavily
 */
public interface ApiService {

    /**
     * 单图片文件上传
     *
     * @param file 文件
	 * @param source 文件来源
     * @return 文件的访问地址
     * @throws SystemException
     */
    public String singleImageUpload(MultipartFile file, FileSourceEnum source) throws SystemException;

    /**
     * 单图片文件上传(自定义水印)
     *
     * @param file 文件
	 * @param source 文件来源
     * @return 文件的访问地址
     * @throws SystemException
     */
    public String singleImageUpload(MultipartFile file, String watermark,FileSourceEnum source) throws SystemException;

    /**
     * 单图片文件上传(无水印)
     *
     * @param file 文件
	 * @param source 文件来源
     * @return 文件的访问地址
     * @throws SystemException
     */
    public String singleImageUploadWithoutWatermark(MultipartFile file,FileSourceEnum source) throws SystemException;

    /**
     * 直接保存原图，不再压缩
     * @param file
     * @param source
     * @return
     * @throws SystemException
     */
    public String saveOriginImage(MultipartFile file,FileSourceEnum source) throws SystemException;

}
