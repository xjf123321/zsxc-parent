package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.process.entity.ZsMeettingGuocheng;
import com.zs.create.modules.system.entity.ZsSqRoom;
import com.zs.create.modules.system.vo.ZsSqRoomVo;

import java.util.Map;

/**
 * <p>
 * 会议室申请
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-16
 */
public interface ZsSqRoomService extends IService<ZsSqRoom> {

    ZsMeettingGuocheng add(ZsSqRoom zsSqRoom) throws Exception;

    ZsSqRoom select(String id) throws Exception;

    /**
     * 个人事务会议审批列表
     *
     * @param emergencyLevel
     * @param page
     * @return
     */
    IPage<ZsSqRoom> meettingApplyList(String emergencyLevel, IPage<ZsSqRoom> page, String title, String username);

    /**
     * 根据id查询审批单信息(会议)
     *
     * @param id
     * @return
     */
    Map formShow(String id);

    /**
     * 会议室管理审核列表
     * @param page
     * @return
     */
    IPage<ZsSqRoom> roomAdminList(IPage<ZsSqRoom> page, String username);

    IPage<ZsSqRoom> allowList(String realname, Page<ZsSqRoom> page, String username);

    void updateMeettingStatus(ZsSqRoom zsSqRoom);

    void updateMeettingStatusAndMeettingEnd(String meettingRoomName, String id);

    void cancel(String id);

    void updateStatus(ZsSqRoom zsSqRoom, String status);

    Map<String, Object> queryCollectList(ZsSqRoomVo zsSqRoomVo, Integer pageNo, Integer pageSize);

    void updateZsSqStatus(ZsSqRoom zsSqRoom1);

    String role();

    Map selectRoom(String id);

    void deleteAttachment(String id);

    void updateRoom(ZsSqRoom zsSqRoom);
}
