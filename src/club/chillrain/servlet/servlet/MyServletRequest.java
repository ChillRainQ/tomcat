package club.chillrain.servlet.servlet;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.Map;

public interface MyServletRequest {
    HttpSession getSession();
    Cookie[] getCookies();
    ServletContext getServletContext();
    String getHeader(String key);

    Map<String, String> getHeaders();

    void setAttribute(String key, Object val);
    Object getAttribute(String key);
    /**
     * 获取这次请求的请求方法
     * @return
     */

    String getMethod();

    /**
     * 获取这次请求的URI
     * @return
     */
    String getRemoteURI();
    /**
     * 获取这次请求的URL
     * @return
     */
    String getRemoteURL();

    /**
     * 获取客户端IP
     * @return
     */
    String getRemoteAddr();

    /**
     * 通过key获取查询字符串参数
     * @return
     */
    String getParameter(String key);
    BufferedReader getReader();

    InputStream getInputStream();

    RequestDispatcher getRequestDispatcher(String uri);
}
