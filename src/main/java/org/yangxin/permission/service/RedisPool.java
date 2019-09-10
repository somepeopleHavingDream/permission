package org.yangxin.permission.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

/**
 * Redis池
 *
 * @author yangxin
 * 2019/09/10 15:39
 */
@Service
@Slf4j
public class RedisPool {
//    @Resource(name = "sharedJedisPool")
//    private ShardedJedisPool shardedJedisPool;
}
