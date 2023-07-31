package club.chillrain.servlet;

import java.util.Map;

public interface MyServletRequest {
    /**
     * 通过key获取val
     * @param key
     * @return
     */
    String getHeader(String key);

    /**
     * 获取所有的请求头的key
     *
     * @return
     */
    Map<String, String> getHeaders();

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
}
