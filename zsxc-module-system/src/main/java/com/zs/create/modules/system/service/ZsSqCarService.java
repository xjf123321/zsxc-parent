package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.ZsSqCar;

import java.util.Map;

/**
 * <p>
 * 车辆申请
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-15
 */
public interface ZsSqCarService extends IService<ZsSqCar> {

    void add(ZsSqCar zsSqCar) throws Exception;

    ZsSqCar selectById(String id) throws Exception;

    /**
     * 个人事务车辆审批列表
     *
     * @param emergencyLevel
     * @param page
     * @return
     */
    IPage<ZsSqCar> carApplyList(String emergencyLevel, IPage<ZsSqCar> page, String title, String username);

    /**
     * 车辆管理审核列表
     * @param page
     * @return
     */
    IPage<ZsSqCar> vehicleOfficerList(IPage<ZsSqCar> page, String applyer);

    /**
     * 修改状态
     * @param zsSqCar
     */
    void updateStatus(ZsSqCar zsSqCar);

    /**
     * 根据id查询表单和过程
     * @param id
     * @return
     */
    Map formShow(String id);

    /**
     * 车辆已办事项列表
     * @param page
     * @return
     */
    IPage<ZsSqCar> haveDoneList(IPage<ZsSqCar> page, String username);

    void updateCarStatus(String plateNumber);

    void updateCarStatusAndInDate(String id);

    String[] selectPlateNumber(ZsSqCar zsSqCar);

    ZsSqCar selectCar(String id);

    ZsSqCar findById(String id);

    void updateFlag(ZsSqCar zsSqCar);

    String role();

    Map select(String id);
}
