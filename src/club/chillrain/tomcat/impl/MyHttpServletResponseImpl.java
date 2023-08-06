package club.chillrain.tomcat.impl;

import club.chillrain.servlet.MyServletResponse;
import club.chillrain.tomcat.constants.Constant;
import club.chillrain.tomcat.enums.Status;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ChillRain 2023 07 22
 */
public class MyHttpServletResponseImpl implements MyServletResponse {
    /**
     * 响应头表
     */
    private Map<String, String> headMap = new HashMap<>();
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
    private OutputStream outputStream;

    public MyHttpServletResponseImpl(Socket socket) throws IOException {
        this.status = 200;
        this.outputStream = socket.getOutputStream();
        headMap.put("Content-Type", "text/html;charset=utf8");
        this.byteStream = new ByteArrayOutputStream();
        this.writer = new PrintWriter(byteStream);

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
    private void prepareResponseHeader(){//响应头准备
        this.content = new StringBuffer();
        this.content.append("HTTP/1.1 ").append(this.status).append(" " + Constant.statusMap.get(this.status)).append("\r\n");//拼接响应行
        for(String key : headMap.keySet()){//拼接响应头
            this.content.append(key).append(": ").append(headMap.get(key)).append("\r\n");
        }
        this.content.append("\r\n");
    }
    public void finishedResponse() throws IOException {
        this.writer.flush();
        byte[] byteArray = this.byteStream.toByteArray();//用户写入的内容
        prepareResponseHeader();
        //写入响应头
        this.outputStream.write(this.content.toString().getBytes(StandardCharsets.UTF_8));
        //写入响应体
        this.outputStream.write(byteArray);
        this.writer.close();
        this.byteStream.close();
        System.out.println("--->响应已返回");
    }
}
