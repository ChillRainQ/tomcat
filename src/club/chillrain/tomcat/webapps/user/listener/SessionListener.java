package club.chillrain.tomcat.webapps.user.listener;

import club.chillrain.servlet.annotation.WebListener;
import club.chillrain.servlet.listener.HttpSessionEvent;
import club.chillrain.servlet.listener.HttpSessionListener;

/**
 * @author ChillRain 2023 08 22
 */
@WebListener
public class SessionListener implements HttpSessionListener {
    @Override
    public void initSession(HttpSessionEvent event) {
        System.out.println("Seesion开始了");
    }

    @Override
    public void destroyed(HttpSessionEvent event) {
        System.out.println("Session要销毁");
    }
}
