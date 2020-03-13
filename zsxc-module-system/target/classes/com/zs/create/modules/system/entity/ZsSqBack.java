package com.zs.create.modules.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 销假实体类
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-18
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "销假申请实体类", description = "销假申请实体类")
public class ZsSqBack implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
    * 主键id
    * */
    private String id;

    /*
    *申请人id
    * */
    private String userId;

    /*
    *申请人姓名
    * */
    @ApiModelProperty(value = "申请人姓名")
    private String username;

    /*
    * 销假类型（0.公休1.病假2.事假3.调休4.婚假）
    * */
    @ApiModelProperty(value = "销假类型（0：事假  1：病假）")
    private String type;

    /*
    * 实际请假时间
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "实际请假时间")
    private Date actualLeaveStart;

    /*
    * 实际请假结束时间
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "实际请假结束时间")
    private Date actualLeaveEnd;

    /*
    * 实际请假天数
    * */
    @ApiModelProperty(value = "实际请假天数")
    private String actualNumber;

    /*
    * 联系电话
    * */
    @ApiModelProperty(value = "联系电话")
    private String telephone;

    /*
    * 通信地址
    * */
    @ApiModelProperty(value = "通信地址")
    private String signalAddress;

    /*
    * 备注
    * */
    @ApiModelProperty(value = "备注")
    private String remarks;

    /*
    * 审批状态（0：待审批 1：审批通过 2：审批不通过）
    * */
    @ApiModelProperty(value = "审批状态（0：待审批 1：审批通过 2：审批不通过）")
    private String status;

    /*
    * 审批意见
    * */
    @ApiModelProperty(value = "审批意见")
    private String approvalOpinion;

    /*
    * 人员状态（0：在位  1：事假  2：病假）
    * */
    @ApiModelProperty(value = "人员状态（0：在位  1：事假  2：病假）")
    private String personStatus;

    /*
    * 创建时间
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /*
    * 删除标识
    * */
    private String delFlag;

    /*
    * 紧急程度（0：一般 1：紧急）
    * */
    @ApiModelProperty(value = "紧急程度（0：一般 1：紧急）")
    private String emergencyLevel;

    /*
    * 申请类型（0：车辆申请 1：会议申请 2：请假申请 3：销假申请）
    * */
    @ApiModelProperty(value = "申请类型（0：车辆申请 1：会议申请 2：请假申请 3：销假申请）")
    private String applyType;

    /*
    * 申请人部门
    * */
    private String applyerDept;

}
