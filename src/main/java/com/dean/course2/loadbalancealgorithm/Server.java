package com.dean.course2.loadbalancealgorithm;

/**
 * 服务器信息
 * Created by dean on 14/12/14.
 */
public class Server {

    /**
     * 地址
     */
    private String address;

    /**
     * 权重
     */
    private int weight;

    public Server() {
    }

    public Server(String address) {
        this.address = address;
    }

    public Server(String address, int weight) {
        this.address = address;
        this.weight = weight;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
