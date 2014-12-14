package com.dean.course1.protocol;

/**
 * 请求类封装
 * Created by dean on 14/12/7.
 */
public class Request {

    private String commond;

    private int commondLength;

    private byte encode;

    public String getCommond() {
        return commond;
    }

    public void setCommond(String commond) {
        this.commond = commond;
    }

    public int getCommondLength() {
        return commondLength;
    }

    public void setCommondLength(int commondLength) {
        this.commondLength = commondLength;
    }

    public byte getEncode() {
        return encode;
    }

    public void setEncode(byte encode) {
        this.encode = encode;
    }
}
