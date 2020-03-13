package com.zs.create.modules.workflow.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * <p>
 * 审批表实体类
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-25
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BusinessShenqing implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
    * 主键
    * */
    private String id;

    /*
    * 业务表单id
    * */
    private String businessFormDataId;

    /*
    * 流程任务id
    * */
    private String processinstanceId;

    /*
    * 业务申请人id
    * */
    private String businessSqUserid;

    /*
    * 业务名称
    * */
    private String businessName;

    /*
    * 业务状态
    * */
    private String businessStatus;

    /*
    * 申请时间
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String businessSqtime;

    /*
    * 删除标志
    * */
    private String del_flag;

    /*
    * 紧急状态
    * */
    private String emergencyLevel;

    /*
    * 申请表单id
    * */
    private String sqId;

    /*
    * work_id
    * */
    private String workId;

    /*
    * 申请人部门id
    * */
    private String deptId;

    /*
    * 申请人上级部门id
    * */
    private String parentDeptId;

}
