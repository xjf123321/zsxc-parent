package com.zs.create.modules.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.modules.system.entity.ZsPath;

import com.zs.create.modules.system.mapper.ZsPathMapper;
import com.zs.create.modules.system.service.ZsPathService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 路径表service层实现
 * </p>
 * @Author yaochao
 * @since 2019-10-14
 */
@Service
@Slf4j
public class ZsPathServiceImpl extends ServiceImpl<ZsPathMapper, ZsPath> implements ZsPathService {

    @Autowired
    private ZsPathMapper zsPathMapper;
}
