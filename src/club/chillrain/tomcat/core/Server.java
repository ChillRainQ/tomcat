package club.chillrain.tomcat.core;

import club.chillrain.servlet.HttpServlet;
import club.chillrain.servlet.MyServletRequest;
import club.chillrain.servlet.MyServletResponse;
import club.chillrain.tomcat.constants.Constant;
import club.chillrain.tomcat.enums.Status;
import club.chillrain.tomcat.impl.MyHttpServletRequestImpl;
import club.chillrain.tomcat.impl.MyHttpServletResponseImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 启动类
 * @author ChillRain 2023 07 22
 */
public class Server {
//    public static Map<String, HttpServlet> servletMap = new HashMap<>();
    public static void main(String[] args) {
        try {
            Map<String, HttpServlet> servletMap = Constant.servletMap;
            Map<String, String> uriMap = Constant.uriMap;
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("--->服务开始于端口：8080");
            while (true){
                System.out.println("--->准备接收HTTP请求");
                Socket socket = serverSocket.accept();
                new Thread(() ->{
                    try {
                        System.out.println("--->接收到连接请求");
                        InputStream inputStream = socket.getInputStream();
                        OutputStream outputStream = socket.getOutputStream();
                        MyServletRequest request = new MyHttpServletRequestImpl(socket, inputStream);
                        MyServletResponse response = new MyHttpServletResponseImpl(outputStream);
                        String uri = request.getRemoteURI();
                        System.out.println("--->请求资源：" + uri);
                        String className = uriMap.get(uri);//获取uri对应全限定类名用于绑定
                        boolean flag = false;
                        if("/".equals(uri)){
                            response.getWriter().write("<h1>Welcom to Home</h1>");
                            flag = true;
                        }
                        if(className != null){//请求的uri不存在 不创建servlet
                            Class<?> clazz = Class.forName(className);
                            Class<?> superclass = clazz.getSuperclass();
                            if(superclass == HttpServlet.class) {
                                flag = true;
                                HttpServlet servlet = null;
                                if (servletMap.get(className) == null) {
                                    servlet = (HttpServlet) clazz.newInstance();
                                    servletMap.put(className, servlet);
                                    System.out.println("--->servlet init");
                                }else{
                                    servlet = servletMap.get(className);
                                }
                                servlet.init();
                                servlet.service(request, response);
                                servlet.destory();
                            }
                        }
                        if(!flag){
                            response.setStatus(404);
                            response.getWriter().write(
                                    Constant.errorPage.replace("${code}", Status.HTTP_404.toString())
                                            .replace("${message}", Status.HTTP_404.getMessage())
                                            .replace("${uri}", uri));
                        }
                        ((MyHttpServletResponseImpl) response).finishedResponse();
                        outputStream.close();
                        inputStream.close();
                        socket.close();
                        socket.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
//                System.out.println("--->接收到连接请求");
//                InputStream inputStream = socket.getInputStream();
//                OutputStream outputStream = socket.getOutputStream();
//                MyServletRequest request = new MyHttpServletRequestImpl(socket, inputStream);
//                MyServletResponse response = new MyHttpServletResponseImpl(outputStream);
//                String uri = request.getRemoteURI();
//                System.out.println("--->请求资源：" + uri);
//                String className = uriMap.get(uri);//获取uri对应全限定类名用于绑定
//                boolean flag = false;
//                if("/".equals(uri)){
//                    response.getWriter().write("<h1>Welcom to Home</h1>");
//                    flag = true;
//                }
//                if(className != null){//请求的uri不存在 不创建servlet
//                    Class<?> clazz = Class.forName(className);
//                    Class<?> superclass = clazz.getSuperclass();
//                    if(superclass == HttpServlet.class) {
//                        flag = true;
//                        HttpServlet servlet = null;
//                        if (servletMap.get(className) == null) {
//                            servlet = (HttpServlet) clazz.newInstance();
//                            servletMap.put(className, servlet);
//                            System.out.println("--->servlet init");
//                        }else{
//                            servlet = servletMap.get(className);
//                        }
//                        servlet.init();
//                        servlet.service(request, response);
//                        servlet.destory();
//                    }
//                }
////                System.out.println(request.getRemoteAddr());
////                StringBuffer sb = new StringBuffer();
////                sb
////                        .append("HTTP/1.1 200 OK \n")//响应行
////                        .append("Content-Type: text/html;charset=utf8\n")//响应头
////                        .append("A: B\n")
////                        .append("Content-length: " + "Here is a HTTP Server".getBytes(StandardCharsets.UTF_8).length + "\n")
////                        .append("\n")
////                        .append("Here is a HTTP Server");//响应体
////                outputStream.write(sb.toString().getBytes(StandardCharsets.UTF_8));               if(
//                if(!flag){
//                    response.setStatus(404);
//                    response.getWriter().write(
//                            Constant.errorPage.replace("${code}", Status.HTTP_404.toString())
//                                    .replace("${message}", Status.HTTP_404.getMessage())
//                                    .replace("${uri}", uri));
//                }
//                ((MyHttpServletResponseImpl) response).finishedResponse();
//                outputStream.close();
//                inputStream.close();
//                socket.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
