package com.zs.create.modules.quartz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.quartz.entity.QuartzJob;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * @Description: 定时任务在线管理
 * @Author: zsxc
 * @Date: 2019-04-28
 * @Version: V1.1
 */
public interface IQuartzJobService extends IService<QuartzJob> {

    List<QuartzJob> findByJobClassName(String jobClassName);

    boolean saveAndScheduleJob(QuartzJob quartzJob);

    boolean editAndScheduleJob(QuartzJob quartzJob) throws SchedulerException;

    boolean deleteAndStopJob(QuartzJob quartzJob);

    boolean resumeJob(QuartzJob quartzJob);
}
