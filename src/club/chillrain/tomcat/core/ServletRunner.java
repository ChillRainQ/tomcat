package club.chillrain.tomcat.core;

import club.chillrain.servlet.listener.Listener;
import club.chillrain.servlet.listener.ServletRequestEvent;
import club.chillrain.servlet.listener.ServletRequestListener;
import club.chillrain.servlet.servlet.Cookie;
import club.chillrain.servlet.servlet.HttpServlet;
import club.chillrain.servlet.servlet.MyServletResponse;
import club.chillrain.servlet.servlet.RequestDispatcher;
import club.chillrain.tomcat.context.request.MyHttpServletRequestImpl;
import club.chillrain.tomcat.enums.Status;
import club.chillrain.tomcat.context.request.MyHttpServletResponseImpl;
import club.chillrain.tomcat.impl.RequestDispatcherImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet的运行环境
 */
public class ServletRunner{

    private static final Logger LOGGER = LoggerFactory.getLogger("servletContext");
    /**
     * Servlet的容器
     */
    public static Map<String, HttpServlet> servletContext = new HashMap<>();
    /**
     * uri的容器
     */
    public static Map<String, String> uriMap = null;


    /**
     * ServletContext初始化
     */
    public void init(){

    }
    static {
        try {
            uriMap = Prepare.initURIMapping(Prepare.getAllClasses(new File(Constant.src)));
            servletContext = Prepare.servletMapInit(uriMap);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 处理器
     */
    public static class Processor{
        private Socket socket;

        public Processor(Socket socket) {
            this.socket = socket;
        }
        /**
         * Servlet处理
         */
        public void process() throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, NoSuchAlgorithmException {
            LOGGER.info("--->" + Thread.currentThread() + "处理请求");
            MyHttpServletRequestImpl request = new MyHttpServletRequestImpl(socket);
            //监听器监听创建
            ServletRequestListener listener = request.getListener();
            ServletRequestEvent requestEvent = new ServletRequestEvent(request);
            listener.initRequest(requestEvent);//运行用户自定义的监听器动作


            MyServletResponse response = new MyHttpServletResponseImpl(socket);
            String uri = request.getRemoteURI();
            LOGGER.info("--->请求URI为：" + uri);
            if("/".equals(uri)){//首页
                response.getWriter().write("<h1>Here is home</h1>");
            }else{
                String className = ServletRunner.uriMap.get(uri);
                if (className == null || uri == null){//不存在的资源
                    response.setStatus(Status.HTTP_404.getCode());
                    response.getWriter().write(Constant.errorPage
                            .replace("${code}", Status.HTTP_404.getCode().toString())
                            .replace("${message}", Status.HTTP_404.getMessage())
                            .replace("${uri}", uri));
                }else {//映射表正常
                    RequestDispatcher dispatcher = new RequestDispatcherImpl(uri);
                    dispatcher.forward(request, response);
                }
            }
            if(request.initSessionMark){
                Integer id = request.currentSession.getId();
                response.addCookie(new Cookie("JSESSIONID", id.toString()));
                LOGGER.info("SESSION异常,重新写入SESSION：" + id);
            }
            //监听器监听销毁
            listener.destroyed(requestEvent);

            ((MyHttpServletResponseImpl)response).finishedResponse();
        }
    }

}
