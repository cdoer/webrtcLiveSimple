package com.yxy.webrtc.bean;

/**
 * Created by yang on 2019-05-01.
 */
public class Result {
    private boolean success;
    private Object data;

    public Result() {
        this.success = true;
    }

    public Result(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
