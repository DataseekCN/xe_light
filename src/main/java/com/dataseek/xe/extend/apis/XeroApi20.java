package com.dataseek.xe.extend.apis;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignature;
import com.github.scribejava.core.oauth2.bearersignature.BearerSignatureURIQueryParameter;

public class XeroApi20 extends DefaultApi20 {
    protected XeroApi20(){

    }

    private static class InstanceHolder {
        private static final XeroApi20 INSTANCE = new XeroApi20();
    }

    public static XeroApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://identity.xero.com/connect/token";
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://login.xero.com/identity/connect/authorize";
    }

    @Override
    public BearerSignature getBearerSignature() {
        return BearerSignatureURIQueryParameter.instance();
    }
}
