package com.zs.create.modules.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.config.Websocket.WebSocketServer;
import com.zs.create.modules.system.entity.*;
import com.zs.create.modules.system.mapper.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import com.zs.create.modules.system.service.ZsSqCarService;
import com.zs.create.modules.workflow.instance.service.IProcessInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * <p>
 * 车辆申请
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-15
 */
@Service
@Slf4j
public class ZsSqCarServiceImpl extends ServiceImpl<ZsSqCarMapper, ZsSqCar> implements ZsSqCarService {
    @Autowired
    private ZsSqCarMapper zsSqCarMapper;

    @Autowired
    private IProcessInstanceService processInstanceService;

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private ApprovalInformationMapper approvalInformationMapper;

    @Autowired
    private SysDepartMapper sysDepartMapper;

    @Autowired
    private SysUserDepartMapper sysUserDepartMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private ZsCarGuochengMapper zsCarGuochengMapper;

    @Autowired
    private ZsBasicCarMapper zsBasicCarMapper;


    @Override
    @Transactional
    public void add(ZsSqCar zsSqCar) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        String userId = sysUser.getId();
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(userId);   //当前登陆人部门id
        String deptType = sysDepartMapper.selectById(deptId).getDeptType();

        try {
            zsSqCar.setDelFlag("0");
            zsSqCar.setStatus("0");
            zsSqCar.setCreateTime(new Date());
            zsSqCar.setApplyType("0");
            zsSqCar.setUsername(sysUser.getRealname());
            zsSqCar.setUserId(sysUser.getId());
            zsSqCar.setApplyerDept(deptId);
            zsSqCarMapper.insert(zsSqCar);
            //车辆过程表中插数据
            ZsCarGuocheng zsCarGuocheng = new ZsCarGuocheng();
            zsCarGuocheng.setCarId(zsSqCar.getId());
            //zsCarGuocheng.setUserId(userId);
            zsCarGuocheng.setUserName(zsSqCar.getUsername());
            zsCarGuocheng.setState(zsSqCar.getStatus());
            zsCarGuocheng.setCreateTime(new Date());
            zsCarGuocheng.setPlayName("车辆申请提交");
            zsCarGuocheng.setApprovalOpinion(zsSqCar.getApprovalOpinion());
            zsCarGuochengMapper.insert(zsCarGuocheng);
            //修改车的状态
            String[] arr = zsSqCar.getPlateNumber().split(",");
            for (int i = 0; i < arr.length; i++) {
                zsBasicCarMapper.updateCarLock(arr[i], "5");
                //zsBasicCarMapper.updateTime(arr[i],zsSqCar.getOutDate(),zsSqCar.getInDate());
            }
        } catch (Exception e) {
            throw new Exception("SaveConferenceRoom Error");
        }
        //提醒信息
        List<String> usersId = sysUserDepartMapper.selectIdsByDeptId(deptId);
        for (String id: usersId) {
            try  {
                WebSocketServer.SendMessage("您有一条车辆申请待审批", id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ZsSqCar selectById(String id) throws Exception {
        ZsSqCar zsSqCar = zsSqCarMapper.selectById(id);
        return zsSqCar;
    }


    @Override
    public IPage<ZsSqCar> carApplyList(String emergencyLevel, IPage<ZsSqCar> page, String title, String username) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(sysUser.getId());   //当前登陆人部门id
        List<SysRole> roleList = sysRoleMapper.roleList(sysUser.getId());
        for (SysRole sysRole: roleList) {
            if (sysRole.getRoleCode().equals("BMJL")) {   //判断当前用户是否包含BMJL角色
                Long count = zsSqCarMapper.carApplyCount(deptId, emergencyLevel, title, username);
                String state = "0";     //领导待审批
                List<ZsSqCar> bpmnWorkIPage = zsSqCarMapper.carApplyList(page, deptId, emergencyLevel, state, title, username);
                for (ZsSqCar zsSqCar : bpmnWorkIPage) {
                    String plateNumber = "";
                    String[] carIds = zsSqCar.getPlateNumber().split(",");
                    for (String id : carIds) {
                        ZsBasicCar car = zsBasicCarMapper.selectById(id);
                        plateNumber += car.getPlateNumber() + " ";
                    }
                    zsSqCar.setPlateNumber(plateNumber);
                }
                page.setRecords(bpmnWorkIPage);
                page.setTotal(count);
                return page;
            }
        }
        Long count = new Long((page.getCurrent() - 1) * page.getSize());
        int atr = (int) ((page.getCurrent() - 1) * page.getSize());
        List<ZsSqCar> bpmnWorkIPage = new ArrayList<ZsSqCar>();
        List<SysDepart> depaerList = sysDepartMapper.listDeparts(sysUser.getId());
        if (depaerList.size() != 0) {
            for (SysDepart sysDepart:depaerList) {
                String state = "1";     //分领导待审批
                List<ZsSqCar> forBpmnWorkIPage = zsSqCarMapper.carApplyList(page, sysDepart.getId(), emergencyLevel, state, title, username);
                    if (forBpmnWorkIPage.size() != 0) {
                        for (ZsSqCar zsSqCar:forBpmnWorkIPage) {
                            //bpmnWorkIPage.add(atr++, zsSqCar);
                            String plateNumber = "";
                            String[] carIds = zsSqCar.getPlateNumber().split(",");
                            for (String id : carIds) {
                                ZsBasicCar car = zsBasicCarMapper.selectById(id);
                                plateNumber += car.getPlateNumber() + " ";
                            }
                            zsSqCar.setPlateNumber(plateNumber);
                            bpmnWorkIPage.add(zsSqCar);
                            count++;
                            if (bpmnWorkIPage.size() >= 10) {
                                page.setRecords(bpmnWorkIPage);
                                page.setTotal(count);
                                return page;
                            }
                        }
                    }
                }
            }
        page.setRecords(bpmnWorkIPage);
        page.setTotal(count);
        return page;
    }

    @Override
    public IPage<ZsSqCar> vehicleOfficerList(IPage<ZsSqCar> page, String applyer) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(sysUser.getId());   //当前登陆人部门id
        Long count = zsSqCarMapper.vehicleOfficerCount(applyer);
        List<ZsSqCar> bpmnWorkIPage = zsSqCarMapper.vehicleOfficerList(page, applyer);
        if (bpmnWorkIPage == null) {
            return page;
        }
        for (ZsSqCar zsSqCar : bpmnWorkIPage) {
            String plateNumber = "";
            String[] carIds = zsSqCar.getPlateNumber().split(",");
            for (String id : carIds) {
                ZsBasicCar car = zsBasicCarMapper.selectById(id);
                plateNumber += car.getPlateNumber() + " ";
            }
            zsSqCar.setPlateNumber(plateNumber);
        }
        page.setRecords(bpmnWorkIPage);
        page.setTotal(count);
        return page;
    }

    @Override
    @Transactional
    public void updateStatus(ZsSqCar zsSqCar) {
        zsSqCarMapper.updateStatus(zsSqCar.getId(), zsSqCar.getStatus());
    }

    @Override
    public Map formShow(String id) {
        Map map = new HashMap();
        ZsSqCar zsSqCar = zsSqCarMapper.formShow(id);
        zsSqCar.setUseDept(sysDepartMapper.selectDeptName(zsSqCar.getUseDept()));
        String[] split = zsSqCar.getPlateNumber().split(",");
        String plateNumber = "";
        for (int i = 0; i < split.length; i++) {
            plateNumber += zsBasicCarMapper.selectPlateNumber(split[i]) + " ";
        }
        zsSqCar.setPlateNumber(plateNumber);
        List<ZsCarGuocheng> zsCarGuochengList = zsCarGuochengMapper.formShow(id);
        map.put("formData", zsSqCar);
        map.put("processList", zsCarGuochengList);
        return map;
    }

    @Override
    public IPage<ZsSqCar> haveDoneList(IPage<ZsSqCar> page, String username) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(sysUser.getId());   //当前登陆人部门id
        Long count = zsSqCarMapper.haveDoneCount(sysUser.getId(), username);
        List<ZsSqCar> bpmnWorkIPage = zsSqCarMapper.haveDoneList(page, sysUser.getId(), username);
        for (ZsSqCar zsSqCar : bpmnWorkIPage) {
            String plateNumber = "";
            String[] carIds = zsSqCar.getPlateNumber().split(",");
            for (String id : carIds) {
                ZsBasicCar car = zsBasicCarMapper.selectById(id);
                if (car != null) {
                    plateNumber += car.getPlateNumber() + " ";
                }
            }
            zsSqCar.setPlateNumber(plateNumber);
        }
        page.setRecords(bpmnWorkIPage);
        page.setTotal(count);
        return page;
    }

    @Override
    public void updateCarStatus(String plateNumber) {
        if (plateNumber.contains(",")) {
            String[] arr = plateNumber.split(",");
            for (int i = 0; i < arr.length; i++) {
                zsBasicCarMapper.updateCarStatus(arr[i], "1");
            }
        } else {
                zsBasicCarMapper.updateCarStatus(plateNumber, "1");
        }
    }

    @Override
    @Transactional
    public void updateCarStatusAndInDate(String id) {
        String plateNumber = zsSqCarMapper.selectPlateNumber(id);
        String arr1[] = plateNumber.split(",");
        List<ZsSqCar> zsSqCarList = zsSqCarMapper.selectAll();
        String status = "0";
        for (int i = 0; i < zsSqCarList.size(); i++) {
            if (zsSqCarList.get(i).getId().equals(id)) {
                zsSqCarList.remove(i);
                i--;
            }
        }
        for (int i = 0; i < arr1.length; i++) {
            for (ZsSqCar zsSqCar:zsSqCarList) {
                String arr[] = zsSqCar.getPlateNumber().split(",");
                for (int j = 0; j < arr.length; j++) {
                    if (arr1[i].equals(arr[j])){
                        if (zsSqCar.getStatus().equals("0")){
                            status = "5";
                            break;
                        }else if (zsSqCar.getStatus().equals("1")){
                            status = "4";
                            break;
                        }else if (zsSqCar.getStatus().equals("3")) {
                            status = "1";
                            break;
                        }
                    }
                }
                if (!status.equals("0")){
                    break;
                }
            }
            zsBasicCarMapper.updateCarStatus(arr1[i], status);
            zsSqCarMapper.updateInDate(new Date(), id, "4");
        }

        /*if (zsSqCar.getStatus().equals("0")){
            status = "5";
            break;
        }else if (zsSqCar.getStatus().equals("1")){
            status = "4";
            break;
        }else if (zsSqCar.getStatus().equals("3")) {
            status = "1";
            break;
        }*/


        /*if (plateNumber.contains(",")) {
            String[] arr = plateNumber.split(",");
            for (int i = 0; i < arr.length; i++) {

                zsBasicCarMapper.updateCarStatus(arr[i], "0");
                zsBasicCarMapper.cleanTime(arr[i]);
            }
        } else {
            zsBasicCarMapper.updateCarStatus(plateNumber, "0");
            zsBasicCarMapper.cleanTime(plateNumber);
        }
        zsSqCarMapper.updateInDate(new Date(), id, "4");*/
    }

    @Override
    public String[] selectPlateNumber(ZsSqCar zsSqCar) {
        String[] arr = zsSqCarMapper.selectPlateNumber(zsSqCar.getId()).split(",");
        return arr;
    }

    @Override
    public ZsSqCar selectCar(String id) {
        ZsSqCar zsSqCar = zsSqCarMapper.selectCar(id);
        return zsSqCar;
    }

    @Override
    public ZsSqCar findById(String id) {
        ZsSqCar zsSqCar = zsSqCarMapper.findById(id);
        return zsSqCar;
    }

    @Override
    @Transactional
    public void updateFlag(ZsSqCar zsSqCar) {
        zsSqCarMapper.updateFlag(zsSqCar.getId());
    }

    @Override
    public String role() {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        List<SysRole> roleList = sysRoleMapper.roleList(sysUser.getId());
        String flag = "0";
        for (int i = 0; i < roleList.size(); i++) {
            if (roleList.get(i).getRoleCode().equals("CLGLY")) {
                flag = "1";
            }
        }
        return flag;
    }

    @Override
    public Map select(String id) {
        Map map = new HashMap();
        ZsSqCar zsSqCar = zsSqCarMapper.selectCar(id);
        map.put("formData", zsSqCar);
        return map;
    }
}
