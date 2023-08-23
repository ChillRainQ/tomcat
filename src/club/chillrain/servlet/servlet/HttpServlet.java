package club.chillrain.servlet.servlet;

import java.io.IOException;

/**
 * @author ChillRain 2023 07 30
 */
public abstract class HttpServlet implements Servlet{

    @Override
    public void init() {

    }

    @Override
    public void service(MyServletRequest request, MyServletResponse response) throws IOException {

    }

    @Override
    public void destory() {

    }
}
