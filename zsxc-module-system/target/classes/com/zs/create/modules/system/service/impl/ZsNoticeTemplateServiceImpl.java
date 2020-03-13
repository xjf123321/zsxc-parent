package com.zs.create.modules.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.modules.system.entity.*;
import com.zs.create.modules.system.mapper.ZsNoticeTemplateMapper;
import com.zs.create.modules.system.service.ZsNoticeTemplatService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 通知模板表 service层实现
 * </p>
 * @Author yaochao
 * @since 2019-10-14
 */
@Service
@Slf4j
public class ZsNoticeTemplateServiceImpl extends ServiceImpl<ZsNoticeTemplateMapper, ZsNoticeTemplate> implements ZsNoticeTemplatService {

    @Autowired
    private ZsNoticeTemplateMapper zsNoticeTemplateMapper;

    @Override
    @Transactional
    public void saveNotice(JSONObject jsonObject) {
        ZsNoticeTemplate template = new ZsNoticeTemplate();
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        String templateName = jsonObject.getString("templateName");
        String addr = jsonObject.getJSONObject("addr").getJSONObject("file").getJSONObject("response").getString("message");

        template.setAddr(addr);
        template.setTemplateName(templateName);
        template.setCreateName(sysUser.getRealname());
        String fileName = addr.substring(addr.lastIndexOf("/") + 1);
        template.setFileName(fileName);
        template.setCreateTime(new Date());
        template.setDelFlag(0);
        zsNoticeTemplateMapper.insert(template);
    }

    @Override
    @Transactional
    public void removeNotice(String id) {
        zsNoticeTemplateMapper.deleteById(id);
    }
}
