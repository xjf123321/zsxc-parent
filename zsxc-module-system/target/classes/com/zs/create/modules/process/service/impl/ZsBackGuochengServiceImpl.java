package com.zs.create.modules.process.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.modules.process.entity.ZsBackGuocheng;
import com.zs.create.modules.system.entity.ZsSqBack;
import com.zs.create.modules.process.mapper.ZsBackGuochengMapper;
import com.zs.create.modules.process.service.ZsBackGuochengService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
@Service
@Slf4j
public class ZsBackGuochengServiceImpl extends ServiceImpl<ZsBackGuochengMapper, ZsBackGuocheng> implements ZsBackGuochengService {
    @Autowired
    private ZsBackGuochengMapper zsBackGuochengMapper;

    @Override
    @Transactional
    public void addRecord(ZsSqBack zsSqBack) {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        ZsBackGuocheng zsBackGuocheng = new ZsBackGuocheng();
        zsBackGuocheng.setState(zsSqBack.getStatus());
        zsBackGuocheng.setCreateTime(new Date());
        zsBackGuocheng.setApprovalOpinion(zsSqBack.getApprovalOpinion());
        zsBackGuocheng.setUserName(sysUser.getRealname());
        zsBackGuocheng.setUserId(sysUser.getId());
        zsBackGuocheng.setBackId(zsSqBack.getId());
        zsBackGuocheng.setAutograph(zsSqBack.getRemarks());
        if (zsSqBack.getStatus().equals("1")) {
            zsBackGuocheng.setPlayName("领导同意");
        }
        if (zsSqBack.getStatus().equals("2")) {
            zsBackGuocheng.setPlayName("领导不同意");
        }
        zsBackGuochengMapper.insert(zsBackGuocheng);
    }
}
