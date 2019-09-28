package org.yangxin.permission.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

/**
 * Redis池
 *
 * @author yangxin
 * 2019/09/10 15:39
 */
@Service("redisPool")
@Slf4j
public class RedisPoolService {
    @Resource(name = "shardedJedisPool")
    private ShardedJedisPool shardedJedisPool;

    /**
     * 获得一个实例
     */
    public ShardedJedis instance() {
        return shardedJedisPool.getResource();
    }

    /**
     * 安全关闭一个redis连接
     */
    void safeClose(ShardedJedis shardedJedis) {
        try {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        } catch (Exception e) {
            log.error("return redis resource exception", e);
        }
    }
}
