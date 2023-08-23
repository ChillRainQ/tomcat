package club.chillrain.servlet.listener;

import club.chillrain.servlet.servlet.MyServletRequest;
import club.chillrain.servlet.servlet.ServletContext;

/**
 * @author ChillRain 2023 08 20
 */
public class ServletRequestEvent {
    private MyServletRequest servletRequest;

    public ServletRequestEvent(MyServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    public MyServletRequest getServletRequest() {
        return servletRequest;
    }
    public ServletContext getServletContext(){
        return this.servletRequest.getServletContext();
    }
}
