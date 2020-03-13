package com.zs.create.modules.system.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 邮件组合实体
 * </p>
 * @Author yaochao
 * @since 2019-10-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ZsEmailVO {
    /**
     * id
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
     * send_status
     */
    private Integer sendStatus;

    /**
     * 0-未读,1-已读
     */
    private Integer readType;

    /**
     * 用户id
     */

    private String loginName;


    /**
     * 邮件内容
     */
    private String msgContent;

    /**
     * 邮件附件
     */
    private String annex;

    /**
     * 收件人id
     */
    private String ids;

    /**
     * 收件人id
     */
    private List<String> files;

}
