package club.chillrain.tomcat.factory;

import club.chillrain.servlet.listener.*;
import club.chillrain.servlet.servlet.HttpSession;
import club.chillrain.servlet.servlet.ServletRequest;
import club.chillrain.servlet.servlet.ServletContext;
import club.chillrain.tomcat.components.DefaultListener;
import club.chillrain.tomcat.exception.NotListenerException;
import club.chillrain.tomcat.interfaces.abstracts.ListenerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author ChillRain 2023 08 20
 */
public class ListenerFactoryImpl extends ListenerFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger("ListenerFactory");
    private static Class<HttpSessionListener> httpSessionListenerClass;
    private static Class<HttpSessionAttributeListener> httpSessionAttributeListenerClass;
    private static Class<ServletRequestListener> servletRequestListenerClass;
    private static Class<ServletRequestAttributeListener> servletRequestAttributeListenerClass;
    private static Class<ServletContextListener> servletContextListenerClass;
    private static Class<ServletContextAttributeListener> servletContextAttributeListenerClass;

//    @Override
    public static Listener getListener(Object obj) throws InstantiationException, IllegalAccessException {
        if(obj instanceof ServletRequest && servletRequestListenerClass != null){
            return servletRequestListenerClass.newInstance();
        }
        if(obj instanceof HttpSession && httpSessionListenerClass != null){
            return httpSessionListenerClass.newInstance();
        }
        if(obj instanceof ServletContext && servletContextListenerClass != null){
            return servletContextListenerClass.newInstance();
        }
        return new DefaultListener();
    }

    public static Listener getAttributeListener(Object obj) throws InstantiationException, IllegalAccessException {
        if(obj instanceof ServletRequest && servletRequestAttributeListenerClass != null){
            return servletRequestAttributeListenerClass.newInstance();
        }
        if(obj instanceof HttpSession && httpSessionAttributeListenerClass != null){
            return httpSessionAttributeListenerClass.newInstance();
        }
        if(obj instanceof ServletContext && servletContextAttributeListenerClass != null){
            return servletContextAttributeListenerClass.newInstance();
        }
        return null;
    }

    /**
     * 判断是否是监听器以及其类型
     * @param clazz
     */
    public static void init(Class clazz) throws NotListenerException {
        if(!Listener.class.isAssignableFrom(clazz)){//他是监听器接口的子类
            throw new NotListenerException("类：" + clazz.getName() + " 没有实现接口Listener");
        }
        if(ServletContextListener.class.isAssignableFrom(clazz)){
            servletContextListenerClass = clazz;
            LOGGER.info("类：" + clazz.getName() + " 已作为监听器进行载入");
        }
        if(ServletContextAttributeListener.class.isAssignableFrom(clazz)){
            servletContextAttributeListenerClass = clazz;
            LOGGER.info("类：" + clazz.getName() + " 已作为监听器进行载入");
        }
        if(ServletRequestListener.class.isAssignableFrom(clazz)){
            servletRequestListenerClass = clazz;
            LOGGER.info("类：" + clazz.getName() + " 已作为监听器进行载入");
        }
        if(ServletRequestAttributeListener.class.isAssignableFrom(clazz)){
            servletRequestAttributeListenerClass = clazz;
            LOGGER.info("类：" + clazz.getName() + " 已作为监听器进行载入");
        }
        if(HttpSessionListener.class.isAssignableFrom(clazz)){
            httpSessionListenerClass = clazz;
            LOGGER.info("类：" + clazz.getName() + " 已作为监听器进行载入");
        }
        if(HttpSessionAttributeListener.class.isAssignableFrom(clazz)){
            httpSessionAttributeListenerClass = clazz;
            LOGGER.info("类：" + clazz.getName() + " 已作为监听器进行载入");
        }
    }
}
