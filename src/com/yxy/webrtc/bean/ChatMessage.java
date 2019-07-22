package com.yxy.webrtc.bean;

import com.alibaba.fastjson.JSONObject;
import com.yxy.webrtc.model.User;

/**
 * Created by yang on 2019-05-01.
 */
public class ChatMessage {
    private String type;
    private String msg;
    private String from;
    private String to;
    private String data;
    private String fromName;
    private long time = System.currentTimeMillis();
    private JSONObject offer;
    private JSONObject answer;
    private JSONObject candidate;

    public ChatMessage() {
    }

    public ChatMessage(String type) {
        this.type = type;
    }

    public ChatMessage(String type, String msg, String from, String to, String data) {
        this.type = type;
        this.msg = msg;
        this.from = from;
        this.to = to;
        this.data = data;
    }

    public ChatMessage(String type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public void setFrom(User user){
        if(user!=null){
            this.from = user.getId();
            this.fromName = user.getName();
            if(user.getRoom()!=null&&user.getRoom().getCreator()==user){
                this.fromName = user.getName()+"<span style='color:red;'>[主持人]</span>";
            }
        }
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public JSONObject getOffer() {
        return offer;
    }

    public void setOffer(JSONObject offer) {
        this.offer = offer;
    }

    public JSONObject getAnswer() {
        return answer;
    }

    public void setAnswer(JSONObject answer) {
        this.answer = answer;
    }

    public JSONObject getCandidate() {
        return candidate;
    }

    public void setCandidate(JSONObject candidate) {
        this.candidate = candidate;
    }
}
