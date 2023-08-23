package club.chillrain.tomcat.context.request;

import club.chillrain.servlet.servlet.Cookie;
import club.chillrain.servlet.servlet.ServletResponse;
import club.chillrain.tomcat.core.Constant;
import club.chillrain.tomcat.enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ChillRain 2023 07 22
 */
public class HttpServletResponseImpl implements ServletResponse {
    private static final Logger LOGGER = LoggerFactory.getLogger("Response");
    /**
     * HTTP请求的Socket连接
     */
    private final Socket socket;
    /**
     * 响应头表
     */
    private Map<String, String> headMap = new HashMap<>();
    /**
     * 响应体写入
     */
    private PrintWriter writer;
    /**
     * 响应码
     */
    private Integer status;
    /**
     * 响应报文
     */
    private StringBuffer content;
    /**
     * 接收writer的流
     */
    private ByteArrayOutputStream byteStream;
    /**
     * 响应报文的输出流
     */
    private OutputStream userOutputStream;
    private List<StringBuffer> cookieBuffers;
//    private StringBuffer cookieBuffer;

    public HttpServletResponseImpl(Socket socket) throws IOException {
        this.socket = socket;
        this.status = 200;
//        this.outputStream = socket.getOutputStream();
        headMap.put("Content-Type", "text/html;charset=utf8");
        this.byteStream = new ByteArrayOutputStream();
        this.writer = new PrintWriter(byteStream);
        this.userOutputStream = new ByteArrayOutputStream();

    }

    @Override
    public void setCharacterEncoding(String charset) {
    }

    @Override
    public void setContentType(String type) {
        headMap.put("Content-Type", type);
    }

    @Override
    public PrintWriter getWriter() {
        return this.writer;
    }

    @Override
    public void setHeader(String key, String val) {
        headMap.put(key, val);
    }

    @Override
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public OutputStream getOutputStream() {
        return this.userOutputStream;
    }

    @Override
    public void setRedirect(String uri) {
        this.setStatus(Status.HTTP_302.getCode());
        this.setHeader("location", uri);
    }

    @Override
    public void reset() {//重置写入区
        this.byteStream.reset();
    }

    @Override
    public void addCookie(Cookie cookie) {
//        if(cookieBuffer == null) {
//            cookieBuffer = new StringBuffer();
//        }
        if(cookieBuffers == null){
            cookieBuffers = new ArrayList<>();
        }
        StringBuffer cookieBuffer = new StringBuffer();
        cookieBuffer
                .append(cookie.getKey()).append("=").append(cookie.getValue()).append(";")
                .append("Path").append("=").append(cookie.getPath()).append(";");
        if (cookie.getMaxAge() > 0){
            cookieBuffer
                    .append("Max-Age").append("=").append(cookie.getMaxAge()).append(";")
                    .append("Expires").append("=").append(cookie.getExpires()).append(";");
        }
        if(cookie.isHttpOnly()){
            cookieBuffer.append("HttpOnly");
        }
        cookieBuffers.add(cookieBuffer);

    }

    private void prepareResponseHeader(){//响应头准备
        this.content = new StringBuffer();
        this.content.append("HTTP/1.1 ").append(this.status).append(" " + Constant.statusMap.get(this.status)).append("\r\n");//拼接响应行
//        if(this.cookieBuffer != null){
//            this.headMap.put("Set-Cookie", this.cookieBuffer.toString());
//        }
        if(this.cookieBuffers != null){
//            this.headMap.put("Set-Cookie", this.cookieBuffer.toString());
            for(StringBuffer sb : this.cookieBuffers){
                content.append("Set-Cookie: ")
                        .append(sb.toString())
                        .append("\r\n");
            }
        }
        for(String key : headMap.keySet()){//拼接响应头
            this.content.append(key).append(": ").append(headMap.get(key)).append("\r\n");
        }
        this.content.append("\r\n");
    }
    public void finishedResponse() throws IOException {
        this.writer.flush();
        byte[] byteArray = this.byteStream.toByteArray();//用户写入的内容
        OutputStream outputStream = socket.getOutputStream();
        prepareResponseHeader();
        //写入响应头
        outputStream.write(this.content.toString().getBytes(StandardCharsets.UTF_8));
        //写入响应体
        outputStream.write(byteArray);
        outputStream.write(((ByteArrayOutputStream)this.userOutputStream).toByteArray());
        this.writer.close();
        this.byteStream.close();
//        System.out.println("--->响应已返回");
        LOGGER.info("--->响应已返回");
    }
}
