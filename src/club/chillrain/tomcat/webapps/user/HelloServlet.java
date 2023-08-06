package club.chillrain.tomcat.webapps.user;

import club.chillrain.servlet.HttpServlet;
import club.chillrain.servlet.MyServletRequest;
import club.chillrain.servlet.MyServletResponse;
import club.chillrain.servlet.annotation.WebServlet;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author ChillRain 2023 08 02
 */
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    @Override
    public void service(MyServletRequest request, MyServletResponse response) throws IOException {
        String name = request.getParameter("name");
        response.getWriter().write(name);
        response.getWriter().write(request.getRemoteAddr());
        BufferedReader reader = request.getReader();
        String temp ="";
        StringBuilder sb = new StringBuilder();
        while((temp = reader.readLine()) != null){
            sb.append(temp);
        }
        System.out.println(sb);
        reader.close();
    }
}
