package club.chillrain.tomcat.webapps.user;

import club.chillrain.servlet.HttpServlet;
import club.chillrain.servlet.MyServletRequest;
import club.chillrain.servlet.MyServletResponse;
import club.chillrain.servlet.annotation.WebServlet;

/**
 * @author ChillRain 2023 07 30
 */
@WebServlet(value = "/test", loadOnStartup = 1)
public class TestServlet extends HttpServlet {
    @Override
    public void service(MyServletRequest request, MyServletResponse response) {
        response.getWriter().write("<h1>Write by service</h1>");
    }
}
