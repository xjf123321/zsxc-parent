package com.zs.create.modules.zdemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.zs.create.modules.zdemo.entity.DemoEntity;
import com.zs.create.modules.zdemo.mapper.DemoMapper;
import com.zs.create.modules.zdemo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author heliu
 * @Description 测试Service实现层
 * @email heliu@zs-create.com
 * @date 2019-08-30 10:23:20
 * @Version: V1.0
 */
@Service
public class DemoServiceImpl extends ServiceImpl<DemoMapper, DemoEntity> implements DemoService {


    @Autowired
    private DemoMapper demoMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @DS("multi-datasource1")
    public Map mulQueryById(String id) {
        Map result = Maps.newHashMap();
        Map<String, Object> objectMap = jdbcTemplate.queryForMap("select * from demo where id=?", id);
        DemoEntity demoEntity = JSON.parseObject(JSON.toJSONString(objectMap), DemoEntity.class);
        result.put("jdbcTemplate", demoEntity);
        result.put("mybatis", demoMapper.selectById(id));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transaction(String id) throws Exception {
        DemoEntity demoEntity = demoMapper.selectById(id);
        demoEntity.setName("TRA_TEST");
        demoMapper.updateById(demoEntity);
        throw new Exception("Transaction Error");
    }

}


