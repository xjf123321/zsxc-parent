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
 * 公文模板表
 * </p>
 *
 * @Author yaochao
 * @since 2019-10-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("zs_doc_template")
public class ZsDocTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 模板保存的组名
     */
    private String groupName;

    /**
     * 模板保存的文件名
     */
    @TableField("remote_filename")
    private String remoteFileName;

    /**
     * 模板保存的地址
     */
    private String addr;

    /**
     * 删除标记 0-正常,1-删除
     */
    private Integer delFlag;

    /**
     * 模板保存的时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 文件名
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 创建人
     */
    private String createName;

    /**
     * 模板类型
     *0-普通模板,1-红头模板,2-其他
     */
    private Integer type;

}
