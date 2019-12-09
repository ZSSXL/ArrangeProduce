package cn.edu.jxust.arrangeproduce.common;

import cn.edu.jxust.arrangeproduce.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author ZSS
 * @date 2019/11/14 9:54
 * @description redis 连接池
 */
public class RedisPool {

    /**
     * jedis连接池
     */
    private static JedisPool pool;

    /**
     * 最大连接数
     */
    private static Integer maxTotal = Integer.valueOf(PropertiesUtil.getProperty("redis.max.total", "20"));

    /**
     * 在Jedis pool中最大的idle（空闲）状态的jedis实例个数
     */
    private static Integer maxIdle = Integer.valueOf(PropertiesUtil.getProperty("redis.max.idle", "10"));

    /**
     * 在Jedis pool中最大的idle（空闲）状态的jedis实例个数
     */
    private static Integer minIdle = Integer.valueOf(PropertiesUtil.getProperty("redis.min.idle", "2"));

    /**
     * 在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true，则得到的jedis实例肯定是可以用的
     */
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));

    /**
     * 在return一个jedis实例的时候，是否要进行验证操作，如果赋值true，则放回的jedis实例肯定是可以用的
     */
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "true"));

    /**
     * redis的端口
     */
    private static Integer redisPort = Integer.valueOf(PropertiesUtil.getProperty("redis.port", "8379"));

    /**
     * redis的ip地址
     */
    private static String redisIp = PropertiesUtil.getProperty("redis.ip");

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        // 连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时，默认为true
        config.setBlockWhenExhausted(true);

        pool = new JedisPool(config, redisIp, redisPort, 1000 * 2);
    }

    static {
        initPool();
    }

    /**
     * 从连接池中获取jedis
     *
     * @return Jedis
     */
    public static Jedis getJedis() {
        return pool.getResource();
    }

    /**
     * 将获取的jedis返回给jedis连接池
     *
     * @param jedis jedis
     */
    public static void returnResource(Jedis jedis) {
        jedis.close();
    }

    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("name", "zss");
        returnResource(jedis);

        // 临时调用，销毁连接池中的所有连接
        pool.destroy();
        System.out.println("program is end ... ");
    }

}
