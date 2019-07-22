package com.yxy.webrtc.model;

import com.yxy.webrtc.bean.ChatRoom;

import java.io.Serializable;
import java.util.TimerTask;

/**
 * Created by yang on 2019-05-01.
 * 1249492252@qq.com
 */
public class User implements Serializable {
    private String id;
    private String name;
    private String userName;
    private String pwd;
    private ChatRoom room;
    private TimerTask task;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public ChatRoom getRoom() {
        return room;
    }

    public void setRoom(ChatRoom room) {
        this.room = room;
    }

    public TimerTask getTask() {
        return task;
    }

    public void setTask(TimerTask task) {
        this.task = task;
    }
}
