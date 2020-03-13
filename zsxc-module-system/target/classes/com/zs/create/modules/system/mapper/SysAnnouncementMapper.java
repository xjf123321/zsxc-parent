package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.modules.system.entity.SysAnnouncement;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 系统通告表
 * @Author: zsxc
 * @Date: 2019-01-02
 * @Version: V1.0
 */
public interface SysAnnouncementMapper extends BaseMapper<SysAnnouncement> {


    List<SysAnnouncement> querySysCementListByUserId(Page<SysAnnouncement> page, @Param("userId") String userId, @Param("msgCategory") String msgCategory);

}
