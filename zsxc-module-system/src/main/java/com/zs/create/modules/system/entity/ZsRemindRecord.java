package com.zs.create.modules.system.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 申请实体类
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-11-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ZsRemindRecord {

    private static final long serialVersionUID = 1L;

    /*
    * 主键
    * */
    private String id;

    /*
    *提醒标题
    * */
    private String txtitle;

    /*
    * 提醒内容
    * */
    private String content;

    /*
    * 提醒对象(id)
    * */
    private String txdx;

    /*
    * 提醒时间
    * */

    private Date txsj;

    /*
    * 提醒类型
    * */
    private String txlx;

    /*
    * 当前操作人(id)
    * */
    private String operator;

    /*
    * 备注
    * */
    private String bz;
}
