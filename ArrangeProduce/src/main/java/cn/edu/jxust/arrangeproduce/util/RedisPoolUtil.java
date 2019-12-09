package cn.edu.jxust.arrangeproduce.util;

import cn.edu.jxust.arrangeproduce.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * @author ZSS
 * @date 2019/9/11 21:24
 * @description redis 连接池工具
 */
@Slf4j
@Component
public class RedisPoolUtil {

    /**
     * set
     *
     * @param key   键
     * @param value 值
     * @return String
     */
    public static String set(String key, String value) {
        Jedis jedis = null;
        String result;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error", key, value, e);
            assert jedis != null;
            RedisPool.returnResource(jedis);
            return null;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     * 获取值
     *
     * @param key 键
     * @return String
     */
    public static String get(String key) {
        Jedis jedis = null;
        String result;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("set key:{} error", key, e);
            assert jedis != null;
            RedisPool.returnResource(jedis);
            return null;
        }
        RedisPool.returnResource(jedis);
        return result;
    }


    /**
     * 删除
     *
     * @param key 键
     * @return String
     */
    public static Long del(String key) {
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("set key:{} error", key, e);
            assert jedis != null;
            RedisPool.returnResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     * 自加一
     *
     * @param key key
     * @return Long
     */
    public static Long incr(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.incr(key);
        } catch (Exception e) {
            log.error("incr key : {} error", key, e);
            assert jedis != null;
            RedisPool.returnResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }
}
