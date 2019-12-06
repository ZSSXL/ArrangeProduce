package cn.edu.jxust.arrangeproduce.websocket;

import cn.edu.jxust.arrangeproduce.entity.po.User;
import cn.edu.jxust.arrangeproduce.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author ZSS
 * @date 2019/12/6 15:45
 * @description 通知员工有新的任务
 */
@Slf4j
@Component
@ServerEndpoint("/notice/{userId}")
public class Notice extends TextWebSocketHandler {

    public static UserService userService;

    /**
     * 静态变量，用来记录当前在线连接数，应该把它设计成线程安全的
     */
    private static int onlineCount = 0;

    /**
     * 线程安全Set, 用来存放每个客户端对应的webSocket对象
     */
    private static CopyOnWriteArraySet<Notice> noticeSet = new CopyOnWriteArraySet<>();

    /**
     * 与某个客户端连接绘画，需要它来给客户端发送数据
     */
    private Session session;

    /**
     * 当前发送消息的人员企业Id
     */
    private String enterpriseId = "";

    /**
     * 建立连接
     * @param userId 用户Id
     * @param session session
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) {
        if (StringUtils.isEmpty(userId)) {
            log.error("参数错误");
            try {
                session.getBasicRemote().sendText("参数错误");
            } catch (IOException e) {
                log.error("发送消息异常 : {}", e.getMessage());
            }
        } else {
            User user = userService.getUserById(userId);
            if (user != null) {
                // 将当前用户的企业Id保存
                this.enterpriseId = user.getEnterpriseId();
                this.session = session;
                noticeSet.add(this);
                addOnlineCount();
                log.info("New connection, currently online : {}", onlineCount);
            } else {
                log.warn("query user failed, the userId is : " + userId);
            }

        }
    }

    /**
     * 断开连接
     */
    @OnClose
    public void onClose() {
        if (!StringUtils.isEmpty(this.enterpriseId)) {
            noticeSet.remove(this);
            subOnlineCount();
            log.info("someone offline, currently online : {}", onlineCount);
        }
    }

    /**
     * 发送消息
     *
     * @param message 消息
     * @param session session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("get message : {} by session : {}", message, session.getId());
        sendAll();
    }

    /**
     * 将提示消息发送给所有人，本企业的人只能发送给自己企业的人
     */
    public void sendAll() {
        // 发送数据给所有用户
        // 遍历noticeSet
        for (Notice notice : noticeSet) {
            if (StringUtils.equals(this.enterpriseId, notice.enterpriseId)) {
                if (!StringUtils.equals(this.session.getId(), notice.session.getId())) {
                    try {
                        notice.session.getBasicRemote().sendText("How are you ?");
                    } catch (IOException e) {
                        log.error("send message error : {}", e.getMessage());
                    }
                }
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("session : {} websocket has error : {}", session.getId(), error.getMessage());
    }

    /**
     * 当前在线人数加一
     */
    private static synchronized void addOnlineCount() {
        Notice.onlineCount++;
    }

    /**
     * 当前在线人数减一
     */
    private static synchronized void subOnlineCount() {
        Notice.onlineCount--;
    }

    // 重写hashCode和equals方法

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
