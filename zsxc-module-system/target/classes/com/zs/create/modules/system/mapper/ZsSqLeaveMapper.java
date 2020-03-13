package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.modules.system.entity.Done;
import com.zs.create.modules.system.entity.ZsSqLeave;
import com.zs.create.modules.system.entity.ZsSqNotice;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 请假申请
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-16
 */
public interface ZsSqLeaveMapper extends BaseMapper<ZsSqLeave> {
    List<ZsSqLeave> leaveApplyList(IPage<ZsSqLeave> page, @Param("deptId") String deptId, @Param("state") String state, @Param("emergencyLevel") String emergencyLevel,
                                   @Param("title") String title, @Param("username") String username);

    Long leaveApplyCount(@Param("deptId") String deptId, @Param("state") String state, @Param("emergencyLevel") String emergencyLevel,
                         @Param("title") String title, @Param("username") String username);

    void updateStatus(@Param("id")String id, @Param("status")String status);

    ZsSqLeave formShow(String id);

    Long haveDoneCount(@Param("userId")String id, @Param("username") String username);

    List<ZsSqLeave> haveDoneList(Page<ZsSqLeave> page, @Param("userId")String id, @Param("username") String username);

    Long queryZsSqLeaveCount(@Param("userId") String userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<ZsSqNotice> queryZsSqLeave(@Param("userId") String userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate,
                                    @Param("firstIndex") int firstIndex, @Param("lastIndex") int lastIndex);

    IPage<ZsSqLeave> findList(@Param("page")Page<ZsSqLeave> page, @Param("zsSqLeave")ZsSqLeave zsSqLeave, @Param("userId")String userId);

    void add(ZsSqLeave zsSqLeave);

    IPage<ZsSqLeave> needDoList(Page<ZsSqLeave> page, @Param("userId")String userId, @Param("emergencyLevel")String emergencyLevel, @Param("title")String title, @Param("username")String username);

    ZsSqLeave findById(String id);

    IPage<ZsSqLeave> doneList(Page<ZsSqLeave> page, @Param("userId")String userId, @Param("username")String username);

    IPage<ZsSqLeave> leaveList(Page<ZsSqLeave> page, @Param("zsSqLeave")ZsSqLeave zsSqLeave);

    void updateZsLeave(ZsSqLeave zsSqLeave);

    List<ZsSqLeave> findAll(@Param("zsSqLeave")ZsSqLeave zsSqLeave);

    Long leaveCount(String userId);

    List<Done> done(String userId);

    Done selectBy(@Param("id") String id);
}
