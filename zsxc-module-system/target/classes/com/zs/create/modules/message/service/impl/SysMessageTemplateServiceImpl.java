package com.zs.create.modules.message.service.impl;

import com.zs.create.modules.message.service.ISysMessageTemplateService;
import com.zs.create.base.service.impl.BaseServiceImpl;
import com.zs.create.modules.message.entity.SysMessageTemplate;
import com.zs.create.modules.message.mapper.SysMessageTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 消息模板
 * @Author: zsxc
 * @Date: 2019-04-09
 * @Version: V1.0
 */
@Service
public class SysMessageTemplateServiceImpl extends BaseServiceImpl<SysMessageTemplateMapper, SysMessageTemplate> implements ISysMessageTemplateService {

    @Autowired
    private SysMessageTemplateMapper sysMessageTemplateMapper;


    @Override
    public List<SysMessageTemplate> selectByCode(String code) {
        return sysMessageTemplateMapper.selectByCode(code);
    }
}
