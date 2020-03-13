package com.zs.create.modules.system.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 用户表状态枚举
 *
 * @author lizt
 */
public enum SysUserStatusEnum {
    NORMAL("正常", 1), FREEZE("冻结", 2);
    private String name;
    @EnumValue
    private Integer code;

    private SysUserStatusEnum(String name, Integer code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }


}

