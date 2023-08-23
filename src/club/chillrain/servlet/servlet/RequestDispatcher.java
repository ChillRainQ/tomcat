package club.chillrain.servlet.servlet;

/**
 * 请求控制器
 */
public interface RequestDispatcher {
    void include(ServletRequest request, ServletResponse response);

    void forward(ServletRequest request, ServletResponse response);
}
