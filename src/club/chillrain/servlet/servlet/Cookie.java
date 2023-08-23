package club.chillrain.servlet.servlet;

import java.util.Date;

/**
 * @author ChillRain 2023 08 13
 */
public class Cookie {
    private String key;
    private String value;
    private Long maxAge;
    private String path;
    private String expires;
    private boolean httpOnly;

    public String getExpires() {
        return expires;
    }

    public boolean isHttpOnly() {
        return this.httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public void setExpires() {
        long time = System.currentTimeMillis();
        long endTime = time + maxAge * 1000L;
        Date date = new Date(endTime);
        String dateStr = date.toString();
        String[] split = dateStr.split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(split[0])
                .append(", ")
                .append(split[2])
                .append("-")
                .append(split[1])
                .append("-")
                .append(split[split.length - 1])
                .append(" ")
                .append(split[3])
                .append(" ")
                .append("GMT");
        this.expires = stringBuilder.toString();
    }

    public Cookie(String key, String value) {
        this.key = key;
        this.value = value;
        this.path = "/";
        this.maxAge = 0L;
        this.httpOnly = true;

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Long maxAge) {
        this.maxAge = maxAge;
        this.setExpires();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
