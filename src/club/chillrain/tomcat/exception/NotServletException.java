package club.chillrain.tomcat.exception;

/**
 * @author ChillRain 2023 08 20
 */
public class NotServletException extends Exception{
    private static final long serialVersionUID = 8866247359177082861L;

    public NotServletException(String message) {
        super(message);
    }
}
