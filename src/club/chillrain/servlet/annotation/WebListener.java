package club.chillrain.servlet.annotation;

import java.lang.annotation.*;

/**
 * 监听器注解
 * @author ChillRain 2023 08 20
 */
@Target({ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface WebListener {
}
