package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.zs.create.modules.system.entity.SysDepart;
import com.zs.create.modules.system.entity.ZsSqLeave;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 请假申请
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-16
 */
public interface ZsSqLeaveService extends IService<ZsSqLeave> {
    void add(ZsSqLeave zsSqLeave) throws Exception;

    ZsSqLeave selectById(String id) throws Exception;

    /**
     * 个人事务请假审批列表
     * @param page
     * @return
     */
    IPage<ZsSqLeave> leaveApplyList(IPage<ZsSqLeave> page, String emergencyLevel, String title, String username);

    /**
     * 修改状态
     * @param zsSqLeave
     */
    void updateStatus(ZsSqLeave zsSqLeave);

    /**
     * 根据id查询表单和过程
     * @param id
     * @return
     */
    Map formShow(String id);

    IPage<ZsSqLeave> haveDoneList(Page<ZsSqLeave> page, String username);

    Map<String, Object> queryZsSqLeave(ZsSqLeave zsSqLeave, Integer pageNo, Integer pageSize);

    Boolean listDeparts();
}
