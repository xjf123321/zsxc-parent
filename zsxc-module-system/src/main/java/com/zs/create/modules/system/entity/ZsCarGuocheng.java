package com.zs.create.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 车辆过程实体类
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ZsCarGuocheng implements Serializable {
    private static final long serialVersionUID = 1L;
    /*
     * id
     * */
    @TableId(type = IdType.UUID)
    private String id;

    /*
    * 车辆申请表ID
    * */
    private String carId;

    /*
    * 操作人ID
    * */
    private String userId;

    /*
    * 操作人姓名
    * */
    private String userName;

    /*
    * 状态
    * */
    private String state;

    /*
    * 操作时间
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /*
    * 审批意见
    * */
    private String approvalOpinion;

    /*
    * 操作名称
    * */
    private String playName;

    /*
     * 签名
     * */
    private String autograph;

    /*
    * 审核人
    * */
    private String approvalPerson;

    /*
    * 编号
    * */
    private String number;

    /*
    * 编号标识
    * */
    private String flag;

}
