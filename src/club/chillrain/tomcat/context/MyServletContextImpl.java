package club.chillrain.tomcat.context;

import club.chillrain.servlet.listener.ServletContextAttributeEvent;
import club.chillrain.servlet.listener.ServletContextAttributeListener;
import club.chillrain.servlet.listener.ServletContextEvent;
import club.chillrain.servlet.listener.ServletContextListener;
import club.chillrain.servlet.servlet.ServletContext;
import club.chillrain.tomcat.factory.ListenerFactoryImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ServletContext域对象
 * @author ChillRain 2023 08 20
 */
public class MyServletContextImpl implements ServletContext {
    /**
     * 域对象存储容器
     */
    private Map<String, Object> attributes;
    private ServletContextEvent event;
    private ServletContextListener servletContextListener;
    private ServletContextAttributeListener servletContextAttributeListener;

    @Override
    public void setAttribute(String key, Object val) {
        //修改功能监听
        if(this.attributes.containsKey(key)){
            this.servletContextAttributeListener
                    .updateAttribute(new ServletContextAttributeEvent(this, key, this.attributes.get(key)));
        }else{
            this.servletContextAttributeListener
                    .addAttribute(new ServletContextAttributeEvent(this, key, val));
        }
        this.attributes.put(key, val);
    }

    /**
     * 单例对象
     */
    private static ServletContext application = new MyServletContextImpl();
    private MyServletContextImpl() {
        try {
            this.attributes = new ConcurrentHashMap<>();
            this.servletContextListener = (ServletContextListener) ListenerFactoryImpl.getListener(this);
            this.servletContextAttributeListener = (ServletContextAttributeListener) ListenerFactoryImpl.getAttributeListener(this);
            this.event = new ServletContextEvent(this);
            if(application != null){
                throw new RuntimeException("非法的操作");
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static ServletContext getContext(){
        return application;
    }

    @Override
    public Object getAttribute(String key) {
        return this.attributes.get(key);
    }

    @Override
    public Boolean removeAttribute(String key) {
        this.servletContextAttributeListener
                .removeAttribute(new ServletContextAttributeEvent(this, key, this.attributes.get(key)));
        Object remove = this.attributes.remove(key);
        return remove != null ? true : false;
    }

    public ServletContextEvent getEvent(){
        return this.event;
    }
    public ServletContextListener getServletContextListener(){
        return this.servletContextListener;
    }
    public ServletContextAttributeListener getServletContextAttributeListener(){
        return this.servletContextAttributeListener;
    }
}
