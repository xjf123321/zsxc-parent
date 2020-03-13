package com.zs.create.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.modules.system.entity.ZsRewards;
import com.zs.create.modules.system.mapper.ZsRewardsMapper;
import com.zs.create.modules.system.service.ZsRewardsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ZsRewardsServiceImpl extends ServiceImpl<ZsRewardsMapper, ZsRewards> implements ZsRewardsService {

    @Autowired
    private ZsRewardsMapper zsRewardsMapper;

    @Override
    public List<ZsRewards> select(String userId) {
        List<ZsRewards> list = zsRewardsMapper.select(userId);
        return list;
    }

    @Override
    public void saveRecords(List<ZsRewards> list) {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        List<ZsRewards> zsRewards = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == null) {
                zsRewards.add(list.get(i));
                list.remove(i);
                i--;
            }
        }
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                List<ZsRewards> zsRewardsList = new ArrayList<>();
                zsRewardsList.add(list.get(i));
                zsRewardsMapper.updateRewards(zsRewardsList);
            }
        }
        for (int i = 0; i < zsRewards.size(); i++) {
            zsRewards.get(i).setCreateTime(new Date());
            zsRewards.get(i).setDelFlag("0");
            zsRewards.get(i).setCreater(sysUser.getId());
            zsRewardsMapper.insert(zsRewards.get(i));
        }
    }

    @Override
    @Transactional
    public void deleteRecords(String id) {
        zsRewardsMapper.deleteRecords(id);
    }
}
