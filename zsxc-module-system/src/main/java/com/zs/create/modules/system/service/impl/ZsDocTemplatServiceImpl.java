package com.zs.create.modules.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.modules.system.entity.ZsDocTemplate;
import com.zs.create.modules.system.mapper.ZsDocTemplateMapper;
import com.zs.create.modules.system.service.ZsDocTemplatService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 公文模板表 service层实现
 * </p>
 * @Author yaochao
 * @since 2019-10-2
 */
@Service
@Slf4j
public class ZsDocTemplatServiceImpl extends ServiceImpl<ZsDocTemplateMapper, ZsDocTemplate> implements ZsDocTemplatService {

    @Autowired
    private ZsDocTemplateMapper zsDocTemplateMapper;

    @Override
    @Transactional
    public void removeDoc(String id) {
        zsDocTemplateMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void saveDoc(JSONObject jsonObject) {
        ZsDocTemplate template = new ZsDocTemplate();
        String templateName = jsonObject.getString("templateName");
        String addr = jsonObject.getJSONObject("addr").getJSONObject("file").getJSONObject("response").getString("message");
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        template.setAddr(addr);
        template.setTemplateName(templateName);
        template.setCreateName(sysUser.getRealname());
        String fileName = addr.substring(addr.lastIndexOf("/") + 1);
        template.setFileName(fileName);
        template.setCreateTime(new Date());
        template.setDelFlag(0);
        zsDocTemplateMapper.insert(template);
    }
}
