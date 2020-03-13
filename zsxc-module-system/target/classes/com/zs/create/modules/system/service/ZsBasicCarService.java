package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.ZsBasicCar;
import com.zs.create.modules.system.entity.ZsSqCar;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 车辆信息表 服务类
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-10
 */
public interface ZsBasicCarService extends IService<ZsBasicCar> {

    void add(ZsBasicCar zsBasicCar) throws Exception;

    Map<String, Integer> status();

    void updateCarStatus(ZsSqCar zsSqCar);

    void updateStatus(String[] arr, String status);

    List<ZsBasicCar> listCars(String startTime, String endTime);

    void updateState(ZsSqCar zsSqCar);

    void updateCar(ZsBasicCar zsBasicCar);

    void updateBasicCar(ZsSqCar zsSqCar);
}
