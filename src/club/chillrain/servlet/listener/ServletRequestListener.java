package club.chillrain.servlet.listener;

/**
 * @author ChillRain 2023 08 20
 */
public interface ServletRequestListener extends Listener{
    default void initRequest(ServletRequestEvent event){

    }
    default void destroyed(ServletRequestEvent event){

    }
}
