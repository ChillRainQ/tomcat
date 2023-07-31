package club.chillrain.servlet;

public interface Servlet {
    public void init();
    void service(MyServletRequest request, MyServletResponse response);

    void destory();
}
