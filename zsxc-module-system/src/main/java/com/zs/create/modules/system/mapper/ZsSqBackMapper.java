package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.modules.system.entity.ZsSqBack;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 销假
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-18
 * */
public interface ZsSqBackMapper extends BaseMapper<ZsSqBack> {

    Long backApplyCount(@Param("deptId")String deptId, @Param("state")String state, @Param("emergencyLevel") String emergencyLevel, @Param("username") String username);

    List<ZsSqBack> backApplyList(Page<ZsSqBack> page, @Param("deptId") String deptId, @Param("state")String state, @Param("emergencyLevel") String emergencyLevel, @Param("username") String username);

    Long personnelCount();

    List<ZsSqBack> personnelList(Page<ZsSqBack> page);

    Long allowListCount(@Param("userId") String userId, @Param("username") String username);

    List<ZsSqBack> allowList(Page<ZsSqBack> page, @Param("userId") String userId, @Param("username") String username);

    void updateStatus(@Param("id") String id, @Param("status") String status);

    Long backCount(String userId);
}
