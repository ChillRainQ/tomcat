package club.chillrain.tomcat.webapps.user.listener;

import club.chillrain.servlet.annotation.WebListener;
import club.chillrain.servlet.listener.ServletRequestAttributeEvent;
import club.chillrain.servlet.listener.ServletRequestAttributeListener;

/**
 * @author ChillRain 2023 08 23
 */
@WebListener
public class RequestAttributeListener implements ServletRequestAttributeListener {
    @Override
    public void addAttribute(ServletRequestAttributeEvent event) {
        System.out.println("添加了");
    }

    @Override
    public void updateAttribute(ServletRequestAttributeEvent event) {
        System.out.println("修改了");
    }

    @Override
    public void removeAttribute(ServletRequestAttributeEvent event) {
        System.out.println("删除了");
    }
}
