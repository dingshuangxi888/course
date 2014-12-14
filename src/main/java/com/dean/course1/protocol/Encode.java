package com.dean.course1.protocol;

/**
 * 编码枚举
 * Created by dean on 14/12/7.
 */
public enum Encode {

    GBK((byte) 0), UTF8((byte) 1);

    private byte value;

    Encode(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
