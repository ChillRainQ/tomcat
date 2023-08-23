package club.chillrain.tomcat.interfaces.abstracts;

import club.chillrain.servlet.servlet.Servlet;
import club.chillrain.tomcat.interfaces.Factory;

/**
 * @author ChillRain 2023 08 08
 */
public abstract class ServletFactory implements Factory {

    public abstract Servlet createServlet(Class clazz) throws InstantiationException, IllegalAccessException;
}
