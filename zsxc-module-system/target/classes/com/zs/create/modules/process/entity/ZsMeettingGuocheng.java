package com.zs.create.modules.process.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 会议室申请记录实体类
 * </p>
 *
 * @Author yaochao
 * @since 2019-11-4
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("zs_meetting_guocheng")
public class ZsMeettingGuocheng implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 会议申请表id
     */
    private String meettingId;

    /**
     * 操作人id
     */
    private String userId;

    /**
     * 操作人姓名
     */
    private String userName;

    /**
     * 状态
     */
    private String state;

    /**
     * 操作时间
     */
    private Date createTime;

    /**
     * 审批意见
     */
    private String approvalOpinion;

    /**
     * 操作名称
     */
    private String playName;

    /*
     * 签名
     * */
    private String autograph;
}
