package com.zs.create.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.modules.system.entity.ZsOperationalSituation;
import com.zs.create.modules.system.mapper.ZsOperationalSituationMapper;
import com.zs.create.modules.system.service.ZsOperationalSituationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ZsOperationalSituationServiceImpl extends ServiceImpl<ZsOperationalSituationMapper, ZsOperationalSituation> implements ZsOperationalSituationService {

    @Autowired
    private ZsOperationalSituationMapper zsOperationalSituationMapper;

    @Override
    public List<ZsOperationalSituation> select(String userId) {
        List<ZsOperationalSituation> list = zsOperationalSituationMapper.select(userId);
        return list;
    }

    @Override
    @Transactional
    public void saveFight(List<ZsOperationalSituation> list) {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        List<ZsOperationalSituation> operationalSituations = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == null) {
                operationalSituations.add(list.get(i));
                list.remove(i);
                i--;
            }
        }
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                List<ZsOperationalSituation> zsOperationalSituationList = new ArrayList<>();
                zsOperationalSituationList.add(list.get(i));
                zsOperationalSituationMapper.updateOperational(zsOperationalSituationList);
            }
        }
        for (int i = 0; i < operationalSituations.size(); i++) {
            operationalSituations.get(i).setCreateTime(new Date());
            operationalSituations.get(i).setDelFlag("0");
            operationalSituations.get(i).setCreater(sysUser.getId());
            zsOperationalSituationMapper.insert(operationalSituations.get(i));
    }
    }

    @Override
    @Transactional
    public void deleteOperational(String id) {
        zsOperationalSituationMapper.deleteOperational(id);
    }
}
