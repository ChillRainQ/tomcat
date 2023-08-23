package club.chillrain.servlet.listener;

import club.chillrain.servlet.servlet.ServletRequest;
import club.chillrain.servlet.servlet.ServletContext;

/**
 * @author ChillRain 2023 08 20
 */
public class ServletRequestEvent {
    private ServletRequest servletRequest;

    public ServletRequestEvent(ServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    public ServletRequest getServletRequest() {
        return servletRequest;
    }
    public ServletContext getServletContext(){
        return this.servletRequest.getServletContext();
    }
}
