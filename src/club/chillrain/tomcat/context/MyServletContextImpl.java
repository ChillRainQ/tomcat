package club.chillrain.tomcat.context;

import club.chillrain.servlet.servlet.ServletContext;

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
    private Map<String, Object> servletContextAttributes;

    @Override
    public void setAttribute(String key, Object val) {
        this.servletContextAttributes.put(key, val);
    }

    /**
     * 单例对象
     */
    private static ServletContext application = new MyServletContextImpl();
    private MyServletContextImpl() {
        this.servletContextAttributes = new ConcurrentHashMap<>();
        if(application != null){
            throw new RuntimeException("非法的操作");
        }
    }
    public static ServletContext getContext(){
        return application;
    }

    @Override
    public Object getAttribute(String key) {
        return this.servletContextAttributes.get(key);
    }

    @Override
    public void removeAttribute(String key) {

    }
}
