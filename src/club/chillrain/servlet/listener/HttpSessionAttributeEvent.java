package club.chillrain.servlet.listener;

import club.chillrain.servlet.servlet.HttpSession;

/**
 * @author ChillRain 2023 08 20
 */
public class HttpSessionAttributeEvent extends HttpSessionEvent{
    private String key;
    private Object val;

    public HttpSessionAttributeEvent(HttpSession httpSession, String key, Object val) {
        super(httpSession);
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
