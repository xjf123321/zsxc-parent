package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.ZsCarGuocheng;
import com.zs.create.modules.system.entity.ZsSqCar;
/**
 * <p>
 * 车辆过程表 service层
 * </p>
 * @Author yaochao
 * @since 2019-10-14
 */
public interface ZsCarGuochengService extends IService<ZsCarGuocheng> {
    void add(ZsSqCar zsSqCar);

    void addAndTx(ZsSqCar zsSqCar);

    void delete(ZsSqCar zsSqCar);
}
