package club.chillrain.tomcat.constants;

import club.chillrain.servlet.HttpServlet;
import club.chillrain.tomcat.core.PreparedHandler;
import club.chillrain.tomcat.enums.Status;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ChillRain 2023 07 30
 */
public class Constant {
    public static Map<String, HttpServlet> servletMap = new HashMap<>();
    public static final String src = "D:/IDEA项目/tomcat/src";
    public static final Map<Integer, String> statusMap = Status.init();
    private static final List<String> allClasses = PreparedHandler.getAllClasses(new File(src));
    public static final Map<String, String> uriMap;

    static {
        try {
            uriMap = PreparedHandler.initURIMapping(allClasses);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
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
