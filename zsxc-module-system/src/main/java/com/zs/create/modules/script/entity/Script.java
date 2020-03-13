package com.zs.create.modules.script.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Description 脚本实体类
 * @Author HeLiu
 * @Date 2019/9/5 9:48
 * @Version 1.0
 **/
@Data
@TableName("zs_script")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "script对象", description = "脚本对象")
public class Script {

    /**
     * 主键ID
     */
    private String id;
    /**
     * 脚本名称
     */
    private String scriptName;
    /**
     * 脚本表达式
     */
    private String scriptExpression;
    /**
     * 删除标记0-有效，1-已删除（默认值为 0 ）
     */
    private String delFlag = "0";
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
