package cn.edu.jxust.arrangeproduce.websocket;

import cn.edu.jxust.arrangeproduce.entity.po.User;
import cn.edu.jxust.arrangeproduce.service.UserService;
import cn.edu.jxust.arrangeproduce.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
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
public class Notice {

    private final UserService userService;

    @Autowired
    public Notice(UserService userService) {
        this.userService = userService;
    }

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
            // 将当前用户的企业Id保存
            this.enterpriseId = user.getEnterpriseId();
            noticeSet.add(this);
            addOnlineCount();
            log.info("New connection, current number of people online : {}", onlineCount);
        }
    }

    @OnClose
    public void onClose() {
        noticeSet.remove(this);
        subOnlineCount();
        log.info("someone offline");
    }

    @OnMessage
    public void onMessage(Session session) {

    }

    private void sendMessageToAll() {
        // 发送数据给所有用户
        // 遍历noticeSet
        for (Notice notice : noticeSet) {
            System.out.println(notice.enterpriseId);
            if (StringUtils.equals(this.enterpriseId, notice.enterpriseId)) {
                try {
                    notice.session.getBasicRemote().sendText("test");
                } catch (IOException e) {
                    log.error("{} : 发送消息给 ：{} 所有人发生异常", DateUtil.getDateComplete(), notice.enterpriseId);
                }
            }
        }
    }

    /**
     * 获取当前的在线人数
     *
     * @return int
     */
    private static synchronized int getOnlineCount() {
        return onlineCount;
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

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
