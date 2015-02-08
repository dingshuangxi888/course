package com.dean.course6.redis;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by dean on 15/1/11.
 */
public class RedisTest {

    private JedisPool jedisPool;
    private Jedis jedis;

    @Before
    public void setUp() {
        jedisPool = new JedisPool(new JedisPoolConfig(), "127.0.0.1");
        jedis = jedisPool.getResource();
    }

    @Test
    public void testBasicString() {
        //---------添加数据-----------
        jedis.set("name", "Shuangxi");
        System.out.println(jedis.get("name"));

        jedis.append("name", " Ding");
        System.out.println(jedis.get("name"));

        jedis.set("name", "Ding Shuangxi");
        System.out.println(jedis.get("name"));

        jedis.del("name");
        System.out.println(jedis.get("name"));

        jedis.mset("name", "Shuangxi Ding", "city", "Hangzhou");
        System.out.println(jedis.mget("name", "city"));
    }

    /**
     * jedis操作Map
     */
    @Test
    public void testMap() {
        Map<String, String> user = new HashMap<>();
        user.put("name", "Shuangxi Ding");
        user.put("pwd", "password");
        jedis.hmset("user", user);
        List<String> rsmap = jedis.hmget("user", "name");
        System.out.println(rsmap);

        System.out.println(jedis.hmget("user", "pwd"));
        System.out.println(jedis.hlen("user"));
        System.out.println(jedis.exists("user"));
        System.out.println(jedis.hkeys("user"));
        System.out.println(jedis.hvals("user"));

        Iterator<String> iter = jedis.hkeys("user").iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            System.out.println(key + ":" + jedis.hmget("user", key));
        }

    }

    /**
     * jedis操作List
     */
    @Test
    public void testList() {
        jedis.del("java framework");
        System.out.println(jedis.lrange("java framework", 0, -1));
        jedis.lpush("java framework", "spring");
        jedis.lpush("java framework", "struts");
        jedis.lpush("java framework", "hibernate");
        System.out.println(jedis.lrange("java framework", 0, -1));
    }

    /**
     * jedis操作Set
     */
    @Test
    public void testSet() {
        //添加
        jedis.sadd("sname", "zhangsan1");
        jedis.sadd("sname", "zhangsan2");
        jedis.sadd("sname", "zhangsan3");
        jedis.sadd("sanme", "zhangsan4");
        //移除noname
        jedis.srem("sname", "noname");
        System.out.println(jedis.smembers("sname"));
        System.out.println(jedis.sismember("sname", "minxr"));
        System.out.println(jedis.srandmember("sname"));
        System.out.println(jedis.scard("sname"));
    }

    @Test
    public void test() throws InterruptedException {
        //keys中传入的可以用通配符
        System.out.println(jedis.keys("*"));
        System.out.println(jedis.keys("*name"));
        System.out.println(jedis.del("sanmdde"));
        System.out.println(jedis.ttl("sname"));
        jedis.setex("timekey", 10, "min");
        Thread.sleep(5000);
        System.out.println(jedis.ttl("timekey"));
        jedis.setex("timekey", 1, "min");
        System.out.println(jedis.ttl("timekey"));
        System.out.println(jedis.exists("key"));
        System.out.println(jedis.rename("timekey", "time"));
        System.out.println(jedis.get("timekey"));
        System.out.println(jedis.get("time"));

        jedis.del("a");
        jedis.rpush("a", "1");
        jedis.lpush("a", "6");
        jedis.lpush("a", "3");
        jedis.lpush("a", "9");
        System.out.println(jedis.lrange("a", 0, -1));// [9, 3, 6, 1]
        System.out.println(jedis.sort("a")); //[1, 3, 6, 9]  //输入排序后结果
        System.out.println(jedis.lrange("a", 0, -1));

    }
}
