package club.chillrain.tomcat.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP响应码
 */
public enum Status {
    HTTP_100(100, "continue"),
    HTTP_200(200, "OK"),
    HTTP_201(201, "Created"),
    HTTP_202(202, "Accepted"),
    HTTP_204(204, "No Content"),
    HTTP_404(404, "Not Found")
    ;
    Integer code;
    String message;

    Status(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Map<Integer, String> init(){
        Map<Integer, String> map = new HashMap<>();
        for (Status status : Status.values()){
            map.put(status.getCode(), status.getMessage());
        }
        return map;
    }
}
