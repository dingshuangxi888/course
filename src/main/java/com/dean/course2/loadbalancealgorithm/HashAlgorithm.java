package com.dean.course2.loadbalancealgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 源地址hash算法
 * Created by dean on 14/12/14.
 */
public class HashAlgorithm implements LoadBalanceAlgorithm {

    private List<Server> serverList = new ArrayList<>();

    private int totalServer;
    private int currentIndex;

    private int ipNum = 0;



    public void init() {
        Server server1 = new Server("192.168.0.001");
        serverList.add(server1);

        Server server2 = new Server("192.168.0.002");
        serverList.add(server2);

        Server server3 = new Server("192.168.0.003");
        serverList.add(server3);

        Server server4 = new Server("192.168.0.004");
        serverList.add(server4);

        Server server5 = new Server("192.168.0.005");
        serverList.add(server5);

        totalServer = serverList.size();
        currentIndex = totalServer - 1;
    }

    /**
     * 获取下一个服务器
     * @return
     */
    @Override
    public Server nextServer() {
        String myIp = "192.168.1.00" + ipNum % 10;
        ipNum ++;
        currentIndex =  hash(myIp.hashCode()) * -1 % totalServer;
        return serverList.get(currentIndex);
    }

    private int hash(int h)
    {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    public static void main(String[] args) {
        HashAlgorithm hashAlgorithm = new HashAlgorithm();
        hashAlgorithm.init();

        Map<String, Integer> map = new HashMap<>();

        for (int i = 0; i < 1000; i ++) {
            Server server = hashAlgorithm.nextServer();
            String key = "addr:" + server.getAddress();
            if (map.containsKey(key)) {
                map.put(key, map.get(key) + 1);
            } else {
                map.put(key, 1);
            }
        }

        for (Map.Entry<String, Integer> entity : map.entrySet()) {
            System.out.println("服务器:" + entity.getKey() + "; 调用次数:" + entity.getValue());
        }
    }
}
