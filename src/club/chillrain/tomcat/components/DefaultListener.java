package club.chillrain.tomcat.components;

import club.chillrain.servlet.listener.*;

/**
 * 默认监听器
 * @author ChillRain 2023 08 10
 */
public class DefaultListener implements
        HttpSessionListener,
        HttpSessionAttributeListener,
        ServletRequestListener,
        ServletRequestAttributeListener,
        ServletContextListener,
        ServletContextAttributeListener {
}
