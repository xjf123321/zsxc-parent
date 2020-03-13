package com.zs.create.modules.system.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApprovalInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
    * 主键id
    * */
    private String id;


    /*
    * 流程名称
    * */
    private String name;

    /*
    * workid
    * */
    private String workId;

    /*
    * key
    * */
    private String key;
}
