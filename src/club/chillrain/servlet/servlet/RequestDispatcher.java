package club.chillrain.servlet.servlet;

/**
 * 请求控制器
 */
public interface RequestDispatcher {
    void include(MyServletRequest request, MyServletResponse response);

    void forward(MyServletRequest request, MyServletResponse response);
}
