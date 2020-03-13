package com.zs.create.modules.system.enums;
import com.baomidou.mybatisplus.annotation.EnumValue;
/**
 * 用戶表性別枚舉
 * @author lizt
 *
 */
public enum SysUserSexEnum {
	MAN("男", 1), WOMAN("女", 2);
	private SysUserSexEnum(String name, Integer code) {
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
