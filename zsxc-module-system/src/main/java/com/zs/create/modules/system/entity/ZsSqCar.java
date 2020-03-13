package com.zs.create.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 车辆申请实体类
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "车辆申请实体类", description = "车辆申请实体类")
public class ZsSqCar implements Serializable {
    private static final long serialVersionUID = 1L;
    /*
     * id
     * */
    @TableId(type = IdType.UUID)
    private String id;

    /*
    * 申请人
    * */
    private String userId;

    /*
    * 申请人姓名
    * */
    private String username;

    /*
    * 申请人部门
    * */
    private String applyerDept;

    /*
    * 出行人数
    * */
    private Integer number;

    /*
    * 同行干系人
    * */
    private String party;

    /*
    * 车牌号
    * */
    private String plateNumber;

    /*
    * 出场时间
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date outDate;

    /*
    * 归场时间
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inDate;

    /*
    * 起始地址
    * */
    private String startPlace;

    /*
    * 结束地址
    * */
    private String endPlace;

    /*
    * 用车单位
    * */
    private String useDept;

    /*
    * 用车事由
    * */
    private String reason;

    /*
    * 备注
    * */
    private String remarks;

    /*
    * 领导审批栏
    * */
    private String approvalColumn;

    /*
    * 审批意见
    * */
    @ApiModelProperty(value = "领导审批意见")
    private String approvalOpinion;

    /*
    * 删除标识
    * */
    private String delFlag;

    /*
    * 状态（0：待审批 1：已驳回 2：已派车 3：使用中 4：已归还）
    * */
    private String status;

    /*
    * 申请时间
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /*
     * 紧急程度（0：一般 2：紧急）
     * */
    private String emergencyLevel;

    /*
     * 申请类型（0：车辆申请 1：会议申请 2：请假申请 3：销假申请）
     * */
    private String applyType;

    /*
    * 审核人集合
    * */
    private List<Map<String,String>> receiver;

    /*
    * 车辆名称
    * */
    private String carName;

}
