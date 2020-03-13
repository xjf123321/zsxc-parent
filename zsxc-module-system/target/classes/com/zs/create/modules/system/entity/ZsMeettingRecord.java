package com.zs.create.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 会议记录实体类
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("zs_meetting_record")
public class ZsMeettingRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    /*
     * id
     * */
    @TableId(type = IdType.UUID)
    private String id;

    /*
    * 会议id
    * */
    private String meettingId;

    /*
    * 会议纪要
    * */
    private String meetingMinutes;

    /*
    * 附件url
    * */
    private String url;
}
