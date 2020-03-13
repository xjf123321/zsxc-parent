package com.zs.create.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.modules.system.entity.ZsTrainingExperience;
import com.zs.create.modules.system.mapper.ZsTrainingExperienceMapper;
import com.zs.create.modules.system.service.ZsTrainingExperienceService;
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
public class ZsTrainingExperienceServiceImpl extends ServiceImpl<ZsTrainingExperienceMapper, ZsTrainingExperience> implements ZsTrainingExperienceService {
    @Autowired
    private ZsTrainingExperienceMapper zsTrainingExperienceMapper;
    @Override
    public List<ZsTrainingExperience> select(String userId) {
        List<ZsTrainingExperience> list = zsTrainingExperienceMapper.select(userId);
        return list;
    }

    @Override
    public void saveTraining(List<ZsTrainingExperience> list) {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        List<ZsTrainingExperience> zsTrainingExperiences = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == null) {
                zsTrainingExperiences.add(list.get(i));
                list.remove(i);
                i--;
            }
        }
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                List<ZsTrainingExperience> zsTrainingExperienceList = new ArrayList<>();
                zsTrainingExperienceList.add(list.get(i));
                zsTrainingExperienceMapper.updateTraining(zsTrainingExperienceList);
            }
        }
        for (int i = 0; i < zsTrainingExperiences.size(); i++) {
            zsTrainingExperiences.get(i).setCreateTime(new Date());
            zsTrainingExperiences.get(i).setDelFlag("0");
            zsTrainingExperiences.get(i).setCreater(sysUser.getId());
            zsTrainingExperienceMapper.insert(zsTrainingExperiences.get(i));
        }
    }

    @Override
    @Transactional
    public void deleteTraining(String id) {
        zsTrainingExperienceMapper.deleteTraining(id);
    }
}
