package com.zs.create.modules.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.create.modules.quartz.entity.QuartzJob;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 定时任务在线管理
 * @Author: zsxc
 * @Date: 2019-01-02
 * @Version: V1.0
 */
public interface QuartzJobMapper extends BaseMapper<QuartzJob> {

    List<QuartzJob> findByJobClassName(@Param("jobClassName") String jobClassName);

}
