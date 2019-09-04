package com.dataseek.xe.util;

public class ResponseDto extends BaseVo {
    private String user_id;

    private String user_session_id;

    public String getUser_session_id() {
        return user_session_id;
    }

    public void setUser_session_id(String user_session_id) {
        this.user_session_id = user_session_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
