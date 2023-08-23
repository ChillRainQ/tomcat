package club.chillrain.servlet.listener;

/**
 * @author ChillRain 2023 08 20
 */
public interface ServletRequestAttributeListener extends Listener{
    default void addAttribute(ServletRequestAttributeEvent event) {

    }

    default void updateAttribute(ServletRequestAttributeEvent event) {

    }


    default void removeAttribute(ServletRequestAttributeEvent event) {

    }
}
