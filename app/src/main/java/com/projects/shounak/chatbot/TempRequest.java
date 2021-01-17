package com.projects.shounak.chatbotv3;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

class TempRequest implements Serializable {

    @SerializedName("msg")
    private String msg;
    @SerializedName("session_id")
    private String session_id;
    @SerializedName("lang_code")
    private String lang_code;
    @SerializedName("mobile_num")
    private String mobile_num;
    @SerializedName("user_code")
    private String user_code;



    public TempRequest(String msg, String lang, String session, String num, String code) {
        this.msg = msg;
        this.lang_code = lang;
        this.session_id = session;
        this.mobile_num = num;
        this.user_code = code;

    }

    public String getMsg() {
        return msg;
    }

}
