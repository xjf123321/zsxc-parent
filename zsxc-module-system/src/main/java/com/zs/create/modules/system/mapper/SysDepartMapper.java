package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.create.modules.system.entity.SysDepart;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 * 部门 Mapper 接口
 * <p>
 *
 * @Author: lingrui
 * @Since： 2019-01-22
 */
public interface SysDepartMapper extends BaseMapper<SysDepart> {

    /**
     * 根据用户ID查询部门集合
     */
    List<SysDepart> queryUserDeparts(@Param("userId") String userId);

    String selectParentIdById(String id);

    Long selectId(String parentId);

    /**
     * 根据用户ID查询分级部门领导
     */
    List<SysDepart> listDeparts(@Param("userId") String userId);

    String selectDeptName(String deptId);
}
