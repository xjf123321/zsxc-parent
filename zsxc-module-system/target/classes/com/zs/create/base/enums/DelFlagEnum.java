package com.zs.create.base.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum DelFlagEnum {
    DEL("已刪", 1), NO_DEL("正常", 0);

    DelFlagEnum(String name, Integer code) {
        this.name = name;
        this.code = code;
    }

    private String name;
    @EnumValue
    private Integer code;

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
