package lj.study.utils;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

public class RedisClusterUtils {
    private JedisCluster jedisCluster;

    /**
     * addr
     * @param addr 集群地址字符串，为ip1:port1,ip2:port2,ip3:port3 此类格式
     */
    public RedisClusterUtils(String addr){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);

        Set<HostAndPort> set = new HashSet<>();
        String[] nodes = addr.split(",");
        for (String node : nodes) {
            HostAndPort hostAndPort = HostAndPort.parseString(node);
            set.add(hostAndPort);
        }
        jedisCluster = new JedisCluster(set, 5000, poolConfig);
    }

    public JedisCluster getResource(){
        return jedisCluster;
    }

    public void close(){
        jedisCluster.close();
    }
}
