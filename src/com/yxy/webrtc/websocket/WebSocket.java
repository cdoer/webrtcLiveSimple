package com.yxy.webrtc.websocket;

/**
 * Created by yang on 2018-08-03.
 * email 1249492252@qq.com
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yxy.webrtc.bean.ChatMessage;
import com.yxy.webrtc.bean.ChatRoom;
import com.yxy.webrtc.model.User;
import com.yxy.webrtc.utils.ChatUtils;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/websocket", configurator = GetHttpSessionConfigurator.class)
public class WebSocket {

    private static int onlineCount = 0;
    private static Map<User, WebSocket> clients = new ConcurrentHashMap<User, WebSocket>();
    private static Map<String, WebSocket> userIdclientsMap = new ConcurrentHashMap<String, WebSocket>();
    private Session session;
    private HttpSession httpSession;
    private User user;
    private Timer timer = new Timer();

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws IOException {
        this.session = session;
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        User user = (User) httpSession.getAttribute("user");
        this.user = user;
        clients.put(user, this);
        userIdclientsMap.put(user.getId(), this);
        System.out.println("已连接");
        if(user.getTask()!=null){
            user.getTask().cancel();
        }
        ChatRoom userRoom = user.getRoom();
        if (userRoom != null) {
            ChatMessage chatMessage = new ChatMessage(ChatUtils.ONLINE);
            chatMessage.setFrom(user);
            sendMessageTo(chatMessage, userRoom.getCreator().getId());
        }
    }

    @OnClose
    public void onClose() throws IOException {
        //发送下线消息 这里直接发送退出会议即可
        if (user.getRoom() != null) {
            user.setTask(new TimerTask() {
                @Override
                public void run() {
                    try {
                        user.getRoom().remove(user);
                    } catch (Exception e) {}
                }
            });
            //防止用户刷新
            timer.schedule(user.getTask(), 1000);
        }
        clients.remove(user);
        userIdclientsMap.remove(user.getId());
    }

    @OnMessage
    public void onMessage(String message) throws IOException {
        ChatMessage chatMessage = JSONObject.parseObject(message, ChatMessage.class);
        chatMessage.setFrom(user);
        String type = chatMessage.getType();
        ChatMessage alert = new ChatMessage(ChatUtils.ALERT);
        if (ChatUtils.chatTypes.contains(type)) {
            if (ChatUtils.ROOM_CHAT.equals(type)) {//文字消息
                //是否加入房间
                ChatRoom room = user.getRoom();
                sendRoom(room, chatMessage);
            } else {
                sendMessageTo(chatMessage, chatMessage.getTo());
            }
        } else {
            alert.setMsg("未知的消息类型");
            sendMessageTo(alert, user.getId());
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public static void sendMessageTo(ChatMessage chatMessage, String To) throws IOException {
        String message = JSON.toJSONString(chatMessage);
        WebSocket webSocket = userIdclientsMap.get(To);

        if (webSocket != null) {
            try {
                Session session = webSocket.session;
                synchronized (session) {
                    session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(e);
                System.out.println("信息发送错误" + message);
            }
        }
    }

    public static void sendRoom(ChatRoom room, ChatMessage chatMessage,boolean containsThis) throws IOException {
        if (room != null && chatMessage != null) {
            User from = null;
            if (chatMessage.getFrom() != null) {
                from = userIdclientsMap.get(chatMessage.getFrom()) != null ? userIdclientsMap.get(chatMessage.getFrom()).user : null;
            }
            List<User> userList = room.getUserList();
            for (User u : userList) {
                if (clients.get(u) != null && (containsThis||(!containsThis&&u != from))) {
                    chatMessage.setTo(u.getId());
                    sendMessageTo(chatMessage, u.getId());
                }
            }
        }
    }

    public static void sendRoom(ChatRoom room, ChatMessage chatMessage) throws IOException {
        sendRoom(room,chatMessage,false);
    }


    public static void sendMessageAll(ChatMessage chatMessage) throws IOException {
        String message = JSON.toJSONString(chatMessage);
        for (WebSocket item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }


    public static synchronized Map<User, WebSocket> getClients() {
        return clients;
    }
}



