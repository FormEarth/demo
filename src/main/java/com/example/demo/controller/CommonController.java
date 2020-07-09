package com.example.demo.controller;

import com.example.demo.common.SystemProperties;
import com.example.demo.entity.JSONDataResult;
import com.example.demo.entity.JSONResult;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 该controller写那些返回的非json数据接口
 *
 * @author raining_heavily
 * @description
 * @date 2019年5月28日
 */
@Controller
public class CommonController {

    @Value(value = "${demooo.path.video}")
    private String videoPath;
    private static List<String> TYPES = Arrays.asList("mp4", "avi", "rmvb","wmv");

    /**
     * 访问默认接口时转发到分页接口
     *
     * @return
     */
    @RequestMapping(value = {"/articles"}, method = RequestMethod.GET)
    public String getArticles() {
        //默认接口转发到第一页
        return "forward:/demo/api/articles/1";
    }

    @RequestMapping(value = {"/videos"}, method = RequestMethod.GET)
    @ResponseBody
    public JSONResult getVideos() throws SocketException {
        List<File> files = new ArrayList<>();
        List<Map<String, String>> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int vpLength = videoPath.length();
        for (File file : getFileList(videoPath, files)) {
            Map<String, String> map = new HashMap<>();
            map.put("name", file.getName());
            map.put("time", sdf.format(new Date(file.lastModified())));
            map.put("size", byteFormat(file.length()));
            map.put("url", "http://" + Util.getLocalRealIP() + ":9092/demo/api/media?path=" + file.getAbsolutePath().substring(vpLength).replaceAll("\\\\", "/"));
            result.add(map);
        }
        return new JSONDataResult().add("videos", result);
    }

    public List<File> getFileList(String strPath, List<File> fileList) {
        File dir = new File(strPath);
        // 该文件目录下文件全部放入数组
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                // 判断是文件还是文件夹
                if (file.isDirectory()) {
                    // 获取文件绝对路径
                    getFileList(file.getAbsolutePath(), fileList);
                } else {
                    // 后缀
                    String str = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                    if (TYPES.contains(str)) {
                        fileList.add(file);
                    }
                }
            }

        }
        return fileList;
    }

    /**
     * 将文件大小由Byte转为MB或者KB
     *
     * @return
     */
    private static String byteFormat(Long size) {

        BigDecimal fileSize = new BigDecimal(size);
        BigDecimal param = new BigDecimal(1024);
        int count = 0;
        while (fileSize.compareTo(param) > 0 && count < 5) {
            fileSize = fileSize.divide(param);
            count++;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        String result = df.format(fileSize.setScale(2, BigDecimal.ROUND_DOWN)) + "";
        switch (count) {
            case 0:
                result += "B";
                break;
            case 1:
                result += "KB";
                break;
            case 2:
                result += "MB";
                break;
            case 3:
                result += "GB";
                break;
            case 4:
                result += "TB";
                break;
            case 5:
                result += "PB";
                break;
            default:
                result += "byte";
        }
        return result;
    }
}
