package club.chillrain.servlet.listener;

import club.chillrain.servlet.servlet.ServletRequest;

/**
 * @author ChillRain 2023 08 20
 */
public class ServletRequestAttributeEvent extends ServletRequestEvent{
    private String key;
    private Object obj;

    public ServletRequestAttributeEvent(ServletRequest servletRequest, String key, Object obj) {
        super(servletRequest);
        this.key = key;
        this.obj = obj;
    }

    public String getKey() {
        return key;
    }

    public Object getObj() {
        return obj;
    }
}
