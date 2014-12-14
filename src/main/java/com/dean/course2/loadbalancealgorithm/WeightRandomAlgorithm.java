package com.dean.course2.loadbalancealgorithm;

import java.math.BigInteger;
import java.util.*;

/**
 * 加权随机算法
 * Created by dean on 14/12/14.
 */
public class WeightRandomAlgorithm implements LoadBalanceAlgorithm {
    private List<Server> serverList = new ArrayList<>();

    private int totalServer;
    private int currentWeight;
    private int currentIndex;
    private int maxWeight;
    private int gcdWeight;

    private Random random = new Random();

    public void init() {
        Server server1 = new Server("192.168.0.001", 1);
        serverList.add(server1);

        Server server2 = new Server("192.168.0.002", 2);
        serverList.add(server2);

        Server server3 = new Server("192.168.0.003", 3);
        serverList.add(server3);

        Server server4 = new Server("192.168.0.004", 3);
        serverList.add(server4);

        Server server5 = new Server("192.168.0.005", 2);
        serverList.add(server5);

        totalServer = serverList.size();
        currentIndex = totalServer - 1;
        maxWeight = getMaxWeight();
        gcdWeight = getServerGcd();
    }

    /**
     * 获取最大权重
     *
     * @return
     */
    private int getMaxWeight() {
        int max = serverList.get(0).getWeight();
        int tmp;
        for (int i = 1; i < totalServer; i++) {
            tmp = serverList.get(i).getWeight();
            if (max < tmp) {
                max = tmp;
            }
        }
        return max;
    }

    /**
     * 获取下个服务器
     *
     * @return
     */
    @Override
    public Server nextServer() {
        while (true) {
            currentIndex = random.nextInt(totalServer);
            if (currentIndex == 0) {
                currentWeight = currentWeight - gcdWeight;
                if (currentWeight <= 0) {
                    currentWeight = maxWeight;
                    if (currentWeight == 0) {
                        return null;
                    }
                }
            }

            if (serverList.get(currentIndex).getWeight() >= currentWeight) {
                return serverList.get(currentIndex);
            }
        }
    }

    /**
     * 获取权重最大公约数
     *
     * @return
     */
    public int getServerGcd() {
        int comDivisor = 0;
        for (int i = 0; i < totalServer - 1; i++) {
            if (comDivisor == 0) {
                comDivisor = gcd(serverList.get(i).getWeight(), serverList.get(i + 1).getWeight());
            } else {
                comDivisor = gcd(comDivisor, serverList.get(i + 1).getWeight());
                comDivisor = gcd(comDivisor, serverList.get(i + 1).getWeight());
            }
        }
        return comDivisor;
    }

    /**
     * 获取公约数
     *
     * @param num1
     * @param num2
     * @return
     */
    private int gcd(int num1, int num2) {
        BigInteger i1 = new BigInteger(String.valueOf(num1));
        BigInteger i2 = new BigInteger(String.valueOf(num2));
        return i1.gcd(i2).intValue();
    }

    public static void main(String[] args) {
        WeightRandomAlgorithm weightRandom = new WeightRandomAlgorithm();
        weightRandom.init();

        Map<String, Integer> map = new HashMap<>();

        for (int i = 0; i < 1000; i ++) {
            Server server = weightRandom.nextServer();
            String key = "addr:" + server.getAddress() + "; weight:" + server.getWeight();
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
