package com.zs.create.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 请假申请实体类
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ZsSqLeave implements Serializable {
    private static final long serialVersionUID = 1L;

    /*
    * 主键id
    * */
    @TableId(type = IdType.UUID)
    private String id;

    /*
    * 申请人id
    * */
    private String userId;

    /*
    * 申请人姓名
    * */
    @ApiModelProperty(value = "申请人姓名")
    @Excel(name = "申请人姓名", width = 15)
    private String username;

    /*
    * 请假类型（0：事假  1：病假）
    * */
    private String type;

    /*
    * 开始时间
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
    @Excel(name = "开始日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    /*
    * 结束时间
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
    @Excel(name = "结束日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    /*
    * 休假地址
    * */
    @Excel(name = "休假地址", width = 30)
    private String leaveAddress;

    /*
    * 请假天数
    * */
    @Excel(name = "请假天数", width = 15)
    private String number;

    /*
    * 联系电话
    * */
    @Excel(name = "联系电话", width = 15)
    private String telephone;

    /*
    * 通信地址
    * */
    @Excel(name = "通信地址", width = 30)
    private String signalAddress;

    /*
    * 请假事由
    * */
    @Excel(name = "请假事由", width = 30)
    private String leaveAbsence;

    /*
    * 备注
    * */
    private String remarks;

    /*
    * 审批状态（0：未审批 1：同意 2：不同意）
    * */
    @ApiModelProperty(value = "审批状态（0：未审批 1：同意 2：不同意）")
    private String status;

    /*
    * 领导审批栏
    * */
    private String approvalColumn;

    /*
    * 审批意见
    * */
    private String approvalOpinion;

    /*
    * 人员状态（0：请假中 1：在位）
    * */
    private String personStatus;

    /*
    * 申请时间
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "申请日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /*
    * 删除表识
    * */
    private String delFlag;

    /*
     * 紧急程度（0：一般 1：紧急）
     * */
    private String emergencyLevel;

    /*
     * 申请类型（0：车辆申请 1：会议申请 2：请假申请 3：销假申请）
     * */
    private String applyType;

    /*
    * 申请人部门
    * */
    private String applyerDept;

    /*
     * 审核部门
     * */
    private String currentDept;

    /*
     * 审核人ids
     * */
    private List<Map<String, String>> receiver;
}
