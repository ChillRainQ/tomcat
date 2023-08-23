package club.chillrain.servlet.listener;

/**
 * @author ChillRain 2023 08 20
 */
public interface HttpSessionAttributeListener extends Listener{
    default void addAttribute(HttpSessionAttributeEvent httpSessionAttributeEvent) {

    }

    default void updateAttribute(HttpSessionAttributeEvent httpSessionAttributeEvent) {

    }


    default void removeAttribute(HttpSessionAttributeEvent httpSessionAttributeEvent) {

    }
}
