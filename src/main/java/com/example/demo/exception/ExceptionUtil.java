package com.example.demo.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author raining_heavily
 * @date 2020/4/11 21:54
 **/
public class ExceptionUtil {

    /**
     * 打印异常堆栈信息
     *
     * @param ex
     * @return
     */
    public static String getStackTraceString(Throwable ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return "\n"+sw.toString();
    }
}
