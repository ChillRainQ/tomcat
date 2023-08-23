package club.chillrain.servlet.listener;

/**
 * @author ChillRain 2023 08 20
 */
public interface HttpSessionListener extends Listener{
    default void initSession(HttpSessionEvent event){

    }
    default void destroyed(HttpSessionEvent event){

    }
}
