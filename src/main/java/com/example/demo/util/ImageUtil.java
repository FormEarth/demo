package com.example.demo.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.*;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author raining_heavily
 * @description 图片处理公共类，参考[https://www.cnblogs.com/wzluo09/p/9669989.html]
 * @date 2019年4月2日
 */
public class ImageUtil {

    /**
     * 生成背景透明的 文字水印，文字位于透明区域正中央，可设置旋转角度
     *
     * @param width  生成图片宽度
     * @param heigth 生成图片高度
     * @param text   水印文字
     * @param color  颜色对象
     * @param font   awt字体
     * @param degree 水印文字旋转角度
     * @param alpha  水印不透明度0f-1.0f
     */
    public static BufferedImage waterMarkByText(int width, int heigth, String text, Color color, Font font,
                                                Double degree, float alpha) {
        BufferedImage buffImg = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB);
        /** 2、得到画笔对象 */
        Graphics2D g2d = buffImg.createGraphics();
        // ---------- 增加下面的代码使得背景透明 -----------------
        buffImg = g2d.getDeviceConfiguration().createCompatibleImage(width, heigth, Transparency.TRANSLUCENT);
        g2d.dispose();
        g2d = buffImg.createGraphics();
        // ---------- 背景透明代码结束 -----------------

        // 设置对线段的锯齿状边缘处理
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // 把源图片写入
        // g2d.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null),
        // srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0,null);

        // 设置水印旋转
        // if (null != degree) {
        // //注意rotate函数参数theta，为弧度制，故需用Math.toRadians转换一下
        // //以矩形区域中央为圆心旋转
        // g2d.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2,
        // (double) buffImg.getHeight() / 2);
        // }

        // 设置颜色
        g2d.setColor(color);

        // 设置 Font
        g2d.setFont(font);

        // 设置透明度:1.0f为透明度 ，值从0-1.0，依次变得不透明
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        // 获取真实宽度
        float realWidth = getRealFontWidth(text);
        float fontSize = font.getSize();
//		System.out.println("----------------"+realWidth+","+fontSize);
//		System.out.println("----------------y:"+(width-realWidth));
        // 计算绘图偏移x、y，使得使得水印文字在图片中居中
        // 这里需要理解x、y坐标是基于Graphics2D.rotate过后的坐标系
//		float x = 0.5f * width - 0.5f * fontSize * realWidth;
        float y = 0.5f * heigth + 0.5f * fontSize;
        //设置抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // 取绘制的字串宽度、高度中间点进行偏移，使得文字在图片坐标中居中
        g2d.drawString(text, 100, 45);
        // 释放资源
        g2d.dispose();
//		File file = new File("E:/User/pictures/static/upload/images/test/thumbnail/"+new Date().getTime()+".png");
//		try {
//			ImageIO.write(buffImg,"png",file);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
        return buffImg;

    }

    public static BufferedImage waterMarkByText(int width, int heigth, String text, Color color, float alpha) {
        // jdk默认字体
        Font font = new Font("Times New Roman", Font.PLAIN, 30);
        return waterMarkByText(width, heigth, text, color, font, -30d, alpha);
    }

    public static BufferedImage waterMarkByText(int width, int height, String text, float alpha) {
        return waterMarkByText(width, height, text, Color.white, alpha);
    }

    public static BufferedImage waterMarkByText(int width, int height, String text) {
        return waterMarkByText(width, height, text, 1.0f);
    }

    public static BufferedImage waterMarkByText(String text) {
        return waterMarkByText(200, 50, text);
    }


    /**
     * 获取真实字符串宽度，ascii字符占用0.5，中文字符占用1.0
     */
    private static float getRealFontWidth(String text) {
        int len = text.length();
        float width = 0f;
        for (int i = 0; i < len; i++) {
            if (text.charAt(i) < 256) {
                width += 0.5f;
            } else {
                width += 1.0f;
            }
        }
        return width;
    }

    /**
     * 解析图片的exif信息，获取图片的旋转角度
     * <p>
     * 1:  "Top, left side (Horizontal / normal)";<br>
     * 2:  "Top, right side (Mirror horizontal)";<br>
     * 3:  "Bottom, right side (Rotate 180)";<br>
     * 4:  "Bottom, left side (Mirror vertical)";<br>
     * 5:  "Left side, top (Mirror horizontal and rotate 270 CW)";<br>
     * 6:  "Right side, top (Rotate 90 CW)";<br>
     * 7:  "Right side, bottom (Mirror horizontal and rotate 90 CW)";<br>
     * 8:  "Left side, bottom (Rotate 270 CW)";<br>
     *
     * @param file
     * @return
     * @throws SystemException
     */
    public static double getOrientation(MultipartFile file) throws SystemException {
        Metadata metadata;
        double angles = 0.0;
        try {
            metadata = ImageMetadataReader.readMetadata(new BufferedInputStream(file.getInputStream()));
        } catch (ImageProcessingException | IOException e) {
            //若是读取exif信息失败则不再处理
            //throw new SystemException(ExceptionEnums.IMAGE_HANDLE_ERROR);
            return angles;
        }
        Directory directory = metadata.getDirectory(ExifIFD0Directory.class);
        if(directory==null) return angles;
        String orientation = directory.getString(ExifIFD0Directory.TAG_ORIENTATION);
        switch (orientation) {
            case "3":
                angles = 180;
                break;
            case "6":
                angles = 90;
                break;
            case "8":
                angles = -90;
                break;
            default:
                //什么都不做
        }
        return angles;
    }

    public static void main(String[] args) throws ImageProcessingException, IOException {
        String str = "C:/Users/chunyangwang/Desktop/test/";
        File[] file = new File(str).listFiles();
        int i = 0;
        for(File f : file){
            System.out.println(f.getAbsolutePath());
            Metadata metadata = ImageMetadataReader.readMetadata(f);
            String orientation = metadata.getDirectory(ExifIFD0Directory.class).getString(ExifIFD0Directory.TAG_ORIENTATION);
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    if (tag.getTagName() == "Orientation") {
                        System.out.print(tag.getDescription());
                    }
                }
            }
            i++;
            Thumbnails.of(f).scale(1.0).useExifOrientation(false).toFile(str+i+"_"+f.getName());
//        if (orientation.equals("3")) {
//            Thumbnails.of(file).scale(1.0).rotate(0).toFile("C:/Users/chunyangwang/Desktop/test/abc_1.jpg");
//        }
            System.out.println(orientation);
            System.out.println("-------------------------------------------");
        }

    }
}
