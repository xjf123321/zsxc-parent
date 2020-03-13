package com.zs.create.modules.zdemo.service;

import com.zs.create.modules.zdemo.entity.DemoEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author heliu
 * @Description 测试Service层
 * @email heliu@zs-create.com
 * @date 2019-08-30 10:23:20
 * @Version: V1.0
 */
public interface DemoService extends IService<DemoEntity> {

    Map mulQueryById(String id);

    void transaction(String id) throws Exception;
}




