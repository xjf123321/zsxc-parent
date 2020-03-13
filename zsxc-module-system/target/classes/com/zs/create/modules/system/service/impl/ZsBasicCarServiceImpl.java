package com.zs.create.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.modules.system.entity.ZsBasicCar;
import com.zs.create.modules.system.entity.ZsSqCar;
import com.zs.create.modules.system.mapper.ZsBasicCarMapper;
import com.zs.create.modules.system.mapper.ZsSqCarMapper;
import com.zs.create.modules.system.service.ZsBasicCarService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 车辆基础信息 service层实现
 * </p>
 * @Author xiajunfeng
 * @since 2019-10-14
 */
@Service
@Slf4j
public class ZsBasicCarServiceImpl extends ServiceImpl<ZsBasicCarMapper, ZsBasicCar> implements ZsBasicCarService {

    @Autowired
    private ZsBasicCarMapper zsBasicCarMapper;

    @Autowired
    private ZsSqCarMapper zsSqCarMapper;

    @Override
    @Transactional
    public void add(ZsBasicCar zsBasicCar) throws Exception {
        try {
            zsBasicCar.setDelFlag("0");
            zsBasicCar.setCreateTime(new Date());
            Subject subject = SecurityUtils.getSubject();
            LoginUser sysUser = (LoginUser) subject.getPrincipal();
            zsBasicCar.setCreater(sysUser.getUsername());
            zsBasicCar.setRealname(sysUser.getRealname());
            zsBasicCarMapper.insert(zsBasicCar);
        } catch (Exception e) {
            throw new Exception("SaveVehicle Error");
        }
    }

    @Override
        public Map<String, Integer> status() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        List<ZsBasicCar> zsBasicCars = zsBasicCarMapper.countStatus();
        int count0 = 0;
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int count4 = 0;
        for (int i = 0; i < zsBasicCars.size(); i++) {
            String status = zsBasicCars.get(i).getStatus();
            if (status.equals("0")) {
                count0++;
            } else if (status.equals("1")) {
                count1++;
            } else if (status.equals("2")) {
                count2++;
            } else if (status.equals("3")) {
                count3++;
            } else if (status.equals("4")) {
                count4++;
            }
        }
        map.put("total", zsBasicCars.size());
        map.put("free", count0);
        map.put("use", count1);
        map.put("repair", count2);
        map.put("scrap", count3);
        map.put("noDispatch", count4);
        return map;
    }

    @Override
    @Transactional
    public void updateCarStatus(ZsSqCar zsSqCar) {
        String[] arr = zsSqCar.getPlateNumber().split(",");
        for (int i = 0; i < arr.length; i++) {
            zsBasicCarMapper.updateCarStatus(arr[i], "1");
        }

    }

    @Override
    @Transactional
    public void updateStatus(String[] arr, String status) {
        for (int i = 0; i < arr.length; i++) {
            zsBasicCarMapper.updateCarStatus(arr[i], status);
        }
    }

    @Override
    public List<ZsBasicCar> listCars(String startTime, String endTime) {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        List<ZsSqCar> list = zsSqCarMapper.select();
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).getStatus().equals("0")) {
                Date startTime1 = list.get(i).getOutDate();
                Date endTime1 = list.get(i).getInDate();
                if (startTime1 != null && endTime1 != null) {
                    try {
                        int date1 = formatter.parse(startTime).compareTo(endTime1);
                        int date2 = formatter.parse(endTime).compareTo(startTime1);
                        if (date1 == 1 || date2 == -1) {
                            list.remove(i);
                            i--;
                        } else {
                            continue;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Set<String> numberSet = new HashSet<>();
        for (int i = 0; i < list.size(); i++) {
            String arr[] = list.get(i).getPlateNumber().split(",");
            for (int j = 0; j < arr.length; j++) {
                numberSet.add(arr[j]);
            }
        }
        List<String> stringList = zsBasicCarMapper.selectAll();
        for (int i = 0; i < stringList.size(); i++) {
            for (String number:numberSet) {
                if (stringList.get(i).equals(number)){
                    stringList.remove(i);
                    i--;
                }
            }
        }
        List<ZsBasicCar> zsBasicCarList = new ArrayList<>();
        for (String number:stringList) {
            ZsBasicCar zsBasicCar = zsBasicCarMapper.selectById(number);
            zsBasicCarList.add(zsBasicCar);
        }
            for (ZsBasicCar car: zsBasicCarList) {
                if ("0".equals(car.getVehicleType())) {
                    car.setVehicleType("商务车");
                } else if ("1".equals(car.getVehicleType())) {
                    car.setVehicleType("轿车");
                } else if ("2".equals(car.getVehicleType())) {
                    car.setVehicleType("客车");
                } else if ("3".equals(car.getVehicleType())) {
                    car.setVehicleType("卡车");
                } else if ("4".equals(car.getVehicleType())) {
                    car.setVehicleType("卫星车");
                } else if ("5".equals(car.getVehicleType())) {
                    car.setVehicleType("指挥车");
                } else if ("6".equals(car.getVehicleType())) {
                    car.setVehicleType("防爆车");
                } else if ("7".equals(car.getVehicleType())) {
                    car.setVehicleType("其他");
                }
                car.setPlateNumber(car.getPlateNumber() + "/座位数" + car.getSeatNumber() + "/" + car.getVehicleType());
            }
        return zsBasicCarList;
    }

    @Override
    @Transactional
    public void updateState(ZsSqCar zsSqCar) {
        String plateNumber = zsSqCarMapper.selectPlateNumber(zsSqCar.getId());
        String[] arr = plateNumber.split(",");
        for (int i = 0; i < arr.length; i++) {
            zsBasicCarMapper.updateCarLock(arr[i], "0");
        }
    }

    @Override
    @Transactional
    public void updateCar(ZsBasicCar zsBasicCar) {
        List<ZsBasicCar> list = new ArrayList<>();
        list.add(zsBasicCar);
        zsBasicCarMapper.updateCar(list);
    }

    @Override
    @Transactional
    public void updateBasicCar(ZsSqCar zsSqCar) {
        String[] arr = zsSqCar.getPlateNumber().split(",");
        for (int i = 0; i < arr.length; i++) {
            zsBasicCarMapper.updateBasicCar(arr[i],null,null);
        }
    }
}
