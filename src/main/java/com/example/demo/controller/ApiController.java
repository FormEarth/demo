package com.example.demo.controller;


import com.example.demo.aop.annotation.StaticURL;
import com.example.demo.common.Dict;
import com.example.demo.common.FileSourceEnum;
import com.example.demo.dto.Test;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.ExceptionUtil;
import com.example.demo.service.ImageService;
import com.example.demo.util.Util;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.JSONDataResult;
import com.example.demo.entity.JSONResult;
import com.example.demo.exception.SystemException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author raining_heavily
 */
@RestController
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    /**
     * 服务默认地址
     *
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public JSONResult index() {
        return JSONResult.success();
    }

    @Autowired
    ImageService imageService;

    @StaticURL
    @RequestMapping(value = "/image", method = RequestMethod.POST)
    public JSONResult imageUpload(@RequestParam("image") MultipartFile file) throws SystemException {

        String relativePath = imageService.singleImageUpload(file, Dict.GLOBAL_WATERMARK, FileSourceEnum.ARTICLE);
        return new JSONDataResult().add("relativePath", relativePath);
    }

    @RequestMapping(value = "/test/array", method = RequestMethod.POST)
    public JSONResult array(@RequestBody Test test, HttpServletRequest request) throws SystemException {
        logger.info(request.getParameter("code"));
        logger.info("------------------------list:",test);
        return new JSONDataResult();
    }


    /**
     * 获取指定url的title
     *
     * @return
     */
    @RequestMapping(value = "/title")
    public JSONResult getTitle(@RequestParam(required = false) String url) throws SystemException, URISyntaxException {
//        String url = "https://mp.weixin.qq.com/s?__biz=MzU5ODMzMjE4Mg==&mid=2247485312&idx=1&sn=5bda9c2bbc9792b3e595fe6b905ba971";
        System.out.print("++++++url:"+url);
        URI uri;
        try {
            uri=new URI(url);
        }catch (Exception ex){
            logger.error(url+"/n"+ExceptionUtil.getStackTraceString(ex));
            throw new SystemException(ExceptionEnums.FILED_VALIDATOR_ERROR);
        }

        Document document = Jsoup.parse(getResult(url));
        //标题 property="og:title"
        String title;
        if(uri.getHost().startsWith("mp.weixin.qq.com")){
            title = document.select("meta[property=og:title]").first().attr("content");
        }else{
            title = document.getElementsByTag("title").first().text();
        }

        //站点图标
        //TODO 如何处理base64编码的icon，如简书
        Elements iconElement = document.select("link[rel=shortcut icon]");
        String icon = iconElement.isEmpty()?"":iconElement.first().attr("href");
        Elements descriptionElement = document.select("meta[name=description]");
        String description = descriptionElement.isEmpty()?"":descriptionElement.first().attr("content");
//

        icon = uri.getHost() + "/" + icon;
        return new JSONDataResult().add("title", title).add("description",description).add("icon", icon);
    }

    private String getResult(String uri) throws SystemException {

        String regex = "^(https?|http)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        if (!uri.matches(regex)) {
            throw new SystemException(ExceptionEnums.FILED_VALIDATOR_ERROR);
        }

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(uri);
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36 Edg/86.0.622.69");
        get.setHeader("Accept", "text/html, application/xhtml+xml, image/jxr, */*");
        get.setHeader("Accept-Encoding", "gzip, deflate");
        get.setHeader("Accept-Language", "zh-Hans-CN, zh-Hans; q=0.8, en-US; q=0.5, en; q=0.3");
        get.setHeader("Host", "mp.weixin.qq.com");
//        get.setHeader("If-Modified-Since", "Sat, 04 Jan 2020 12:23:43 GMT");
        String result = null;
        HttpResponse response;
        try {
            response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (IOException e) {
            logger.error(ExceptionUtil.getStackTraceString(e));
            throw new SystemException(ExceptionEnums.DATA_HANDLE_ERROR);
        }
        System.out.print(result);
        return result;
    }
}
