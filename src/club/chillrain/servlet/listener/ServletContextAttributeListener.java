package club.chillrain.servlet.listener;

/**
 * @author ChillRain 2023 08 20
 */
public interface ServletContextAttributeListener extends Listener{
    default void addAttribute(ServletContextAttributeEvent event) {

    }

    default void updateAttribute(ServletContextAttributeEvent event) {

    }


    default void removeAttribute(ServletContextAttributeEvent event) {

    }
}
