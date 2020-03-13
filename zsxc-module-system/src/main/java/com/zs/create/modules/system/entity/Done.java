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
public class Done implements Serializable {

    /*
    * 申请单id
    * */
    private String id;

    /*
    * 申请单状态
    * */
    private String status;

    /*
    * 申请人
    * */
    private String applyer;

    /*
    * 申请单类型
    * */
    private String type;

    /*
    * 创建时间
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /*
    * 标题
    * */
    private String title;
}
