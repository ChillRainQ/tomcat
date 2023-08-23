package club.chillrain.tomcat.webapps.user;

import club.chillrain.servlet.servlet.HttpServlet;
import club.chillrain.servlet.servlet.MyServletRequest;
import club.chillrain.servlet.servlet.MyServletResponse;
import club.chillrain.servlet.annotation.WebServlet;

import java.io.*;

/**
 * @author ChillRain 2023 08 02
 */
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    @Override
    public void service(MyServletRequest request, MyServletResponse response) throws IOException {
//        String name = request.getParameter("name");
//        response.getWriter().write(name);
//        response.getWriter().write(request.getRemoteAddr());
        response.setContentType("image/png");
//        InputStream inputStream = request.getInputStream();
        OutputStream outputStream = response.getOutputStream();
        int len = 0;
        byte[] buffer = new byte[65536];
        FileInputStream fis = new FileInputStream(new File("C:\\JavaCode\\tomcat\\resources\\wakamo.png"));
        BufferedInputStream bis = new BufferedInputStream(fis);
        while((len = bis.read(buffer)) != -1){
            outputStream.write(buffer, 0 , len);
        }
        outputStream.flush();
        outputStream.close();

//        while ()
//        BufferedReader reader = request.getReader();
//        String temp ="";
//        StringBuilder sb = new StringBuilder();
//        while((temp = reader.readLine()) != null){
//            sb.append(temp);
//        }
////        System.out.println(sb);
//        reader.close();
        request.getSession();
    }
}
