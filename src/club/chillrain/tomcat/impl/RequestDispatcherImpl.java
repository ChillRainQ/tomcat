package club.chillrain.tomcat.impl;

import club.chillrain.servlet.servlet.HttpServlet;
import club.chillrain.servlet.servlet.MyServletRequest;
import club.chillrain.servlet.servlet.MyServletResponse;
import club.chillrain.servlet.servlet.RequestDispatcher;
import club.chillrain.tomcat.core.ServletRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 请求分发器的实现
 * @author ChillRain 2023 08 12
 */
public class RequestDispatcherImpl implements RequestDispatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger("RequestDispatcher");
    private String uri;

    public RequestDispatcherImpl(String uri) {
        LOGGER.info("分发请求：" + uri);
        this.uri = uri;
    }

    @Override
    public void include(MyServletRequest request, MyServletResponse response) {
        try{
            HttpServlet servlet = this.getHttpServlet(request, response);
            servlet.init();
            servlet.service(request, response);
            servlet.destory();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取需要分发的servlet
     * @param request
     * @param response
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private HttpServlet getHttpServlet(MyServletRequest request, MyServletResponse response) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        HttpServlet servlet = ServletRunner.servletContext.get(this.uri);//容器中没有
        if(servlet == null){//尝试反射获取
            String className = ServletRunner.uriMap.get(this.uri);
            Class<?> clazz = Class.forName(className);
            servlet = (HttpServlet)clazz.newInstance();
            ServletRunner.servletContext.put(uri, servlet);//写入容器
        }
        return servlet;
    }
    @Override
    public void forward(MyServletRequest request, MyServletResponse response) {
        try{
            HttpServlet servlet = this.getHttpServlet(request, response);
            PrintWriter writer = response.getWriter();
            writer.flush();
            response.reset();
            servlet.init();
            servlet.service(request, response);
            servlet.destory();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
