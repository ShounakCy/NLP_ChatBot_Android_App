package com.projects.shounak.chatbotv3;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

class TempResponse implements Serializable {

    @SerializedName("msg")
    private String msg;
    @SerializedName("session_id")
    private String session_id;


    public String getMsg() {
        return msg;
    }
    public String getSession_id() {
        return session_id;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
}
