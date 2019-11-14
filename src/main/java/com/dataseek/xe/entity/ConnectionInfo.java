package com.dataseek.xe.entity;

public class ConnectionInfo {
    private String connection_id;
    private String etsy_shopname;
    private String xero_orgname;
    private String status;
    private String last_connected;

    public String getConnection_id() {
        return connection_id;
    }

    public void setConnection_id(String connection_id) {
        this.connection_id = connection_id;
    }

    public String getEtsy_shopname() {
        return etsy_shopname;
    }

    public void setEtsy_shopname(String etsy_shopname) {
        this.etsy_shopname = etsy_shopname;
    }

    public String getXero_orgname() {
        return xero_orgname;
    }

    public void setXero_orgname(String xero_orgname) {
        this.xero_orgname = xero_orgname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLast_connected() {
        return last_connected;
    }

    public void setLast_connected(String last_connected) {
        this.last_connected = last_connected;
    }
}
