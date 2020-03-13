package com.zs.create.modules.monitor.service.impl;

import com.zs.create.util.RedisUtil;
import com.zs.create.common.util.oConvertUtils;
import com.zs.create.domain.RedisInfo;
import com.zs.create.modules.monitor.exception.RedisConnectException;
import com.zs.create.modules.monitor.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Redis 监控信息获取
 *
 * @Author lingrui
 */
@Service("redisService")
@Slf4j
public class RedisServiceImpl implements RedisService {

    @Autowired
    @Lazy
    private RedisUtil redisUtil;

    /**
     * Redis详细信息
     */
    @Override
    public List<RedisInfo> getRedisInfo() throws RedisConnectException {
        Properties info = redisUtil.getRedisConnectionFactory().getConnection().info();
        List<RedisInfo> infoList = new ArrayList<>();
        RedisInfo redisInfo = null;
        for (Map.Entry<Object, Object> entry : info.entrySet()) {
            redisInfo = new RedisInfo();
            redisInfo.setKey(oConvertUtils.getString(entry.getKey()));
            redisInfo.setValue(oConvertUtils.getString(entry.getValue()));
            infoList.add(redisInfo);
        }
        return infoList;
    }

    @Override
    public Map<String, Object> getKeysSize() throws RedisConnectException {
        Long dbSize = redisUtil.getRedisConnectionFactory().getConnection().dbSize();
        Map<String, Object> map = new HashMap<>();
        map.put("create_time", System.currentTimeMillis());
        map.put("dbSize", dbSize);

        log.info("--getKeysSize--: " + map.toString());
        return map;
    }

    @Override
    public Map<String, Object> getMemoryInfo() throws RedisConnectException {
        Map<String, Object> map = null;
        Properties info = redisUtil.getRedisConnectionFactory().getConnection().info();
        for (Map.Entry<Object, Object> entry : info.entrySet()) {
            String key = oConvertUtils.getString(entry.getKey());
            if ("used_memory".equals(key)) {
                map = new HashMap<>();
                map.put("used_memory", entry.getValue());
                map.put("create_time", System.currentTimeMillis());
            }
        }
        log.info("--getMemoryInfo--: " + map.toString());
        return map;
    }
}
