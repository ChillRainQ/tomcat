package club.chillrain.tomcat.webapps.user.listener;

import club.chillrain.servlet.annotation.WebListener;
import club.chillrain.servlet.listener.HttpSessionListener;
import club.chillrain.servlet.listener.ServletRequestEvent;
import club.chillrain.servlet.listener.ServletRequestListener;

/**
 * @author ChillRain 2023 08 22
 */
@WebListener
public class MyListener implements ServletRequestListener {
    @Override
    public void initRequest(ServletRequestEvent event) {
        System.out.println("运行了自定义的监听器");
    }

    @Override
    public void destroyed(ServletRequestEvent event) {
        System.out.println("Request销毁");
    }
}
