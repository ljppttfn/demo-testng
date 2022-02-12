package lj.study.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 哨兵模式下的redis集群
 */
public class RedisSentinelPoolUtils {

    private JedisSentinelPool pool;

    public RedisSentinelPoolUtils(String address, String masterName){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);

        Set<String> sentinels = new HashSet<>();
        String[] nodes = address.split(",");
        Collections.addAll(sentinels, nodes);

        pool = new JedisSentinelPool(masterName, sentinels, poolConfig);
    }

    public Jedis getResource(){
        return pool.getResource();
    }

    public void close(){
        pool.close();
    }

//    public static void main(String[] args) {
//        RedisSentinelPoolUtils redis_node2 = new RedisSentinelPoolUtils("172.16.48.170:26379,172.16.48.172:26379,172.16.48.173:26379","mymaster");
//        Jedis jedis = redis_node2.getResource();
//        Map<String, String> res2 = jedis.hgetAll("qatest-n2:register:wareInfo");
//        System.out.println(res2.size());
//        System.out.println(res2);
//        jedis.close();
//    }
}
