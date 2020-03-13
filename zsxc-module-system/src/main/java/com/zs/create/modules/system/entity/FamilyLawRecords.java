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
public class FamilyLawRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
    * 主键id
    * */
    private String id;

    /*
    * 用户id
    * */
    private String userId;

    /*
    * 军属姓名
    * */
    private String name;


    /*
    * 涉法时间
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date familySfsj;

    /*
    * 涉法说明
    * */
    private String familyRemarks;


}
