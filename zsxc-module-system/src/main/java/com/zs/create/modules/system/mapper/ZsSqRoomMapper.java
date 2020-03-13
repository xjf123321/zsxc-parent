package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zs.create.modules.system.entity.Done;
import com.zs.create.modules.system.entity.ZsSqRoom;
import com.zs.create.modules.system.vo.ZsSqRoomVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 会议室申请
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-16
 */
public interface ZsSqRoomMapper extends BaseMapper<ZsSqRoom> {

    Long meettingApplyCount(@Param("deptId") String deptId, @Param("state") String state, @Param("emergencyLevel") String emergencyLevel,
                            @Param("title") String title, @Param("username") String username);

    List<ZsSqRoom> meettingApplyList(IPage<ZsSqRoom> page, @Param("deptId") String deptId, @Param("state") String state, @Param("emergencyLevel") String emergencyLevel,
                                     @Param("title") String title, @Param("username") String username);

    Long roomAdminCount(@Param("username") String username);

    List<ZsSqRoom> roomAdminList(IPage<ZsSqRoom> page, @Param("username") String username);

    Long allowListCount(@Param("userId") String userId, @Param("username") String username);

    List<ZsSqRoom> allowList(IPage<ZsSqRoom> page, @Param("userId") String userId, @Param("username") String username);

    void updateMeettingEnd(@Param("meettingEnd") Date date, @Param("id") String id);

    ZsSqRoom formShow(String id);

    String selectRoomName(String id);

    void cancel(String id);

    Long count(@Param("meettingHost") String meettingHost);

    List<ZsSqRoomVo> queryCollectList(@Param("meettingHost") String meettingHost, @Param("firstIndex") int firstIndex,
                                    @Param("lastIndex") int lastIndex);

    void updateStatus(@Param("id") String id, @Param("status") String status);

    Long roomCount(String userId);

    void deleteAttachment(@Param("id") String id);

    ZsSqRoomVo findById(String id);

    List<Done> done(String userId);

    Done selectBy(@Param("id") String id);
}
