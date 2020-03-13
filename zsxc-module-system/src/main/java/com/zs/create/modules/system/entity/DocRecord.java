package com.zs.create.modules.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DocRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
    * 主键id
    * */
    private String id;

    /*
    * 申请单id
    * */
    private String docId;

    /*
    * 审批人
    * */
    private String approvalPerson;

    /*
    * 审批意见
    * */
    private String approvalOpinion;

    /*
    * 申请人id
    * */
    private String applyer;

    /*
    * 申请人姓名
    * */
    private String userName;

    /*
    * 创建时间
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /*
    * 个人审批状态
    * */
    private String approvalStatus;

    /*
    * 操作名称
    * */
    private String playName;

    /*
    * 紧急类型
    * */
    private String emergencyLevel;

    /*
     * 签名
     * */
    private String autograph;
}
