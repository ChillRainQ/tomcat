package club.chillrain.tomcat.exception;

/**
 * @author ChillRain 2023 08 22
 */
public class NotListenerException extends Exception{
    private static final long serialVersionUID = -3873181562427711292L;

    public NotListenerException(String message) {
        super(message);
    }
}
