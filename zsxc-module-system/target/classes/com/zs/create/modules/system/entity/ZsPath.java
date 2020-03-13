package com.zs.create.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 模板路径表
 * </p>
 *
 * @Author yaochao
 * @since 2019-10-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("zs_path")
public class ZsPath implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 路径
     */
    private String path;

    /**
     * 类型
     * 0-公文模板上传,1-公文模板保存,2-通知模板保存,3-通知模板保存
     */
    private Integer type;

}
