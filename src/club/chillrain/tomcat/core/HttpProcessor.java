package club.chillrain.tomcat.core;

import club.chillrain.servlet.HttpServlet;
import club.chillrain.servlet.MyServletRequest;
import club.chillrain.servlet.MyServletResponse;
import club.chillrain.tomcat.constants.Constant;
import club.chillrain.tomcat.enums.Status;
import club.chillrain.tomcat.impl.MyHttpServletRequestImpl;
import club.chillrain.tomcat.impl.MyHttpServletResponseImpl;

import java.io.IOException;
import java.net.Socket;

/**
 * HTTP处理器
 * @author ChillRain 2023 07 31
 */
public class HttpProcessor {
    /**
     * 构建servlet的socket
     */
    private Socket socket;

    public HttpProcessor(Socket socket) {
        this.socket = socket;
    }

    /**
     * 处理HTTP请求
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public void process() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        MyServletRequest request = new MyHttpServletRequestImpl(socket, socket.getInputStream());
        MyServletResponse response = new MyHttpServletResponseImpl(socket.getOutputStream());
        String uri = request.getRemoteURI();
        System.out.println("--->请求URI为：" + uri);
        if("/".equals(uri)){//首页
            response.getWriter().write("<h1>Here is home</h1>");
        }else{
            String className = Constant.uriMap.get(uri);

            if (className == null){//不存在的资源
                response.setStatus(Status.HTTP_404.getCode());
                response.getWriter().write(Constant.errorPage
                        .replace("${code}", Status.HTTP_404.getCode().toString())
                        .replace("${message}", Status.HTTP_404.getMessage())
                        .replace("${uri}", uri));
            }else {//映射表正常
                Class<?> clazz = Class.forName(className);
                HttpServlet servlet = (HttpServlet)clazz.newInstance();
                servlet.init();
                servlet.service(request, response);
                servlet.destory();
            }
        }
        ((MyHttpServletResponseImpl)response).finishedResponse();
    }
}
