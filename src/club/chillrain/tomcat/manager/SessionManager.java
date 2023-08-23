package club.chillrain.tomcat.manager;

import club.chillrain.servlet.listener.HttpSessionListener;
import club.chillrain.servlet.servlet.HttpSession;
import club.chillrain.tomcat.context.MyHttpSessionImpl;
import club.chillrain.tomcat.core.ProduceAndConsumClearSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Session管理器
 * @author ChillRain 2023 08 17
 */
public class SessionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("SessionManager");
    public static AtomicInteger sessionIdManager = new AtomicInteger(0);

    public static Map<Integer, HttpSession> sessionContainer = new ConcurrentHashMap<>();
    private static ProduceAndConsumClearSession sessionMarker = new ProduceAndConsumClearSession(new ArrayBlockingQueue<>(100));


    /**
     * 需要获取的时候，前端会传输一个Cookie: JSESSIONID,这个就是Session的ID，是Session的标识
     * @param jSessionId
     * @return
     */
    public static HttpSession getSession(Integer jSessionId){
        MyHttpSessionImpl httpSession = (MyHttpSessionImpl) sessionContainer.get(jSessionId);
        if(httpSession != null){//自动续期
            if(httpSession.isClear()){//应当被清除
                return null;
            }
            long nowTime = System.currentTimeMillis();
            if(nowTime - httpSession.getCreatTime() >= httpSession.getTTL()){//都没被检查过，惰性删除
                LOGGER.info("Session：" + httpSession.getId() + "进行惰性删除标记");

                //Session销毁监听
                HttpSessionListener httpSessionListener = httpSession.getHttpSessionListener();
                httpSessionListener.destroyed(httpSession.getEvent());


                httpSession.setTtlMark(true);
                return null;
            }
            httpSession.setCreatTime(System.currentTimeMillis());
        }
        return httpSession;
    }

    /**
     * 创建Session并且放入容器
     * @return
     */
    public static HttpSession initAndGetSession(){
        MyHttpSessionImpl httpSession = new MyHttpSessionImpl();
        sessionContainer.put(httpSession.getId(), httpSession);
        return httpSession;
    }
    static{
        //生产随机Session准备检查过期
        new Thread(() -> {
            LOGGER.info("随机检查线程已启动");
            try {
                sessionMarker.produce();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        //消费随机Session进行过期检查
        new Thread(() -> {
            LOGGER.info("标记删除线程已启动");
            try {
                sessionMarker.consume();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        //清除过期Session
        new Thread(() -> {
            LOGGER.info("删除Session线程已启动");
            while (true){
                try {
                    Set<Integer> sessionIds = sessionContainer.keySet();
                    for (Integer sessionId : sessionIds) {
                        MyHttpSessionImpl session = (MyHttpSessionImpl)sessionContainer.get(sessionId);
                        if(session.isClear()){
                            sessionContainer.remove(sessionId);
//                            LOGGER.info("Session：" + sessionId + "已清除");
                        }
                    }
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
