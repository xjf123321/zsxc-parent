package com.zs.create.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zs.create.modules.workflow.vo.BpmnWork;
import com.zs.create.modules.workflow.vo.BusinessShenqing;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author heliu
 * @Description 测试Mapper层
 * @email heliu@zs-create.com
 * @date 2019-08-30 10:23:20
 * @Version: V1.0
 */
public interface BpmWorkMapper extends BaseMapper<BpmnWork> {

    List<BpmnWork> workList(IPage<BpmnWork> page, @Param("realname") String realname, @Param("id") String id,String emergency_level,String deptId,String parentDetId);

    Long workCount(@Param("realname") String realname, @Param("id") String id,String emergency_level,String deptId,String parentDetId);

    BusinessShenqing selectBySqId(@Param("sqId") String sqId);
}


