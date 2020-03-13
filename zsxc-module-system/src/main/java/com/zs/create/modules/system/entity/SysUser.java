package com.zs.create.modules.system.entity;

import java.io.Serializable;
import java.util.Date;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zs.create.base.enums.DelFlagEnum;
import com.zs.create.modules.system.enums.SysUserStatusEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @Author lingrui
 * @since 2018-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 登录账号
     */
    @Excel(name = "登录账号", width = 15)
    private String username;

    /**
     * 真实姓名
     */
    @Excel(name = "真实姓名", width = 15)
    private String realname;

    /**
     * 密码
     */
    private String password;

    /**
     * md5密码盐
     */
    private String salt;

    /**
     * 头像
     */
    @Excel(name = "头像", width = 15)
    private String avatar;

    /**
     * 生日
     */
    @Excel(name = "生日", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 性别（1：男 2：女）
     */
    //@Excel(name = "性别", width = 15, dicCode = "sex")
    private String sex;

    /**
     * 电子邮件
     */
    @Excel(name = "电子邮件", width = 15)
    private String email;

    /**
     * 电话
     */
    @Excel(name = "电话", width = 15)
    private String phone;

    /**
     * 民族（1：汉族2：满族）
     */
    private Integer national;

    /**
     * 籍贯
     */
    private String nativePlace;

    /**
     * 文化程度（1：博士2：硕士3：本科4：大专5：高中6：中专7：初中）
     */
    private Integer culture;

    /**
     * 学位
     */
    private Integer degree;

    /**
     * 现部职别
     */
    private String currentPosition;

    /**
     * 入伍时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date militaryTime;

    /**
     * 人员分类（1：警官）
     */
    private Integer userClassify;

    /**
     * 干部警衔（1：少尉2：中尉3：上尉4：少校5：中校6：上校7：大校8：少将9：中将10：上将）
     */
    private Integer title;

    /**
     * 警衔时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date titleTime;

    /**
     * 职级（1：正连）
     */
    private Integer rank;

    /**
     * 职级时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date rankTime;

    /**
     * 部门code
     */
    private String orgCode;

    /**
     * 状态(1：正常  2：冻结 ）
     */
    //@Excel(name = "状态", width = 15, dicCode = "user_status")
    private SysUserStatusEnum status;

    /**
     * 删除状态（0，正常，1已删除）
     */
    //@Excel(name = "删除状态", width = 15, dicCode = "del_flag")
    @TableLogic
    private DelFlagEnum delFlag;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 同步工作流引擎1同步0不同步
     */
    private String activitiSync;

    /**
     * 人员状态 0：在位 1：事假 2：病假
     */
    /*private Integer personnelStatus;*/


    /*
    * 年度请假天数
    * */
    private String leaveDayAll;

    /*
    * 已经休假天数
    * */
    private String leaveDayUsed;

    /*
    * 剩余天数
    * */
    private String leaveDaySurplus;


}
