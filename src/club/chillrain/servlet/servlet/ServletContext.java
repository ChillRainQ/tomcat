package club.chillrain.servlet.servlet;

/**
 * @author ChillRain 2023 08 20
 */
public interface ServletContext {
    void setAttribute(String key, Object val);
    Object getAttribute(String key);
    Boolean removeAttribute(String key);
}
