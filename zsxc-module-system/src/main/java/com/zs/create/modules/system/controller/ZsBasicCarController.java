package com.zs.create.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.base.query.QueryGenerator;
import com.zs.create.common.api.vo.Result;
import com.zs.create.common.aspect.annotation.AutoLog;
import com.zs.create.modules.system.entity.ZsBasicCar;
import com.zs.create.modules.system.mapper.ZsBasicCarMapper;
import com.zs.create.modules.system.service.ZsBasicCarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;


/**
 * @Author xiajunfeng
 * @since 2019-10-12
 */
@Slf4j
@Api(tags = "车辆信息管理")
@RestController
@RequestMapping("/car")

public class ZsBasicCarController {

    @Autowired
    private ZsBasicCarService zsBasicCarService;

    @Autowired
    private ZsBasicCarMapper zsBasicCarMapper;
    /*
    * 分页查询所有车辆信息
    * */
    @AutoLog(value = "分页列表查询")
    @ApiOperation(value = "分页列表查询", notes = "分页列表查询")
    @RequestMapping(value = "/queryzsbasiccarlist", method = RequestMethod.GET)
    public Result<IPage<ZsBasicCar>> queryVehicleList(ZsBasicCar zsBasicCar, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<ZsBasicCar>> result = new Result<>();
        zsBasicCar.setDelFlag("0");
        QueryWrapper<ZsBasicCar> queryWrapper = QueryGenerator.initQueryWrapper(zsBasicCar, req.getParameterMap());
        Page<ZsBasicCar> page = new Page<>(pageNo, pageSize);
        IPage<ZsBasicCar> pageList = zsBasicCarService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }


    /*
    * 车辆的添加
    * */
    @AutoLog(value = "车辆添加")
    @ApiOperation(value = "车辆添加", notes = "车辆添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<ZsBasicCar> add(@RequestBody ZsBasicCar zsBasicCar) {
        Result<ZsBasicCar> result = new Result<ZsBasicCar>();
        long vehicleNumber = zsBasicCarMapper.count(zsBasicCar.getVehicleNumber());
        if (vehicleNumber != 0) {
            result.error500("车辆编号重复");
            return result;
        }
        long plateNumber = zsBasicCarMapper.countPlateNumber(zsBasicCar.getPlateNumber());
        if (plateNumber != 0) {
            result.error500("车牌号重复");
            return result;
        }
        try {
            zsBasicCarService.add(zsBasicCar);
            result.success("添加成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /*
    * 车辆信息修改
    * */
    @AutoLog(value = "车辆信息修改")
    @ApiOperation(value = "车辆信息修改", notes = "车辆信息修改")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<ZsBasicCar> update(@RequestBody ZsBasicCar zsBasicCar) {
        Result<ZsBasicCar> result = new Result<ZsBasicCar>();
        ZsBasicCar zsBasicCar1 = zsBasicCarService.getById(zsBasicCar.getId());
        if (zsBasicCar1 == null) {
            result.error500("未找到对应实体");
        } else {
            try {
                zsBasicCar.setUpdateDate(new Date());
                zsBasicCarService.updateCar(zsBasicCar);
                result.success("修改成功");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }
        return result;
    }

    /*public Result<ZsBasicCar> update(@RequestBody JSONObject jsonObject){
        Result<ZsBasicCar> result = new Result<ZsBasicCar>();

        String url = jsonObject.getJSONObject("addr").getJSONObject("file").getJSONObject("response").getString("message");
        return result;
    }*/

    /*
    * 删除
    * */
    @AutoLog(value = "根据id删除")
    @ApiOperation(value = "根据id删除", notes = "根据id删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<ZsBasicCar> delete(@RequestParam(name = "id", required = true) String id) {
        Result<ZsBasicCar> result = new Result<ZsBasicCar>();
        ZsBasicCar zsBasicCar = zsBasicCarService.getById(id);
        if (zsBasicCar == null) {
            result.error500("未找到对应的实体类");
        } else {
            try {
                zsBasicCar.setDelFlag("1");
                zsBasicCarService.updateById(zsBasicCar);
                result.success("删除成功");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }
        return result;
    }

    /*
    * 批量删除
    * */
    @AutoLog(value = "批量删除")
    @ApiOperation(value = "批量删除", notes = "批量删除")
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<ZsBasicCar> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<ZsBasicCar> result = new Result<ZsBasicCar>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别");
        } else {
            String[] arrs = ids.split(",");
            try {
                for (int i = 0; i < arrs.length; i++) {
                    ZsBasicCar zsBasicCar = zsBasicCarService.getById(arrs[i]);
                    zsBasicCar.setDelFlag("1");
                    zsBasicCarService.updateById(zsBasicCar);
                }
                result.success("删除成功");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.error500("操作失败");
            }
        }
        return result;
    }

    /*
     * 查询空闲状态车辆
     * */
    @AutoLog(value = "查询空闲状态车辆")
    @ApiOperation(value = "查询空闲状态车辆", notes = "查询空闲状态车辆")
    @RequestMapping(value = "/findAllByStatus", method = RequestMethod.GET)
    public Result<List<ZsBasicCar>> findAllByStatus(@RequestParam(name = "outDate", required = false) String startTime,
                                                    @RequestParam(name = "inDate", required = false) String endTime) {
        Result<List<ZsBasicCar>> result = new Result<>();
        List<ZsBasicCar> list = zsBasicCarService.listCars(startTime,endTime);
        if (list.size() == 0) {
            result.error500("暂时未有空闲状态车辆");
        }
        result.setResult(list);
        result.setSuccess(true);
        return result;
    }


}
