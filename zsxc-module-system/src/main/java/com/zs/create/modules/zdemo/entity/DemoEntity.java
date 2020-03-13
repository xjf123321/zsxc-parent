package com.zs.create.modules.zdemo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 测试
 *
 * @author heliu
 * @email heliu@zs-create.com
 * @date 2019-08-30 10:23:20
 */
@Data
@TableName("demo")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "demo对象", description = "测试")
public class DemoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
    private String id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 关键词
     */
    private String keyWord;
    /**
     * 打卡时间
     */
    private Date punchTime;
    /**
     * 工资
     */
    private BigDecimal salaryMoney;
    /**
     * 奖金
     */
    private Double bonusMoney;
    /**
     * 性别 {男:1,女:2}
     */
    private String sex;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 个人简介
     */
    private String content;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 所属部门编码
     */
    private String sysOrgCode;

}


