package com.zs.create.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 会议室信息实体类
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ZsBasicRoom implements Serializable {
    private static final long serialVersionUID = 1L;
    /*
     * id
     * */
    @TableId(type = IdType.UUID)
    private String id;

    /*
    * 申请人
    * */
    private String applyer;

    /*
    * 会议室编号
    * */
    private String roomNumber;

    /*
    * 会议室地址
    * */
    private String roomAct;

    /*
    * 容纳人数
    * */
    private String containNumber;

    /*
    * 会议室名称
    * */
    private String roomName;

    /*
    * 负责人id
    * */
    private String personId;

    /*
    * 备注
    * */
    private String remarks;

    /*
    *删除标识
    * */
    private String delFlag;

    /*
    * 创建人id
    * */
    private String createId;

    /*
    * 创建人真实姓名
    * */
    private String realname;

    /*
    * 创建时间
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /*
    * 修改人id
    * */
    private String updateId;

    /*
    * 修改时间
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    /*
    * 会议室状态
    * */
    private String status;

    /*
    * 所属部门
    * */
    private String dept;

    /*
    * 所属部门名称
    * */
    private String deptName;

    /*
    * 开始使用时间
    * */
    private Date startTime;

    /*
    * 使用结束时间
    * */
    private Date endTime;
}
