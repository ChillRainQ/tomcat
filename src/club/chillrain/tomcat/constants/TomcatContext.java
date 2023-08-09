package club.chillrain.tomcat.constants;

/**
 * @author ChillRain 2023 08 03
 */

import club.chillrain.servlet.HttpServlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * tomcat配置
 */
public class TomcatContext {
    public static final ThreadPoolExecutor workThreadPool;
    public static final Map<String, HttpServlet> servletContext;


    static {
        try {
            servletContext = new HashMap<>();
            workThreadPool = servletPoolInit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ThreadPoolExecutor servletPoolInit() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("resources/config.properties"));
        return new ThreadPoolExecutor(
                Integer.valueOf((String) properties.get("tomcat.core-poolsize")),
                Integer.valueOf((String) properties.get("tomcat.max-threads")),
                Integer.valueOf((String) properties.get("tomcat.keep-alivetime")),
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    public static void tomcatInit(){}
}
