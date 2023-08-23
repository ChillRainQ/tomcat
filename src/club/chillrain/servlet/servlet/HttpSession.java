package club.chillrain.servlet.servlet;

/**
 * @author ChillRain 2023 08 17
 */
public interface HttpSession {
    Integer getId();

    void setAttribute(String key, Object val);

    Object getAttribute(String key);
    Boolean removeAttribute(String key);

    void invalidate();
}
