package com.zs.create.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 邮件表
 * </p>
 *
 * @Author yaochao
 * @since 2019-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("zs_email")
public class ZsEmail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.UUID)
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
     * 抄送人
     */
    private String copyer;

    /**
     * 密送人
     */
    private String secret;

    /**
     * 邮件主题
     */
    private String title;

    /**
     * 0-未发送,1-已发送
     */
    private Integer status;

    /**
     * 内容的前50个字
     */
    private String description;

    /**
     * 邮件新增时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 草稿箱/垃圾箱保存时间(草稿最后一次保存时间)
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date saveTime;

    /**
     * 邮件发送类型 0-正常，1-回复
     */
    private Integer type;

    /**
     * 0-正常,1-回收垃圾箱,2-删除
     */
    private Integer delFlag;

    /**
     * email_id
     */
    private String emailId;

    /**
     * send_status
     */
    private int sendStatus;

    /**
     * ids
     */
    private String ids;




}
