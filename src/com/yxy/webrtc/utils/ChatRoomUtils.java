package com.yxy.webrtc.utils;

import com.yxy.webrtc.bean.ChatRoom;
import com.yxy.webrtc.model.User;

import java.util.*;

/**
 * Created by yang on 2019-05-01.
 * 1249492252@qq.com
 */
public class ChatRoomUtils {

    public static Map<String,ChatRoom> chatRoomMap = new HashMap<String,ChatRoom>();
    public static Set<String> chatRooms = new HashSet<String>();

    public static String createRoomName(){
        String chars = "0123456789";
        Random random = new Random();
        int i = random.nextInt(9)+1;
        String roomName = i+"";
        for (int j = 0; j < 9; j++) {
            roomName+=chars.charAt(random.nextInt(chars.length()));
        }
        return roomName;
    }

    public static ChatRoom createChatRoom(User user, String title, String pwd) throws Exception{
        ChatRoom chatRoom = new ChatRoom();
        String roomName = createRoomName();
        while(chatRooms.contains(roomName)){
            roomName = createRoomName();
        }
        user.setRoom(chatRoom);
        chatRoom.setCreateDate(System.currentTimeMillis());
        chatRoom.setCreator(user);
        chatRoom.setRoomName(roomName);
        chatRoom.setPwd(pwd);
        chatRoom.setTitle(title);
        chatRooms.add(roomName);
        chatRoomMap.put(roomName,chatRoom);
        chatRoom.getUserList().add(user);
        user.setRoom(chatRoom);
        return chatRoom;
    }

}
