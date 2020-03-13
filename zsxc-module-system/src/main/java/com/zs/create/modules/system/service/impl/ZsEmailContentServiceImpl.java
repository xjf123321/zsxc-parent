package com.zs.create.modules.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zs.create.modules.system.entity.ZsEmailContent;
import com.zs.create.modules.system.mapper.ZsEmailContentMapper;

import com.zs.create.modules.system.service.ZsEmailContentService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 内容表service层实现
 * </p>
 * @Author yaochao
 * @since 2019-10-14
 */
@Service
@Slf4j
public class ZsEmailContentServiceImpl extends ServiceImpl<ZsEmailContentMapper, ZsEmailContent> implements ZsEmailContentService {

    @Autowired
    private ZsEmailContentMapper zsEmailContentMapper;
}
