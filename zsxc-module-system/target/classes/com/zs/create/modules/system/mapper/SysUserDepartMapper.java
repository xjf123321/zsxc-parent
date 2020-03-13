package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.create.modules.system.entity.SysUserDepart;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * @Description SysUserDepartMapper
 * @Author HeLiu
 * @Date 2019/9/28 9:48
 **/
public interface SysUserDepartMapper extends BaseMapper<SysUserDepart> {

    List<SysUserDepart> getUserDepartByUid(@Param("userId") String userId);

    String selectDeptIdByUserId(@Param("userId") String userId);

    List<String> selectIdsByDeptId(@Param("deptId") String deptId);

    String selectRealNameById(String approvalPerson);

    List<String> selectByDeptId(@Param("deptId") String deptId);
}
