package club.chillrain.tomcat.factory;

import club.chillrain.servlet.servlet.HttpServlet;
import club.chillrain.tomcat.interfaces.abstracts.ServletFactory;

/**
 * @author ChillRain 2023 08 08
 */
public class ServletFactoryImpl extends ServletFactory {

    @Override
    public HttpServlet createServlet(Class clazz) throws InstantiationException, IllegalAccessException {
        return (HttpServlet) clazz.newInstance();
    }
}
