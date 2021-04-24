package link.thingscloud.spring.boot.common.redis;

/**
 * @author zhouhailin
 * @since 1.0.0
 */
public interface DistributedRedisLimiter {

    /**
     * 在阈值范围内获取
     *
     * @param key     业务类型
     * @param hashKey 业务标识
     * @param permits 阈值
     * @return 是否设置成功
     */
    boolean tryAcquire(String key, String hashKey, int permits);

    /**
     * 释放
     *
     * @param key     业务类型
     * @param hashKey 业务标识
     * @return 是否是否成功
     */
    boolean tryRelease(String key, String hashKey);

}
