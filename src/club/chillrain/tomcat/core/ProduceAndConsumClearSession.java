package club.chillrain.tomcat.core;

import club.chillrain.servlet.listener.HttpSessionListener;
import club.chillrain.tomcat.context.MyHttpSessionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

/**
 * 生产消费模式清理过期Session
 * @author ChillRain 2023 08 17
 */
public class ProduceAndConsumClearSession {
    private static final Logger LOGGER = LoggerFactory.getLogger("SessionClear");
    public volatile boolean flag = true;

    private BlockingQueue<Integer> queue;

    public ProduceAndConsumClearSession(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }
    /**
     * 生产随机的要检查的过期Session
     */
    public void produce() throws InterruptedException {
        int countCache = 5;
        while (this.flag){
            Set<Integer> sessionKeySet = SessionManager.sessionContainer.keySet();
            Integer[] sessionIds = sessionKeySet.toArray(new Integer[0]);
            if(sessionIds.length > 0){//Session的数量大于0
                Random random = new Random();
                int count = (sessionIds.length / 8) < 5 ? 5 : sessionIds.length / 8;//动态的确定要尝试淘汰多少
                if(count != countCache){
//                    LOGGER.debug("--->尝试标记次数：" + count);
                    countCache = count;
                }
                for (int i = 0; i < count; i++) {//在session集合中，随机选取5个塞入阻塞队列
                    int index = random.nextInt(sessionIds.length);
                    Integer sessionId = sessionIds[index];
                    if(!queue.contains(sessionId)){
                        queue.put(sessionId);
                    }
                }
            }
            Thread.sleep(1000);
        }
    }
    public void consume() throws InterruptedException {
        while(this.flag){
            Integer sessionId = this.queue.take();
            MyHttpSessionImpl session = (MyHttpSessionImpl) SessionManager.sessionContainer.get(sessionId);
            long currentTime = System.currentTimeMillis();
            //使用标记清楚法，为了快速清除
            if(currentTime - session.getCreatTime() >= session.getTTL() && !session.isClear()){
                //Session销毁监听
                HttpSessionListener httpSessionListener = session.getHttpSessionListener();
                httpSessionListener.destroyed(session.getEvent());


                session.setTtlMark(true);
//                LOGGER.info("Session: " + sessionId + "已经被定期标记为应当删除");
            }
        }
    }
}
