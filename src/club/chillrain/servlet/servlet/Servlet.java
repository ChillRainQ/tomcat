package club.chillrain.servlet.servlet;

import java.io.IOException;

public interface Servlet {
    public void init();
    void service(MyServletRequest request, MyServletResponse response) throws IOException;

    void destory();
}
