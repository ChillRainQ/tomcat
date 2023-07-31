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
 * @author ChillRain 2023 07 31
 */
public class HttpProcessor {
    private Socket socket;

    public HttpProcessor(Socket socket) {
        this.socket = socket;
    }
    public void process() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        MyServletRequest request = new MyHttpServletRequestImpl(socket, socket.getInputStream());
        MyServletResponse response = new MyHttpServletResponseImpl(socket.getOutputStream());
        String uri = request.getRemoteURI();
        String className = Constant.uriMap.get(uri);
        Class<?> clazz = Class.forName(className);
        Class<?> superclass = clazz.getSuperclass();
        boolean flag = false;
        if("/".equals(uri)){//首页
            response.getWriter().write("<h1>Here is home</h1>");
            flag = true;
        }else if (className == null || superclass == HttpServlet.class){//不存在 未实现HttpServlet
            response.setStatus(Status.HTTP_404.getCode());
            response.getWriter().write(Constant.errorPage
                    .replace("${code}", Status.HTTP_404.getCode().toString())
                    .replace("${message}", Status.HTTP_404.getMessage())
                    .replace("${uri}", uri));
            flag = true;
        }else {//映射表正常
            HttpServlet servlet = (HttpServlet)clazz.newInstance();
            servlet.init();
            servlet.service(request, response);
            servlet.destory();
        }
        ((MyHttpServletResponseImpl)response).finishedResponse();
    }
}
