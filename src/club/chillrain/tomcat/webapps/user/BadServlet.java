package club.chillrain.tomcat.webapps.user;

import club.chillrain.servlet.servlet.HttpServlet;
import club.chillrain.servlet.servlet.ServletRequest;
import club.chillrain.servlet.servlet.ServletResponse;
import club.chillrain.servlet.servlet.RequestDispatcher;
import club.chillrain.servlet.annotation.WebServlet;

import java.io.IOException;

/**
 * @author ChillRain 2023 07 31
 */
@WebServlet(value = "/bad", loadOnStartup = 1)
public class BadServlet extends HttpServlet {
    @Override
    public void service(ServletRequest request, ServletResponse response) throws IOException {
        response.getWriter().write("11111");
//        request.setAttribute("test", new User());
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/test");
        requestDispatcher.forward(request, response);
//        response.setRedirect("/hello");

    }
}
