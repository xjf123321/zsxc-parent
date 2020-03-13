package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.create.modules.system.entity.ApprovalInformation;

public interface ApprovalInformationMapper extends BaseMapper<ApprovalInformation> {
    ApprovalInformation selectByName(String name);
}
