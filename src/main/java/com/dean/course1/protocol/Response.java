package com.dean.course1.protocol;

/**
 * 响应类封装
 * Created by dean on 14/12/7.
 */
public class Response {

    private int responseLength;

    private String response;

    private byte encode;

    public int getResponseLength() {
        return responseLength;
    }

    public void setResponseLength(int responseLength) {
        this.responseLength = responseLength;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public byte getEncode() {
        return encode;
    }

    public void setEncode(byte encode) {
        this.encode = encode;
    }
}
