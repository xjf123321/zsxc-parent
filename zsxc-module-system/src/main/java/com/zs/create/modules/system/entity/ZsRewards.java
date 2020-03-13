package com.zs.create.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ZsRewards implements Serializable {
    private static final long serialVersionUID = 1L;
    /*
     * 主键id
     * */
    @TableId(type = IdType.UUID)
    private String id;

    /*
     * 人员id
     * */
    private String userId;

    /*
     * 创建时间
     * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /*
     * 内容
     * */
    @ApiModelProperty(value = "内容")
    private String content;

    /*
     * 发生时间
     * */
    @ApiModelProperty(value = "发生时间")
    private String actionTime;

    /*
     * 创建人id
     * */
    private String creater;

    /*
     * 删除标记
     * */
    private String delFlag;
}
