package com.zs.create.modules.system.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 邮件查询对象
 * @Author yaochao
 * @since 2019-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class QueryEmail implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 邮件ID
     */
    private String id;

    /**
     * 发送人id
     */
    private String send;

    /**
     * 发送人姓名
     */
    private String sendName;

    /**
     * 收件人
     */
    private String receiver;

    /**
     * 记录表loginname
     */
    private String loginname;

    /**
     * 邮件主题
     */
    private String title;

    /**
     * 0-未发送,1-已发送
     */
    private Integer status;

    /**
     * 邮件表的del_flag的状态
     */
    private Integer eDel;

    /**
     * 记录表的del_flag的状态
     */
    private Integer rDel;

    /**
     * 发送的类型 0-正常,1-抄送,2-密送
     */
    private Integer state;

    /**
     * 邮件正文
     */
    private String msgContent;

}
