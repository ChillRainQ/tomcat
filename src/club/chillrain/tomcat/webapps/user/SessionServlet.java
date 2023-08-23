package club.chillrain.tomcat.webapps.user;

import club.chillrain.servlet.servlet.HttpServlet;
import club.chillrain.servlet.servlet.HttpSession;
import club.chillrain.servlet.servlet.ServletRequest;
import club.chillrain.servlet.servlet.ServletResponse;
import club.chillrain.servlet.annotation.WebServlet;
import club.chillrain.tomcat.webapps.user.bean.User;

import java.io.IOException;

/**
 * @author ChillRain 2023 08 17
 */
@WebServlet("/sessionTest")
public class SessionServlet extends HttpServlet {
    @Override
    public void service(ServletRequest request, ServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute("loginUser", new User(24, "Lidaxin", "man"));

    }
}
