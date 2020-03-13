package com.zs.create.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 发送记录表
 * </p>
 *
 * @Author yaochao
 * @since 2019-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("zs_email_recording")
public class ZsEmailRecording implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 发送种类 0-正常,1-抄送,2-密送
     */
    private Integer state;


    /**
     * 0-未读,1-已读
     */
    private Integer readType;

    /**
     * 0-正常,1-回收垃圾箱,2-删除 回收站里的删除是物理删除
     */
    private Integer delFlag;

    /**
     * 删除时间  用于后期定期删除回收站数据
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date delDate;

    /**
     * 邮件表id
     */
    private String emailId;

    /**
     * 用户id
     */
    @TableField("loginname")
    private String loginName;

}
