package lj.study.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 封装JedisPool工具类
 * 特别注意：静态变量只能是jedisPool, 初始化时是获取连接池，不能初始化时获取jedis。
 * 使用时只能通过 调用 jedisPool.getResource() 的方式从pool中获取一个连接。
 * 否则会报错：redis.clients.jedis.exceptions.JedisConnectionException: Unexpected end of stream
 */
public class RedisPoolUtils {

    private JedisPool jedisPool;

    public RedisPoolUtils(String ip, int port){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(10);

        this.jedisPool = new JedisPool(config, ip, port, 5000);
    }

    public RedisPoolUtils(String ip, int port, String password){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(10);
        this.jedisPool = new JedisPool(config, ip, port, 5000, password);
    }

    public Jedis getJedis() {
        return this.jedisPool.getResource();
    }

    public void close(){
        this.jedisPool.destroy();
    }

//    public static void main(String[] args) {
//        // Step1：初始化连接池
//        RedisPoolUtils redisPoolUtils = new RedisPoolUtils("172.16.72.62", 6379);
//
//        // Step2：从连接池中获取一个jedis连接
//        Jedis jedis_node1 = redisPoolUtils.getJedis();
//
//        // Step3：执行操作
//        Set<String> keys_1 = jedis_node1.keys("*");
//        System.out.println(keys_1.size());
//
//        // Step4：操作执行完毕后，释放连接资源
//        jedis_node1.close(); // 注意：每个jedis使用完后一定要close
//
//        // Step5：可以在所有自动化结束后销毁整个pool
//        redisPoolUtils.close();
//
//    }
}
