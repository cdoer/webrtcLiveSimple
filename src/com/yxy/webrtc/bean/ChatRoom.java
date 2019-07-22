package com.yxy.webrtc.bean;

import com.yxy.base.exception.BizException;
import com.yxy.base.utils.CommonUtils;
import com.yxy.webrtc.model.User;
import com.yxy.webrtc.utils.ChatRoomUtils;
import com.yxy.webrtc.utils.ChatUtils;
import com.yxy.webrtc.websocket.WebSocket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2019-05-01.
 */
public class ChatRoom {
    private String title;
    private String roomName;
    private String pwd;
    private User creator;
    private long createDate;
    private List<User> userList = new ArrayList<User>();
    private Integer limit = 10;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void join(User user,String pwd,boolean valid) throws Exception{
        if(user!=null){
            if(valid&&!CommonUtils.isEmpty(this.pwd)&&!this.pwd.equals(pwd)){
                throw new BizException("密码不正确");
            }
            if(!userList.contains(user)){
                if(userList.size()+1>limit) throw new BizException("抱歉，该房间人数已满");
                user.setRoom(this);
                userList.add(user);
                ChatMessage chatMessage = new ChatMessage(ChatUtils.JOIN);
                chatMessage.setFrom(user);
                chatMessage.setType(ChatUtils.JOIN);
                WebSocket.sendRoom(this,chatMessage);
                System.out.println(user.getName()+"加入会议["+this.getTitle()+"]");
            }
        }
    }

    public void remove(User user) throws Exception{
        if(user!=null&&userList!=null&&userList.contains(user)){
            if(user==this.creator){//创建者退出 则终止会议
                ChatMessage chatMessage = new ChatMessage(ChatUtils.END_CHAT);
                chatMessage.setFrom(user);
                WebSocket.sendRoom(this,chatMessage,true);
                System.out.println(user.getName()+"终止会议["+this.getTitle()+"]");
                this.userList.clear();
                ChatRoomUtils.chatRoomMap.remove(this.getRoomName());
                ChatRoomUtils.chatRooms.remove(this.getRoomName());
            }else{
                //发送退出会议消息
                ChatMessage chatMessage = new ChatMessage(ChatUtils.EXIT);
                chatMessage.setFrom(user);
                WebSocket.sendRoom(this,chatMessage,true);
                System.out.println(user.getName()+"退出会议["+this.getTitle()+"]");
            }
            userList.remove(user);
        }
        user.setRoom(null);
    }


}
