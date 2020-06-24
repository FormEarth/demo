package com.example.demo.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import com.example.demo.common.FileSourceEnum;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.service.ImageService;
import com.example.demo.util.ImageUtil;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;


/**
 * @author raining_heavily
 */
@Service
public class ImageServiceImpl implements ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);
    /**
     * 图片类型gif
     **/
    private static final String GIF = "GIF";

    /**
     * 图片文件原图路径
     **/
    @Value("${image.upload.original.path}")
    private String origPath;
    /**
     * 图片文件压缩路径
     **/
    @Value("${image.upload.compression.path}")
    private String compPath;

    @Override
    public String singleImageUpload(MultipartFile image, FileSourceEnum source) throws SystemException {
        return imageUpload(image, source, true, "");
    }

    @Override
    public String singleImageUpload(MultipartFile file, String waterMark, FileSourceEnum source) throws SystemException {
        return imageUpload(file, source, true, waterMark);
    }

    @Override
    public String singleImageUploadWithoutWatermark(MultipartFile file, FileSourceEnum source) throws SystemException {
        return imageUpload(file, source, false, null);
    }

    @Override
    public String saveOriginImage(MultipartFile file, FileSourceEnum source) throws SystemException {
        String fileName = getuuid() + ".jpg";
        saveFileToPath(file, compPath + source.getDynamic() + fileName);
        return source.getDynamic() + fileName;
    }

    private String imageUpload(MultipartFile image, FileSourceEnum source, boolean addWatermark, String watermark) throws SystemException {
        if (image == null) {
            throw new SystemException(ExceptionEnums.IMAGE_FILE_NULL);
        }
        String uuid = getuuid();
        //获取原文件名
        String fileName = image.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
        //直接改为jpg
        fileName = uuid + ".jpg";
        //相对路径
        String relativePath = source.getDynamic() + fileName;
        // 原图保存路径
        String filePath = origPath + relativePath;
        logger.info("图片上传路径：" + filePath);
        // 压缩图片路径
        String thumbnailFilePath = compPath + relativePath;
        logger.info("压缩图片路径：" + thumbnailFilePath);

        //修正图片带有旋转的情况
        double angles =  ImageUtil.getOrientation(image);
        logger.info("图片旋转角度{}",angles);

        if (GIF.equals(fileType)) {
            fileName = uuid + ".gif";
            relativePath = source.getDynamic() + fileName;
            saveFileToPath(image, compPath + relativePath);
            return relativePath;
        } else {
            saveFileToPath(image, filePath);
        }

        File outFile = new File(thumbnailFilePath);
        if (!outFile.exists()) {
            outFile.getParentFile().mkdirs();
        }

        // 压缩和添加水印
        try {
            Thumbnails.Builder<? extends InputStream> builder = Thumbnails.of(image.getInputStream());
            if (addWatermark) {
                builder = builder.watermark(Positions.BOTTOM_RIGHT, ImageUtil.waterMarkByText(watermark), 0.8f);
            }
            //Thumbnails自动处理Exif里的Orientation不准确，这里读取Orientation来旋转
            builder.useExifOrientation(false).outputQuality(0.25f).scale(1f).rotate(angles).toFile(outFile);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ExceptionEnums.IMAGE_FILE_COMPRESSION_FAIL.getMessage());
            throw new SystemException(ExceptionEnums.IMAGE_FILE_UPLOAD_FAIL);
        }

        return relativePath;
    }

    /**
     * 保存源文件到指定路径
     *
     * @param file 文件
     * @param path 路径
     * @throws SystemException
     */
    private void saveFileToPath(MultipartFile file, String path) throws SystemException {
        File imageFile = new File(path);
        if (imageFile.getParentFile() != null || !imageFile.getParentFile().isDirectory()) {
            // 创建文件
            imageFile.getParentFile().mkdirs();
        }
        InputStream inputStream;
        FileOutputStream fileOutputStream;

        try {
            inputStream = file.getInputStream();
            fileOutputStream = new FileOutputStream(imageFile);
            IOUtils.copyLarge(inputStream, fileOutputStream);
            inputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            logger.error(ExceptionEnums.FILE_WRITE_FAIL.getMessage());
            throw new SystemException(ExceptionEnums.IMAGE_FILE_UPLOAD_FAIL);
        }
        //TODO Thumbnails保存原图并旋转时会导致图片部分信息丢失，即使设定原图原比例保存，图片大小仍会被放大数倍，因此直接保存了原图
//        try {
//            Thumbnails.of(file.getInputStream()).rotate(angles).outputQuality(1f).scale(1f).toFile(path);
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new SystemException(ExceptionEnums.IMAGE_FILE_UPLOAD_FAIL);
//        }
    }

    private String getuuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}


