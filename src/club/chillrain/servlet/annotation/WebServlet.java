package club.chillrain.servlet.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface WebServlet {
    String value() default "";
    int loadOnStartup() default -1;
}
