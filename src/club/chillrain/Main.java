package club.chillrain;

import club.chillrain.tomcat.core.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Server httpServer = new Server();
        httpServer.start();
    }
}
