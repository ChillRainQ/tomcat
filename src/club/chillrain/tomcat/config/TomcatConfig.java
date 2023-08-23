package club.chillrain.tomcat.config;

/**
 * @author ChillRain 2023 08 03
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * tomcat配置
 */
public class TomcatConfig {
    public static final ThreadPoolExecutor workThreadPool;

    static {
        try {
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
