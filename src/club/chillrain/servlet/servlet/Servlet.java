package club.chillrain.servlet.servlet;

import java.io.IOException;

public interface Servlet {
    public void init();
    void service(ServletRequest request, ServletResponse response) throws IOException;

    void destory();
}
