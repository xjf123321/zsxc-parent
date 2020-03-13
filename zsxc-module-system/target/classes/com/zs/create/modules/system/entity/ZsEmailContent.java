package com.zs.create.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 邮件内容表
 * </p>
 *
 * @Author yaochao
 * @since 2019-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("zs_email_content")
public class ZsEmailContent implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 邮件表的id
     */
    private String emailId;

    /**
     * 邮件内容
     */
    private String msgContent;

    /**
     * 邮件附件
     */
    private String annex;

}
