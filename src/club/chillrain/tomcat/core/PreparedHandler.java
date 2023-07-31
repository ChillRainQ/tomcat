package club.chillrain.tomcat.core;

import club.chillrain.servlet.HttpServlet;
import club.chillrain.servlet.annotation.WebServlet;
import club.chillrain.tomcat.constants.Constant;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 扫描项目中的所有类
 * @author ChillRain 2023 07 30
 */
public class PreparedHandler {
    /**
     * 全限定类名
     */
    private static List<String> allClasses = new ArrayList<>();
    /**
     * 递归获取文件夹中所有类的全限定类名
     * @param src 文件夹
     * @return 文件夹中所有类的全限定类名
     */
    public static List<String> getAllClasses(File src){
        if(src.isDirectory()){
            File[] files = src.listFiles();
            for (File file : files) {
                if(file.isDirectory()){//递归扫描文件夹
                    getAllClasses(file);
                }else{
                    if(file.getName().endsWith(".java")){
                        allClasses.add(getClassInfo(file));
                    }
                }
            }
        }
        return allClasses;
    }

    /**
     * 获取文件的全限定类名
     * @param file java文件
     * @return java文件的全限定类名
     */
    private static String getClassInfo(File file) {
        String parent = file.getParent();
        String packageName = parent.substring(parent.lastIndexOf("src\\")).replace("\\", ".").replace("src.", "");
        return packageName + "." + file.getName().replace(".java", "");
    }
    /**
     * 通过反射检查类上是否有@WebServlet，并获取其value值，放入List中
     * 使其与全限定类名做绑定
     * @param allClasses 全限定类名
     * @return
     */
    public static Map<String, String> initURIMapping(List<String> allClasses) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Map<String, String> map = new HashMap<>();
        for (String allClass : allClasses) {
            Class<?> clazz = Class.forName(allClass);
            WebServlet annotation = clazz.getAnnotation(WebServlet.class);
            if(annotation != null){//有@WebServlet
                map.put(annotation.value(), allClass);
                Class<?> superclass = clazz.getSuperclass();
                if(annotation.loadOnStartup() > 0 && superclass == HttpServlet.class){
                    Constant.servletMap.put(allClass, (HttpServlet) clazz.newInstance());
                    System.out.println("--->" + allClass + "已装载");
                }
            }
        }
        return map;
    }
}
