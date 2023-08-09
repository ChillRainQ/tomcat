package club.chillrain.tomcat.impl;

import club.chillrain.servlet.MyServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求
 * @author ChillRain 2023 07 22
 */
public class MyHttpServletRequestImpl implements MyServletRequest {
    private static final Logger LOGGER = LoggerFactory.getLogger("servletRequest");
    /**
     * 请求的socket
     */
    private Socket socket;
    /**
     * 请求报文
     */
    private String requestContent;
    /**
     * 请求体
     */
    private String requestBody;
    /**
     * 请求中的K-V
     */
    private final Map<String, String> requestMap;
    /**
     * 按行分割的请求报文
     */
    private String[] headLine;
    /**
     * 二进制请求体数据
     */
    private byte[] requestBodyBytes;
    public MyHttpServletRequestImpl(Socket socket) throws IOException {
        this.socket = socket;
        this.requestMap = new HashMap<>();
        byte[] temp = new byte[8 * 1024];
        InputStream inputStream = socket.getInputStream();
        int len = inputStream.read(temp);
        //获取请求报文
        this.requestContent = new String(temp, 0, len);
//        System.out.println(requestContent);
        parseRequestHeader(requestContent);//解析请求头
        parseRequestBody(requestContent);//解析请求体
    }

    /**
     * 二进制请求体解析
     * @param incompleteBodyBytes
     * @throws IOException
     */
    private void parseBinaryBody(byte[] incompleteBodyBytes) throws IOException {
        if(requestMap.containsKey("content-length")){//可能包含二进制请求体
            int contentLength = Integer.parseInt(requestMap.get("content-length"));//请求体长度
            LOGGER.info("--->请求体长度：" + contentLength);
            int incompleteBodyLength = incompleteBodyBytes.length;//第一次中不完整的请求体长度
            LOGGER.info("--->不完整的请求体：" + incompleteBodyLength);
            int residueBodyLength = contentLength - incompleteBodyLength;
            LOGGER.info("--->还应当接收：" + residueBodyLength);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bufferBytes = null;
            if(residueBodyLength > 0){
                bufferBytes = new byte[65536];//一个TCP包的大小
            }
            byteArrayOutputStream.write(incompleteBodyBytes);//第一次中不完整的请求体
            int nowResidueBodyBytesLength = 0;
            //            while (nowResidueBodyBytesLength < residueBodyLength){//循环读取剩余的请求体
//                int readLength = inputStream.read(bufferBytes);
//                nowResidueBodyBytesLength += readLength;
//                byteArrayOutputStream.write(bufferBytes, 0 , readLength);
//            }
            InputStream inputStream = socket.getInputStream();
            int readLength = 0;
            while(residueBodyLength != 0){
                readLength = inputStream.read(bufferBytes);
                byteArrayOutputStream.write(bufferBytes, 0 , readLength);
                if(readLength < 65536) break;
            }
            byte[] requestBodyBytes = byteArrayOutputStream.toByteArray();
            LOGGER.info("--->完整的请求体长度：" + requestBodyBytes.length);
//            LOGGER.info("请求体报文：" + "\n" + new String(requestBodyBytes, StandardCharsets.UTF_8));
            byteArrayOutputStream.close();
            this.requestBodyBytes = requestBodyBytes;
        }
    }
    /**
     * 解析请求体
     * @param requestContent
     */
    private void parseRequestBody(String requestContent) throws IOException {
        int endHeaderIndex = requestContent.indexOf("\r\n\r\n");
        this.requestBody = requestContent.substring(endHeaderIndex + 4);
        String contentType = this.requestMap.get("content-type");
        if(contentType == null) {
            return;
        }
        if(contentType.trim().contains("multipart/form-data")){//请求体是携带了二进制数据的表单
            byte[] incompleteBodyBytes = this.requestBody.getBytes(StandardCharsets.ISO_8859_1);
            parseBinaryBody(incompleteBodyBytes);
        }
        if ("application/x-www-form-urlencoded".equals(contentType)){//请求体是表单
            parseKeyValueParamToMap(this.requestBody);
        }
    }

    /**
     * 请求头解析
     */
    private void parseRequestHeader(String requestContent) throws IOException {
        String ip = socket.getInetAddress().getHostAddress();
        int startHeaderIndex = requestContent.indexOf("\n");
        int endHeaderIndex = requestContent.indexOf("\r\n\r\n");
//        this.requestLine = requestContent.substring(0, endHeaderIndex - 1);
        String headerContent = requestContent.substring(startHeaderIndex + 1, endHeaderIndex);
        String[] split = headerContent.split("\r\n");
        this.headLine = requestContent.trim().split("\n");
        for (String str : split) {
            int i = str.indexOf(":");
            requestMap.put(str.substring(0, i).toLowerCase(), str.substring(i + 1).trim());
        }
        requestMap.put("ip", ip);
        paramAndUriParse();
    }
    /**
     * 请求参数和URI解析
     */
    private void paramAndUriParse() {
        int start = this.headLine[0].indexOf("/");
        int end = this.headLine[0].indexOf("HTTP");
        String uriAndQueryParam = this.headLine[0].substring(start, end - 1);
        String uri = null;
        if(uriAndQueryParam.contains("?")){//可能携带参数
            int line = uriAndQueryParam.indexOf("?");
            uri = uriAndQueryParam.substring(0, line);
            String queryParam = uriAndQueryParam.substring(line + 1);
            parseKeyValueParamToMap(queryParam);//解析请求参数
        }else{//不携带参数
            uri = uriAndQueryParam;
        }
        requestMap.put("uri", uri);
        requestMap.put("uriAndParamQuery", uriAndQueryParam);
    }

    /**
     * 解析请求参数
     * @param strs
     */
    private void parseKeyValueParamToMap(String strs){
        String[] queryParams = strs.split("&");
        if(queryParams.length != 0){//参数数量为0
            for (String param : queryParams) {
                if(param.contains("=")){
                    int i = param.indexOf("=");
                    requestMap.put(param.substring(0, i).toLowerCase(), param.substring(i + 1).trim());
                }
            }
        }
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
        String[] split = this.headLine[0].split(" ");
        return split[0];
    }

    /**
     * 获取请求的URI
     * @return
     */
    @Override
    public String getRemoteURI() {
        return requestMap.get("uri");
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
        String url = agreement.toLowerCase()//请求协议
                + "://" //分隔符
                + socket.getLocalAddress().getHostAddress()
                + this.requestMap.get("uriAndParamQuery");//请求资源与参数
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
        String param = requestMap.get(key);
        param = param == null ? "null" : param;
        return param;
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new StringReader(this.requestBody));
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.requestBodyBytes);
    }
}
