package com.zs.create.modules.script.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description 控件默认值数据传输对象
 * @Author HeLiu
 * @Date 2019/9/9 10:38
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ControlDefaultValueDto {

    private String label;

    private String value;

    private String param1;

    private String param2;
}
