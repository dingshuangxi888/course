package com.dean.course2.node;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * ZooKeeper节点添加和删除
 * Created by dean on 14/12/13.
 */
public class NodeOperator {
    public static void main(String[] args) {
        try {
            ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 60000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    System.out.println("isChanged");
                }
            });
            zooKeeper.create("/root", "data_root".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

            List<String> root = zooKeeper.getChildren("/", true);
            System.out.println(root);

            zooKeeper.create("/root/child1", "data_child1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

            zooKeeper.create("/root/child2", "data_child2".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

            zooKeeper.create("/root/child3", "data_child3".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

            List<String> children = zooKeeper.getChildren("/", true);
            System.out.println(children);

            zooKeeper.delete("/root/child3", -1);

            List<String> children1 = zooKeeper.getChildren("/", true);
            System.out.println(children1);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
}
