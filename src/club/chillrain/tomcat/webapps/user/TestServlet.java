package club.chillrain.tomcat.webapps.user;

import club.chillrain.servlet.servlet.Cookie;
import club.chillrain.servlet.servlet.HttpServlet;
import club.chillrain.servlet.servlet.ServletRequest;
import club.chillrain.servlet.servlet.ServletResponse;
import club.chillrain.servlet.annotation.WebServlet;

/**
 * @author ChillRain 2023 07 30
 */
@WebServlet(value = "/test", loadOnStartup = 1)
public class TestServlet extends HttpServlet {
    @Override
    public void service(ServletRequest request, ServletResponse response) {
//        response.getWriter().write((request.getCookies()[0].getKey()));
        Cookie cookie1 = new Cookie("test", "lidaxin");
        cookie1.setMaxAge(36000L);
        cookie1.setPath("/test");
        cookie1.setHttpOnly(false);
        Cookie cookie2 = new Cookie("a", "b");
        cookie2.setHttpOnly(true);
        response.addCookie(cookie1);
        response.addCookie(cookie2);

    }
}
