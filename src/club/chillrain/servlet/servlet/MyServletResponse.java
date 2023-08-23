package club.chillrain.servlet.servlet;

import java.io.OutputStream;
import java.io.PrintWriter;

public interface MyServletResponse {
    /**
     * 设置字符集
     * @param charset
     */
    void setCharacterEncoding(String charset);

    /**
     * 设置字段
     * @param type
     */
    void setContentType(String type);

    /**
     * 设置writer
     * @return
     */
    PrintWriter getWriter();

    /**
     * 设定头
     */
    void setHeader(String key, String val);

    /**
     * 设定响应码
     */
    void setStatus(Integer status);

    OutputStream getOutputStream();
    void setRedirect(String uri);

    /**
     * 清空响应体
     */
    void reset();
    void addCookie(Cookie cookie);
}
