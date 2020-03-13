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
 * 车辆信息实体类
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ZsBasicCar implements Serializable {
    private static final long serialVersionUID = 1L;
    /*
    * id
    * */
    @TableId(type = IdType.UUID)
    private String id;

    /*
    * 车辆编号
    * */
    private String vehicleNumber;

    /*
    * 车牌号
    * */
    private String plateNumber;

    /*
    * 车辆品牌
    * */
    private String brand;

    /*
    * 座位数
    * */
    private String seatNumber;

    /*
    * 所属部门
    * */
    private String vehicleDept;

    /*
    * 车辆类型
    * */
    private String vehicleType;

    /*
    * 车架号
    * */
    private String frameNumber;

    /*
    * 发动机号
    * */
    private String engineNumber;

    /*
    * 登记证号
    * */
    private String registrationNumber;

    /*
    * 年审日期
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date annualDate;

    /*
    * 工商年审日期
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date businessDate;

    /*
    * 车辆购置时间
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date buyDate;

    /*
    * 停放位置
    * */
    private String stopAct;

    /*
    * 车辆状态(0：正常 1：使用中 2：维修 3：报废 4：已派出)
    * */
    private String status;

    /*
    * 备注
    * */
    private String remarks;

    /*
    * 附件url
    * */
    private String url;

    /*
    * 创建人
    * */
    private String creater;

    /*
    * 创建人真实姓名
    * */
    private String realname;

    /*
    * 删除标识
    * */
    private String delFlag;

    /*
    * 创建时间
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /*
    * 修改时间
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    /*
    * 修改人
    * */
    private String updater;

    /*
    * 开始时间
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /*
    * 结束时间
    * */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

}
