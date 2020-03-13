package com.zs.create.config.Websocket;


import com.google.common.collect.Maps;
import com.zs.create.base.util.JwtUtil;
import com.zs.create.common.constant.CommonConstant;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.util.RedisUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/ws/asset/{userid}")
@Component
public class WebSocketServer {

    @PostConstruct
    public void init() {
        System.out.println("websocket 加载");
    }

    //    private static Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    private static final AtomicInteger OnlineCount = new AtomicInteger(0);
    // concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
    private static CopyOnWriteArraySet<Map> SessionSet = new CopyOnWriteArraySet<Map>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("userid") String userid,Session session) {
        System.out.println("userid："+userid);
        SessionSet.add(new HashMap() {{
            put(userid, session);
        }});
        System.out.println("当前连接人数：" + SessionSet);
        int cnt = OnlineCount.incrementAndGet(); // 在线数加1
        //log.info("有连接加入，当前连接数为：{}", cnt);
        //SendMessage(session, "连接成功");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("userid") String userid,Session session) {
        System.out.println("关闭userid："+userid);
        SessionSet.remove(new HashMap() {{
            put(userid, session);
        }});
//        Collection<Map> collect = SessionSet.;
//        collect.remove(session);
        System.out.println("关闭调用的方法：" + session);
        int cnt = OnlineCount.decrementAndGet();
        //log.info("有连接关闭，当前连接数为：{}", cnt);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        //log.info("来自客户端的消息：{}",message);
        SendMessage(session, "收到消息，消息内容：" + message);

    }

    /**
     * 出现错误
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        //log.error("发生错误：{}，Session ID： {}",error.getMessage(),session.getId());
        error.printStackTrace();
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     *
     * @param session
     * @param message
     */
    public static void SendMessage(Session session, String message) {
        try {
            //session.getBasicRemote().sendText(String.format("%s (From Server，Session ID=%s)", message, session.getId()));
            session.getBasicRemote().sendText(String.format(message));
        } catch (IOException e) {
            //log.error("发送消息出错：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 群发消息
     * @param message
     * @throws IOException
     */
//    public static void BroadCastInfo(String message) throws IOException {
//        for (Session session : SessionSet) {
//            if(session.isOpen()){
//                SendMessage(session, message);
//            }
//        }
//    }

    /**
     * 指定Session发送消息
     *
     * @param sessionId
     * @param message
     * @throws IOException
     */
    public static void SendMessage(String message, String sessionId) throws IOException {
        Session session = null;
        System.out.println("SessionSet" + SessionSet);
        Map sMap = SessionSet.stream().filter(item -> {
            Map itMap = item;
            String next = (String) itMap.keySet().iterator().next();
            return next.equals(sessionId);
        }).findFirst().orElse(Maps.newHashMap());

        if (!MapUtils.isEmpty(sMap)) {
            session = (Session) sMap.values().iterator().next();
        }

        if (sessionId != null) {
            SendMessage(session, message);
        } else {
            //log.warn("没有找到你指定ID的会话：{}",sessionId);
        }
    }

}
