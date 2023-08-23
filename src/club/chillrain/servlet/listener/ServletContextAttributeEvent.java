package club.chillrain.servlet.listener;

import club.chillrain.servlet.servlet.ServletContext;

/**
 * @author ChillRain 2023 08 20
 */
public class ServletContextAttributeEvent extends ServletContextEvent{
    private String key;
    private Object val;

    public ServletContextAttributeEvent(ServletContext servletContext, String key, Object val) {
        super(servletContext);
        this.key = key;
        this.val = val;
    }

    public String getKey() {
        return key;
    }

    public Object getVal() {
        return val;
    }
}
