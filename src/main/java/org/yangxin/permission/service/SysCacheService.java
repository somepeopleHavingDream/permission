package org.yangxin.permission.service;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yangxin.permission.beans.CacheKeyConstants;
import org.yangxin.permission.util.GsonUtil;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;

/**
 * 缓存Service
 *
 * @author yangxin
 * 2019/09/27 16:51
 */
@Service
@Slf4j
public class SysCacheService {
    @Resource(name = "redisPool")
    private RedisPool redisPool;

    public void saveCache(String toSavedValue, int timeoutSeconds, CacheKeyConstants prefix) {
        saveCache(toSavedValue, timeoutSeconds, prefix, (String[]) null);
    }

    void saveCache(String toSavedValue, int timeoutSeconds, CacheKeyConstants prefix, String... keys) {
        if (toSavedValue == null) {
            return;
        }
        ShardedJedis shardedJedis = null;
        try {
            String cacheKey = generateCacheKey(prefix, keys);
            shardedJedis = redisPool.instance();
            shardedJedis.setex(cacheKey, timeoutSeconds, toSavedValue);
        } catch (Exception e) {
            log.error("save cache exception, prefix: [{}], keys: [{}]", prefix.name(), GsonUtil.obj2String(keys), e);
//            log.error("save cache exception, prefix: [{}], keys: [{}]", prefix.name(), JsonMapper.obj2String(keys), e);
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    /**
     * 从缓存中得到
     */
    String getFromCache(CacheKeyConstants prefix, String... keys) {
        ShardedJedis shardedJedis = null;
        String cacheKey = generateCacheKey(prefix, keys);
        try {
            shardedJedis = redisPool.instance();
            return shardedJedis.get(cacheKey);
        } catch (Exception e) {
            log.error("get from cache exception, prefix: [{}], keys: [{}]", prefix.name(), GsonUtil.obj2String(keys), e);
//            log.error("get from cache exception, prefix: [{}], keys: [{}]", prefix.name(), JsonMapper.obj2String(keys), e);
            return null;
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    /**
     * 生成缓存键
     */
    private String generateCacheKey(CacheKeyConstants prefix, String... keys) {
        String key = prefix.name();
        if (keys != null && keys.length > 0) {
            key += "_" + Joiner.on("_").join(keys);
        }
        return key;
    }
}
