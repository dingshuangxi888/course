package com.dean.course2.loadbalancealgorithm;

import java.util.*;

/**
 * 随机算法
 * Created by dean on 14/12/14.
 */
public class RandomAlgorithm implements LoadBalanceAlgorithm {
    private List<Server> serverList = new ArrayList<>();

    private int totalServer;
    private int currentIndex;

    private Random random = new Random();



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
        currentIndex = random.nextInt(totalServer);
        return serverList.get(currentIndex);
    }

    public static void main(String[] args) {
        RandomAlgorithm randomAlgorithm = new RandomAlgorithm();
        randomAlgorithm.init();

        Map<String, Integer> map = new HashMap<>();

        for (int i = 0; i < 1000; i ++) {
            Server server = randomAlgorithm.nextServer();
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
