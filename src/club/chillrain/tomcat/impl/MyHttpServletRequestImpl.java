package club.chillrain.tomcat.impl;

import club.chillrain.servlet.MyServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求
 * @author ChillRain 2023 07 22
 */
public class MyHttpServletRequestImpl implements MyServletRequest {
    /**
     * 请求报文
     */
    private final String requestContent;
    /**
     * 请求中的K-V
     */
    private final Map<String, String> requestMap;
    /**
     * 请求行
     */
    private final String requestLine;
    /**
     * 按行分割的把报文
     */
    private final String[] headLine;
    public MyHttpServletRequestImpl(Socket socket, InputStream inputStream) throws IOException {
        byte[] temp = new byte[8 * 1024];
        String ip = socket.getInetAddress().getHostAddress();
        int len = inputStream.read(temp);
        this.requestContent = new String(temp, 0, len);
        this.requestMap = new HashMap<>();
//        System.out.println("--->这里是完整的请求报文：");
//        System.out.println(requestContent);
//        System.out.println("--->请求报文结束");
        int startHeaderIndex = requestContent.indexOf("\n");
        int endHeaderIndex = requestContent.indexOf("\r\n\r\n");
        String headerContent = requestContent.substring(startHeaderIndex + 1, endHeaderIndex);
        String[] split = headerContent.split("\r\n");
        this.headLine = requestContent.trim().split("\n");
        this.requestLine = requestContent.substring(0, endHeaderIndex - 1);
        for (String str : split) {
            int i = str.indexOf(":");
            requestMap.put(str.substring(0, i).toLowerCase(), str.substring(i + 1).trim());
        }
        requestMap.put("ip", ip);
    }

    /**
     * 获取请求头中的value
     * @param key
     * @return
     */
    @Override
    public String getHeader(String key) {
        return requestMap.get(key.toLowerCase());
    }

    /**
     * 获取请求头
     * @return
     */
    @Override
    public Map<String, String> getHeaders() {
        return this.requestMap;
    }

    /**
     * 获取请求方法
     * @return
     */
    @Override
    public String getMethod() {
        String[] split = this.requestLine.split(" ");
        return split[0];
    }

    /**
     * 获取请求的URI
     * @return
     */
    @Override
    public String getRemoteURI() {
//        String[] split = this.requestLine.split("\r\n");
//        String firstLine = split[0];
//        int start = firstLine.indexOf("/");
//        int end = firstLine.indexOf("HTTP");
//        String uri = firstLine.substring(start, end - 1);
        int start = this.headLine[0].indexOf("/");
        int end = this.headLine[0].indexOf("HTTP");
        String uri = this.headLine[0].substring(start, end - 1);
        return uri;
    }

    /**
     * 获取请求的URL
     * @return
     */
    @Override
    public String getRemoteURL() {
        int start = this.headLine[0].lastIndexOf(" ");
        int end = this.headLine[0].lastIndexOf("/");
        String agreement = this.headLine[0].substring(start + 1, end);
        String url = agreement.toLowerCase() +"://" + getHeader("host") + getRemoteURI();
        return url.trim();
    }

    /**
     * 获取请求的IP
     * @return
     */
    @Override
    public String getRemoteAddr() {
        return requestMap.get("ip");
    }

    /**
     * 获取param
     * @param key
     * @return
     */
    @Override
    public String getParameter(String key) {
        return requestMap.get(key);
    }
}
