package com.zs.create.modules.monitor.service;

import com.zs.create.domain.RedisInfo;
import com.zs.create.modules.monitor.exception.RedisConnectException;

import java.util.List;
import java.util.Map;

/**
 * @Description RedisService
 * @Author HeLiu
 * @Date 2019/9/28 9:18
 **/
public interface RedisService {

    /**
     * 获取 redis 的详细信息
     *
     * @return List
     */
    List<RedisInfo> getRedisInfo() throws RedisConnectException;

    /**
     * 获取 redis key 数量
     *
     * @return Map
     */
    Map<String, Object> getKeysSize() throws RedisConnectException;

    /**
     * 获取 redis 内存信息
     *
     * @return Map
     */
    Map<String, Object> getMemoryInfo() throws RedisConnectException;

}
