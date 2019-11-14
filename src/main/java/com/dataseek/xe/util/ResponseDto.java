package com.dataseek.xe.util;

import com.dataseek.xe.entity.AccountInfo;
import com.dataseek.xe.entity.ConnectionInfo;

import java.util.List;

public class ResponseDto extends BaseVo {
    private String user_id;

    private String user_session_id;

    private boolean verified;

    private List<AccountInfo> accounts;

    private String connection_id;

    private List<ConnectionInfo> connections;

    public List<ConnectionInfo> getConnections() {
        return connections;
    }

    public void setConnections(List<ConnectionInfo> connections) {
        this.connections = connections;
    }

    public String getConnection_id() {
        return connection_id;
    }

    public void setConnection_id(String connection_id) {
        this.connection_id = connection_id;
    }

    public List<AccountInfo> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountInfo> accounts) {
        this.accounts = accounts;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

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
