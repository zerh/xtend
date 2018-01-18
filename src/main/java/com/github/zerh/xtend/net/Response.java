package com.github.zerh.xtend.net;

import java.util.List;
import java.util.Map;

/**
 * Created by eliezer on 11/20/17.
 */

public class Response {

    private String body;
    private byte[] rawBody;
    private Map<String, List<String>> headers;
    private int status;

    Response() {}

    public byte[] rawBody() {
        return rawBody;
    }

    void rawBody(byte[] rawBody) {
        this.rawBody = rawBody;
    }

    public String body() {
        return new String(rawBody);
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public boolean isEmpty(){
        if(headers==null){ return true; }
        return headers.isEmpty();
    }
}
