package club.chillrain.tomcat.webapps.user.listener;

import club.chillrain.servlet.annotation.WebListener;
import club.chillrain.servlet.listener.ServletContextEvent;

/**
 * @author ChillRain 2023 08 23
 */
@WebListener
public class ServletContextListener implements club.chillrain.servlet.listener.ServletContextListener {
    @Override
    public void initServletContext(ServletContextEvent event) {
        System.out.println("servletContext初始化了");
    }

    @Override
    public void destroyed(ServletContextEvent event) {
        System.out.println("servletContext销毁");
    }
}
