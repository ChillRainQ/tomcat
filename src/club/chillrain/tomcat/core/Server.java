package club.chillrain.tomcat.core;

import club.chillrain.servlet.HttpServlet;
import club.chillrain.servlet.MyServletRequest;
import club.chillrain.servlet.MyServletResponse;
import club.chillrain.tomcat.constants.Constant;
import club.chillrain.tomcat.enums.Status;
import club.chillrain.tomcat.impl.MyHttpServletRequestImpl;
import club.chillrain.tomcat.impl.MyHttpServletResponseImpl;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 启动类
 * @author ChillRain 2023 07 22
 */
public class Server {
    //    public static Map<String, HttpServlet> servletMap = new HashMap<>();
    public static void main(String[] args) throws IOException {
        Constant.init();//常量初始化
        ServerSocket serverSocket = Server.serverInit();//服务器初始化（读取配置文件）
        while (true) {
            System.out.println("--->" + Thread.currentThread() +"准备接收HTTP请求");
            Socket socket = serverSocket.accept();
            Work work = new Work(socket);
            System.out.println("--->" + Thread.currentThread() +"已分发HTTP请求");
            Constant.servletPool.execute(work);
        }
    }

    /**
     * 服务器初始化
     * @return
     * @throws IOException
     */
    public static ServerSocket serverInit() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("resources/config.properties"));
        int port = 8080;
        int readPort = Integer.valueOf((String) properties.get("tomcat.port"));
        port = readPort != 0 ? readPort : port;
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("0.0.0.0", port));
        System.out.println("--->服务开始于端口：" + port);
        return serverSocket;
    }

    /**
     * 工作线程的工作内容
     */
    static class Work implements Runnable{
        Socket socket;
        public Work(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                new HttpProcessor(socket).process();
                socket.close();
                System.out.println("--->已完成请求");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}