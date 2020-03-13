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
 * 公文实体类
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "公文申请实体类", description = "公文申请实体类")
public class ZsDocRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
    * 主键id
    * */
    private String id;

    /*
    * 申请人id
    * */
    private String userId;

    /*
    * 申请人姓名
    * */
    @ApiModelProperty(value = "申请人姓名")
    private String username;

    /*
    * 公文标题
    * */
    @ApiModelProperty(value = "公文标题")
    private String title;

    /*
    * 公文编号
    * */
    @ApiModelProperty(value = "公文编号")
    private Integer number;

    /*
    * 公文类型
    * */
    private String docType;

    /*
    * 公文生成日期
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "公文生成日期")
    private Date createTime;

    /*
    * 发布部门
    * */
    @ApiModelProperty(value = "发布部门")
    private String sendDept;

    /*
    * 接收人id
    * */
    @ApiModelProperty(value = "接收人")
    private String receiver;

    /*
    * 接收人姓名
    * */
    @ApiModelProperty(value = "接收人姓名")
    private String receiverName;

    /*
    * 接受部门
    * */
    @ApiModelProperty(value = "接受部门")
    private String receiverDept;

    /*
    * 备注
    * */
    @ApiModelProperty(value = "备注")
    private String remarks;

    /*
    * 附件
    * */
    private String annex;

    /*
    * 发布状态
    * */
    private String sendState;

    /*
    * 公文类型
    * */
    private String state;

    /*
    * 删除状态
    * */
    private String delFlag;

    /*
    * 文件模板URL
    * */
    private String url;

    /*
    * 申请类型
    * */
    private String applyType;

    /*
    * 紧急状态
    * */
    @ApiModelProperty(value = "紧急状态（0：一般 1：紧急）")
    private String emergencyLevel;

    /*
    * 审批状态
    * */
    @ApiModelProperty(value = "审批状态（0：待审批 1：通过 2：不通过）")
    private String status;

    /*
    * 申请人部门
    * */
    @ApiModelProperty(value = "申请人部门")
    private String applyerDept;

    /*
    * 审批人
    * */
    private String approvalPerson;

    /*
    * 领导审批状态
    * */
    private String leaderStatus;

    /*
    * 领导审批意见
    * */
    private String approvalOpinion;

    /*
     * PDF文件保存地址
     * */
    private String pdfUrl;

    /*
    * 是否已读
    * */
    private String look;

    /*
    * 附件
    * */
    private String enclosure;

    /*
    * 附件说明
    * */
    private String express;

}
