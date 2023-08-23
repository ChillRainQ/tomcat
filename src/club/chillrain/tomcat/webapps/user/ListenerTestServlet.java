package club.chillrain.tomcat.webapps.user;

import club.chillrain.servlet.annotation.WebServlet;
import club.chillrain.servlet.servlet.HttpServlet;
import club.chillrain.servlet.servlet.ServletRequest;
import club.chillrain.servlet.servlet.ServletResponse;

import java.io.IOException;

/**
 * @author ChillRain 2023 08 23
 */
@WebServlet("/listen")
public class ListenerTestServlet extends HttpServlet {
    @Override
    public void service(ServletRequest request, ServletResponse response) throws IOException {
        request.setAttribute("1", 1);
        request.setAttribute("1", 2);
        request.removeAttribute("1");
    }
}
