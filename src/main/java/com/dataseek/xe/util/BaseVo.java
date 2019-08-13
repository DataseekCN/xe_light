package com.dataseek.xe.util;

public class BaseVo {
    //请求响应业务状态-默认为success
    private String status = XeConsts.RESPONSE_STATUS_SUCCESS;
    //请求响应错误消息
    private String error_message="";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
}
