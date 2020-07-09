package com.example.demo.controller.media;

import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author raining_heavily
 * @date 2020/5/24 21:16
 **/
@Controller
public class MediaController {

    @Value(value = "${demooo.path.video}")
    private String videoPath;

    /**
     * video请求，返回视频流，只考虑指定起始位置情况，Range: bytes=1054244864-<br>
     * 参考 https://blog.csdn.net/tongkaiming/article/details/82252061
     * @param request
     * @param response
     * @param path
     * @throws SystemException
     * @throws IOException
     */
    @RequestMapping(value = "/media")
    public void mediaStream(HttpServletRequest request, HttpServletResponse response, @RequestParam String path) throws SystemException, IOException {
        File file = new File(videoPath + path);
        BufferedInputStream bis;
        if (!file.exists()) {
            throw new SystemException(ExceptionEnums.SOURCE_NOT_FOUND);
        }
        long fileLength = file.length();
        // get file content
        bis = new BufferedInputStream(new FileInputStream(file));

        //Range格式bytes=0-或bytes=0-1000，也可以同时指定多个位置，在此只考虑一个的情况，需从range中获取起始位置
        String range = request.getHeader("Range");
        //不允许直接以链接的方式打开视频
        if(range == null){
            throw new SystemException(ExceptionEnums.SERVER_REFUSE);
        }
        range = range.replace("bytes=", "").trim();
        range = range.substring(0,range.length()-1);
        long start = Long.valueOf(range);
        System.out.println("开始:"+start);
        String contentRange = "bytes " + start + "-" + (fileLength - 1) + "/" + fileLength;

        String fileName = file.getName();
        // tell the client to allow accept-ranges
        response.reset();
        response.setHeader("Accept-Ranges", "bytes");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        // 如果设设置了Content-Length，则客户端会自动进行多线程下载。如果不希望支持多线程，则不要设置这个参数。
        // Content-Length: [文件的总大小] - [客户端请求的下载的文件块的开始字节]
        response.setHeader("Content-Length", String.valueOf(fileLength-start));
        response.setHeader("Content-Range", contentRange);
        //告诉客户端已部分响应
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        bis.skip(start);
        OutputStream out = response.getOutputStream();
        int n;
        int size = 1024;
        byte[] bytes = new byte[size];
        while ((n = bis.read(bytes)) != -1) {
            out.write(bytes, 0, n);
        }
        out.flush();
        out.close();
        bis.close();

    }
}
