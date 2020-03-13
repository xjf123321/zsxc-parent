package com.zs.create.modules.system.vo;

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

/**
 * <p>
 * 会议室申请实体类
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "会议申请实体类", description = "会议申请实体类")
public class ZsSqRoomVo implements Serializable {

    private static final long serialVersionUID = 1L;
    /*
     * id
     * */
    @TableId(type = IdType.UUID)
    private String id;

    /*
     * 会议申请人id
     * */
    private String userId;

    /*
     * 会议申请人姓名
     * */
    @ApiModelProperty(value = "会议申请人姓名")
    private String username;

    /*
     * 会议类型
     * */
    @ApiModelProperty(value = "会议类型")
    private String meettingType;

    /*
     * 会议标题
     * */
    @ApiModelProperty(value = "会议标题")
    private String meettingTitle;

    /*
     * 会议标语
     * */
    @ApiModelProperty(value = "会议标语")
    private String conferenceSlogan;

    /*
     * 会议时间开始时间
     * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "会议时间开始时间")
    private Date meettingStart;

    /*
     * 会议结束时间
     * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "会议结束时间")
    private Date meettingEnd;

    /*
     * 主持人
     * */
    @ApiModelProperty(value = "主持人")
    private String meettingHost;

    /*
     * 会议名称
     * */
    @ApiModelProperty(value = "会议名称")
    private String meettingName;

    /*
     * 会议室名称
     * */
    @ApiModelProperty(value = "会议室名称")
    private String meettingRoomName;

    /*
     * 备注
     * */
    @ApiModelProperty(value = "备注")
    private String remarks;

    /*
     * 审批状态（0：未审批 1：同意 2：不同意）
     * */
    private String status;

    /*
     * 领导审批栏
     * */
    private String approvalColumn;

    /*
     * 领导审批意见
     * */
    @ApiModelProperty(value = "领导审批意见")
    private String approvalOpinion;

    /*
     * 附件
     * */
    private String url;

    /*
     * 会议纪要
     * */
    @ApiModelProperty(value = "会议纪要")
    private String description;

    /*
     * 申请时间
     * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "申请时间")
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
    private String applyType;

    /*
     * 申请人部门
     * */
    private String applyerDept;

    /*
    * 附件数组
    * */
    private List<String> files;

}
