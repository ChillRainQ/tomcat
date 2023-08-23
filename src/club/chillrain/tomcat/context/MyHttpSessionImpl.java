package club.chillrain.tomcat.context;

import club.chillrain.servlet.listener.HttpSessionAttributeListener;
import club.chillrain.servlet.listener.HttpSessionEvent;
import club.chillrain.servlet.listener.HttpSessionListener;
import club.chillrain.servlet.servlet.HttpSession;
import club.chillrain.tomcat.core.SessionManager;
import club.chillrain.tomcat.factory.ListenerFactoryImpl;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Session域
 * @author ChillRain 2023 08 17
 */
public class MyHttpSessionImpl implements HttpSession {
    private static final SessionManager manager = new SessionManager();

    private HttpSessionListener httpSessionListener;
    private HttpSessionAttributeListener httpSessionAttributeListener;
    private HttpSessionEvent event;
    /**
     * Session的id标识
     */
    private Integer id;
    /**
     * 创建时间
     */
    private Long creatTime;
    /**
     * 存活时间
     */
    private Long ttl;
    /**
     * 存活标记，方便惰性删除
     */
    private Boolean ttlMark;
    /**
     * 默认过期时间
     */
//    private static final Long DEFAULT_TTL = 1_800_000L;
    private static final Long DEFAULT_TTL = 15_000L;
    private Map<String, Object> attributes;

    public void setCreatTime(Long creatTime) {
        this.creatTime = creatTime;
    }

    public MyHttpSessionImpl(){
        try {
            this.creatTime = System.currentTimeMillis();
            this.ttl = DEFAULT_TTL;
            this.ttlMark = false;
            this.httpSessionListener = (HttpSessionListener) ListenerFactoryImpl.getListener(this);
            this.httpSessionAttributeListener = (HttpSessionAttributeListener) ListenerFactoryImpl.getAttributeListener(this);
            this.event = new HttpSessionEvent(this);
            SecureRandom random = null;
            random = SecureRandom.getInstance("SHA1PRNG");
            this.id = random.nextInt();
            this.attributes = new HashMap<>();

            //Session初始化监听
            httpSessionListener.initSession(event);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpSessionListener getHttpSessionListener() {
        return httpSessionListener;
    }

    public HttpSessionAttributeListener getHttpSessionAttributeListener() {
        return httpSessionAttributeListener;
    }

    public HttpSessionEvent getEvent() {
        return event;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setAttribute(String key, Object val) {
        this.attributes.put(key, val);

    }

    @Override
    public Object getAttribute(String key) {
        return this.attributes.get(key);
    }

    @Override
    public Boolean removeAttribute(String key) {
        Object remove = attributes.remove(key);
        return remove == null ? false : true;
    }

    @Override
    public void invalidate() {
        //Session销毁监听
        HttpSessionListener httpSessionListener = this.getHttpSessionListener();
        httpSessionListener.destroyed(this.getEvent());
        this.setTtlMark(true);
    }

    @Override
    public String toString() {
        return "MyHttpSessionImpl{" +
                "id=" + id +
                ", creatTime=" + creatTime +
                ", ttl=" + ttl +
                ", ttlMark=" + ttlMark +
                '}';
    }

    public long getCreatTime() {
        return this.creatTime;
    }

    public long getTTL() {
        return this.ttl;
    }

    public boolean isClear() {
        return this.ttlMark;
    }

    public void setTtlMark(Boolean ttlMark) {
        this.ttlMark = ttlMark;
    }
}
