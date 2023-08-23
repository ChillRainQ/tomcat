package club.chillrain.servlet.listener;

import club.chillrain.servlet.servlet.HttpSession;

/**
 * @author ChillRain 2023 08 20
 */
public class HttpSessionEvent {
    private HttpSession httpSession;

    public HttpSessionEvent(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }
}
