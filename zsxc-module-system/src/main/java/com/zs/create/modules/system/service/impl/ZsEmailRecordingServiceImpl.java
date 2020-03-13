package com.zs.create.modules.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.modules.system.entity.ZsEmailRecording;

import com.zs.create.modules.system.mapper.ZsEmailRecordingMapper;
import com.zs.create.modules.system.service.ZsEmailRecordingService;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 记录表service实现
 * </p>
 * @Author yaochao
 * @since 2019-10-14
 */
@Service
@Slf4j
public class ZsEmailRecordingServiceImpl extends ServiceImpl<ZsEmailRecordingMapper, ZsEmailRecording> implements ZsEmailRecordingService {

    @Autowired
    private ZsEmailRecordingMapper zsEmailRecordingMapper;

    @Override
    @Transactional
    public void updateStatus(ZsEmailRecording recording) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        QueryWrapper<ZsEmailRecording> qw = new QueryWrapper<>();
        qw.eq("email_id", recording.getEmailId());
        qw.eq("loginname", sysUser.getId());
        zsEmailRecordingMapper.update(recording, qw);
    }

    @Override
    @Transactional
    public void delte(String id) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        QueryWrapper<ZsEmailRecording> qw = new QueryWrapper<>();
        qw.eq("email_id", id);
        qw.eq("loginname", sysUser.getId());
        zsEmailRecordingMapper.delete(qw);
    }
}
