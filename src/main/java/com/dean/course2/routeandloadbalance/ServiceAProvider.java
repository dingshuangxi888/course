package com.dean.course2.routeandloadbalance;

import org.I0Itec.zkclient.ZkClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 服务提供者A
 * Created by dean on 14/12/13.
 */
public class ServiceAProvider {

    private String serviceName = "service";

    public void init() throws UnknownHostException {

        String serverList = "127.0.0.1:2181";
        String PATH = "/configcenter";
        ZkClient zkClient = new ZkClient(serverList);

        boolean rootExists = zkClient.exists(PATH);

        if (!rootExists) {
            zkClient.createPersistent(PATH);
        }

        boolean serviceExists = zkClient.exists(PATH + "/" + serviceName);
        if (!serviceExists) {
            zkClient.createPersistent(PATH + "/" + serviceName);
        }

//        InetAddress addr = InetAddress.getLocalHost();
//        String ip = addr.getHostAddress().toString();
//        System.out.println(ip);

        String ip = "192.168.0.1";

        zkClient.createEphemeral(PATH + "/" + serviceName + "/" + ip);
    }

    //提供服务
    public void provide() {

    }

    public static void main(String[] args) throws Exception {

        ServiceAProvider serviceAProvider = new ServiceAProvider();
        serviceAProvider.init();

        Thread.sleep(1000 * 60 * 60 * 24);

    }
}
