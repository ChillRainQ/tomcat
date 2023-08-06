package club.chillrain.tomcat.constants;

import club.chillrain.servlet.HttpServlet;
import club.chillrain.tomcat.core.HttpProcessor;
import club.chillrain.tomcat.core.PreparedHandler;
import club.chillrain.tomcat.enums.Status;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 常量
 * @author ChillRain 2023 07 30
 */
public class Constant {
    /**
     * 存储servlet的表
     */
    public static final Map<String, HttpServlet> servletMap;
    /**
     * 项目路径
     */
    public static final String src;

    /**
     * HTTP响应码表
     */
    public static final Map<Integer, String> statusMap;
    /**
     * 所有的类
     */
    private static final List<String> allClasses;
    /**
     * uri映射表
     */
    public static final Map<String, String> uriMap;
    /**
     * 工作线程池
     */
    public static final ThreadPoolExecutor servletPool;

    public static void init(){}

    static {
        try {
            src = getProjectSrc();
            statusMap = Status.init();
            servletMap = new HashMap<>();
            allClasses = PreparedHandler.getAllClasses(new File(src));
            uriMap = PreparedHandler.initURIMapping(allClasses);
            servletPool = servletPoolInit();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ThreadPoolExecutor servletPoolInit() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("resources/config.properties"));
        return new ThreadPoolExecutor(
                Integer.valueOf((String) properties.get("tomcat.core-poolsize")),
                Integer.valueOf((String) properties.get("tomcat.max-threads")),
                Integer.valueOf((String) properties.get("tomcat.keep-alivetime")),
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
    public static String getProjectSrc() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("resources/config.properties"));
        return properties.getProperty("tomcat.src");
    }

    /**
     * 错误页
     */
    public static String errorPage = "<!doctype html>\n" +
            "   <html lang=\"zh\">\n" +
            "      <head>\n" +
            "         <title>\n" +
            "            HTTP状态 ${code} - ${message}\n" +
            "         </title>\n" +
            "         <style type=\"text/css\">\n" +
            "            body {font-family:Tahoma,Arial,sans-serif;} h1, h2, h3, b {color:white;background-color:#525D76;} h1 {font-size:22px;} h2 {font-size:16px;} h3 {font-size:14px;} p {font-size:12px;} a {color:black;} .line {height:1px;background-color:#525D76;border:none;}\n" +
            "         </style>\n" +
            "</head>\n" +
            "      <body>\n" +
            "         <h1>\n" +
            "            HTTP状态 ${code} - ${message}\n" +
            "         </h1>\n" +
            "         <hr class=\"line\" />\n" +
            "         <p>\n" +
            "            <b>类型</b> 状态报告\n" +
            "         </p>\n" +
            "         <p>\n" +
            "            <b>消息</b> 请求的资源 ${uri} 不可用\n" +
            "         </p>\n" +
            "         <p>\n" +
            "            <b>描述</b> 源服务器未能找到目标资源的表示或者是不愿公开一个已经存在的资源表示。\n" +
            "         </p>\n" +
            "         <hr class=\"line\" />\n" +
            "         <h3>\n" +
            "            Qcna Tomcat 0.0.1\n" +
            "         </h3>\n" +
            "</body>\n" +
            "</html>";
}
