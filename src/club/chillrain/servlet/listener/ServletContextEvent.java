package club.chillrain.servlet.listener;

import club.chillrain.servlet.servlet.ServletContext;

/**
 * @author ChillRain 2023 08 20
 */
public class ServletContextEvent {
    private ServletContext servletContext;

    public ServletContextEvent(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }
}
