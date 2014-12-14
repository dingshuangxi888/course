package com.dean.course2.routeandloadbalance;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.ArrayList;
import java.util.List;

/**
 * ����������
 * Created by dean on 14/12/13.
 */
public class ServiceConsumer {

    private List<String> serverList = new ArrayList<>();

    private String serviceName = "service";

    public void init() {
        String zkServiceAddress = "127.0.0.1:2181";
        String SERVICE_PATH = "/configcenter/" + serviceName;
        ZkClient zkClient = new ZkClient(zkServiceAddress);

        boolean serviceExists = zkClient.exists(SERVICE_PATH);
        if (serviceExists) {
            serverList = zkClient.getChildren(SERVICE_PATH);
        } else {
            throw new RuntimeException("service is not exists");
        }

        zkClient.subscribeChildChanges(SERVICE_PATH, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                serverList = currentChilds;
                System.out.println(currentChilds);
            }
        });
    }

    //���ѷ���
    public void consume() {
        //ͨ�����ؾ����㷨���ҵ�һ̨��Ӧ�ķ���������

    }

    public static void main(String[] args) throws Exception {
        ServiceConsumer serviceConsumer = new ServiceConsumer();
        serviceConsumer.init();

        Thread.sleep(1000 * 60 * 60 * 24);
    }

}
