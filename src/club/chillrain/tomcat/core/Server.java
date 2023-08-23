package club.chillrain.tomcat.core;

import club.chillrain.tomcat.config.TomcatConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

/**
 * 启动类
 * @author ChillRain 2023 07 22
 */
public class Server {
    private final static Logger LOGGER = LoggerFactory.getLogger("Server");
    public void start() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Constant.init();//常量初始化
        TomcatConfig.tomcatInit();
        ServletRunner servletRunner = new ServletRunner();  //初始化Servlet运行环境
        servletRunner.init();
        ServerSocket serverSocket = Server.serverInit();//服务器初始化（读取配置文件）
        while (true) {
            LOGGER.info("--->" + Thread.currentThread() +"准备接收HTTP请求");
            Socket socket = serverSocket.accept();
            Work work = new Work(socket);
            LOGGER.info("--->" + Thread.currentThread() +"已分发HTTP请求");
            TomcatConfig.workThreadPool.execute(work);
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
        LOGGER.info("--->服务开始于端口：" + port);
        return serverSocket;
    }

    /**
     * 工作线程的工作内容
     */
    static class Work implements Runnable{
        private final static Logger LOGGER = LoggerFactory.getLogger("Work");
        Socket socket;
        public Work(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                new ServletRunner.Processor(socket).process();
                socket.close();
                LOGGER.info("--->已完成请求");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }
}