package club.chillrain.servlet.listener;

import club.chillrain.servlet.servlet.MyServletRequest;

/**
 * @author ChillRain 2023 08 20
 */
public class ServletRequestAttribueEvent extends ServletRequestEvent{
    private String key;
    private Object obj;

    public ServletRequestAttribueEvent(MyServletRequest servletRequest, String key, Object obj) {
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
