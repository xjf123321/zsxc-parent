package com.zs.create.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.modules.system.entity.ZsDocRecord;
import com.zs.create.modules.system.entity.ZsReturnPath;
import com.zs.create.modules.system.entity.ZsSqNotice;
import com.zs.create.modules.system.mapper.ZsDocRecordMapper;
import com.zs.create.modules.system.mapper.ZsReturnPathMapper;
import com.zs.create.modules.system.mapper.ZsSqNoticeMapper;
import com.zs.create.modules.system.service.ZsReturnPathService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ZsReturnPathServiceImpl extends ServiceImpl<ZsReturnPathMapper, ZsReturnPath> implements ZsReturnPathService {

    @Autowired
    private ZsReturnPathMapper zsReturnPathMapper;

    @Autowired
    private ZsDocRecordMapper zsDocRecordMapper;

    @Autowired
    private ZsSqNoticeMapper zsSqNoticeMapper;

    @Override
    @Transactional
    public ZsDocRecord updateUrl(String id, String url) {
        QueryWrapper<ZsReturnPath> qw = new QueryWrapper<>();
        qw.eq("url", id);
        ZsReturnPath returnPath = zsReturnPathMapper.selectOne(qw);
        returnPath.setUrl(url);
        zsReturnPathMapper.updateById(returnPath);
        ZsDocRecord zsDocRecord = zsDocRecordMapper.findByUrl(returnPath.getId());
        return zsDocRecord;

    }

    @Override
    public ZsSqNotice updateNotice(String id, String url) {
        QueryWrapper<ZsReturnPath> qw = new QueryWrapper<>();
        qw.eq("url", id);
        ZsReturnPath returnPath = zsReturnPathMapper.selectOne(qw);
        returnPath.setUrl(url);
        zsReturnPathMapper.updateById(returnPath);
        ZsSqNotice zsSqNotice = zsSqNoticeMapper.findByUrl(returnPath.getId());
        return zsSqNotice;
    }
}
