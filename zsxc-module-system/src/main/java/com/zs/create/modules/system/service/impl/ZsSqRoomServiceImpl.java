package com.zs.create.modules.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.modules.process.entity.ZsMeettingGuocheng;
import com.zs.create.modules.process.mapper.ZsMeettingGuochengMapper;
import com.zs.create.modules.system.entity.*;
import com.zs.create.modules.system.mapper.*;
import com.zs.create.modules.system.service.ZsSqRoomService;
import com.zs.create.modules.system.vo.ZsSqRoomVo;
import com.zs.create.modules.workflow.instance.service.IProcessInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * <p>
 * 会议室申请
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-16
 */
@Service
@Slf4j
public class ZsSqRoomServiceImpl extends ServiceImpl<ZsSqRoomMapper, ZsSqRoom> implements ZsSqRoomService {

    @Autowired
    private ZsSqRoomMapper zsSqRoomMapper;

    @Autowired
    private IProcessInstanceService processInstanceService;

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private ApprovalInformationMapper approvalInformationMapper;

    @Autowired
    private SysUserDepartMapper sysUserDepartMapper;

    @Autowired
    private SysDepartMapper sysDepartMapper;

    @Autowired
    private ZsMeettingGuochengMapper zsMeettingGuochengMapper;


    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private ZsBasicRoomMapper zsBasicRoomMapper;

    @Override
    @Transactional
    public ZsMeettingGuocheng add(ZsSqRoom zsSqRoom) throws Exception {
        try {
            zsSqRoom.setDelFlag("0");
            zsSqRoom.setStatus("0");
            zsSqRoom.setCreateTime(new Date());
            zsSqRoom.setApplyType("1");
            Subject subject = SecurityUtils.getSubject();
            LoginUser sysUser = (LoginUser) subject.getPrincipal();
            String userId = sysUser.getId();
            String deptId = sysUserDepartMapper.selectDeptIdByUserId(userId);   //当前登陆人部门id
            zsSqRoom.setUsername(sysUser.getRealname());
            zsSqRoom.setUserId(sysUser.getId());
            zsSqRoom.setApplyerDept(deptId);
            zsSqRoomMapper.insert(zsSqRoom);
            //修改会议室的状态
            zsBasicRoomMapper.updateMeettingStatus(zsSqRoom.getMeettingRoomName(), "2");
            zsBasicRoomMapper.updateTime(zsSqRoom.getMeettingRoomName(),zsSqRoom.getMeettingStart(),zsSqRoom.getMeettingEnd());
            //将数据插入过程表
            ZsMeettingGuocheng zsMeettingProcess = new ZsMeettingGuocheng();
            zsMeettingProcess.setMeettingId(zsSqRoom.getId());
            zsMeettingProcess.setCreateTime(new Date());
            //zsMeettingProcess.setUserId(sysUser.getId());
            zsMeettingProcess.setUserName(sysUser.getRealname());
            zsMeettingProcess.setState("0"); //未审批
            zsMeettingProcess.setPlayName("会议申请提交");
            return zsMeettingProcess;
        } catch (Exception e) {
            throw new Exception("SavezsSqRoom Error");
        }
    }

    @Override
    public ZsSqRoom select(String id) throws Exception {
        ZsSqRoom zsSqRoom = zsSqRoomMapper.selectById(id);
        ZsBasicRoom  zsBasicRoom = zsBasicRoomMapper.selectById(zsSqRoom.getMeettingRoomName());
        zsSqRoom.setMeettingRoomName(zsBasicRoom.getRoomName());
        return zsSqRoom;
    }

    /**
     * 个人事务会议审批列表
     *
     * @param emergencyLevel
     * @param page
     * @return
     */
    @Override
    public IPage<ZsSqRoom> meettingApplyList(String emergencyLevel, IPage<ZsSqRoom> page, String title, String username) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(sysUser.getId()); //当前登陆人部门id
        List<SysRole> roleList = sysRoleMapper.roleList(sysUser.getId());
        for (SysRole sysRole : roleList) {
            if (sysRole.getRoleCode().equals("BMJL")) {
                //判断当前用户是否包含BMJL角色
                String state = "0";     //领导待审批
                Long count = zsSqRoomMapper.meettingApplyCount(deptId, state, emergencyLevel, title, username);
                List<ZsSqRoom> sqRoomWorkIPage = zsSqRoomMapper.meettingApplyList(page, deptId, state, emergencyLevel, title, username);
                page.setRecords(sqRoomWorkIPage);
                page.setTotal(count);
                return page;
            } else if (sysRole.getRoleCode().equals("MAdmin")) {
                //判断当前用户是否包含MAdmin角色
                String state = "1";     //会议管理员待审批
                Long count = zsSqRoomMapper.meettingApplyCount(deptId, state, emergencyLevel, title, username);
                List<ZsSqRoom> sqRoomWorkIPage = zsSqRoomMapper.meettingApplyList(page, deptId, state, emergencyLevel, title, username);
                page.setRecords(sqRoomWorkIPage);
                page.setTotal(count);
                return page;
            }
        }
        return page;
    }

    /**
     * 根据id查询审批单信息(会议)
     *
     * @param id
     * @return
     */
    @Override
    public Map formShow(String id) {
        Map map = new HashMap();
        ZsSqRoom zsSqRoom = zsSqRoomMapper.formShow(id);
        List<ZsMeettingGuocheng> meettingProcessList = zsMeettingGuochengMapper.formShow(id);
        map.put("formData", zsSqRoom);
        map.put("processList", meettingProcessList);
        return map;
    }

    /**
     * 会议室管理审核列表
     *
     * @param page
     * @return
     */
    @Override
    public IPage<ZsSqRoom> roomAdminList(IPage<ZsSqRoom> page, String username) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(sysUser.getId());   //当前登陆人部门id
        Long count = zsSqRoomMapper.roomAdminCount(username);
        List<ZsSqRoom> zsSqRoomList = zsSqRoomMapper.roomAdminList(page, username);
        for (int i = 0; i < zsSqRoomList.size(); i++) {
            String id = zsSqRoomList.get(i).getMeettingRoomName();
            String roomName = zsBasicRoomMapper.selectRoomName(id);
            zsSqRoomList.get(i).setMeettingName(roomName);
        }
        page.setRecords(zsSqRoomList);
        page.setTotal(count);
        return page;
    }


    @Override
    public IPage<ZsSqRoom> allowList(String realname, Page<ZsSqRoom> page, String username) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(sysUser.getId()); //当前登陆人部门id
        Long count = zsSqRoomMapper.allowListCount(sysUser.getId(), username);
        List<ZsSqRoom> zsSqRoomList = zsSqRoomMapper.allowList(page, sysUser.getId(), username);
        page.setRecords(zsSqRoomList);
        page.setTotal(count);
        return page;
    }

    @Override
    @Transactional
    public void updateMeettingStatus(ZsSqRoom zsSqRoom) {
        String id = zsSqRoom.getId();
        zsBasicRoomMapper.updateMeettingStatus(zsSqRoomMapper.selectRoomName(id), "1");
    }

    @Override
    @Transactional
    public void updateMeettingStatusAndMeettingEnd(String meettingRoomName, String id) {
        zsBasicRoomMapper.updateMeettingStatus(meettingRoomName, "0");
        zsSqRoomMapper.updateMeettingEnd(new Date(), id);
    }

    @Override
    @Transactional
    public void cancel(String id) {
        zsSqRoomMapper.cancel(id);
        String roomName = zsSqRoomMapper.selectRoomName(id);
        zsBasicRoomMapper.cancel(roomName);
    }

    @Override
    public Map<String, Object> queryCollectList(ZsSqRoomVo zsSqRoomVo, Integer pageNo, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        String userId = sysUser.getId();
        Long total = zsSqRoomMapper.count(zsSqRoomVo.getMeettingHost());
        List<ZsSqRoomVo> list = null;
        if (total != null && total > 0) {
            //从第几条数据开始
            int firstIndex = (pageNo - 1) * pageSize;
            //到第几条数据结束
            int lastIndex = pageNo * pageSize;
            list = zsSqRoomMapper.queryCollectList(zsSqRoomVo.getMeettingHost(), firstIndex, lastIndex);
            for (int i = 0; i < list.size(); i++) {
                String roomName = zsBasicRoomMapper.selectById(list.get(i).getMeettingRoomName()).getRoomName();
                list.get(i).setMeettingRoomName(roomName);
                if (list.get(i).getUrl() != null || list.get(i).getUrl() == ""){
                    String[] arr = list.get(i).getUrl().split(",");
                    if (arr.length != 0){
                        List<String> list1 = new ArrayList<>();
                        for (int j = 0; j < arr.length; j++) {
                            list1.add(arr[j]);
                        }
                        list.get(i).setFiles(list1);
                    }
                }
            }
        }
        map.put("total", total);
        map.put("list", list);
        return map;
    }


    @Override
    public void updateZsSqStatus(ZsSqRoom zsSqRoom) {
        zsSqRoomMapper.updateById(zsSqRoom);
    }

    @Override
    public String role() {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        List<SysRole> roleList = sysRoleMapper.roleList(sysUser.getId());
        String flag = "0";
        for (int i = 0; i < roleList.size(); i++) {
            if (roleList.get(i).getRoleCode().equals("MAdmin")) {
                flag = "1";
            }
        }
        return flag;
    }

    @Override
    public Map selectRoom(String id) {
        Map map = new HashMap();
        ZsSqRoomVo zsSqRoomVo = zsSqRoomMapper.findById(id);
        ZsBasicRoom zsBasicRoom = zsBasicRoomMapper.selectById(zsSqRoomVo.getMeettingRoomName());
        zsSqRoomVo.setMeettingRoomName(zsBasicRoom.getRoomName());
        String[] arr = zsSqRoomVo.getUrl().split(",");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            list.add(arr[i]);
        }
        zsSqRoomVo.setFiles(list);
        map.put("formData",zsSqRoomVo);
        return map;
    }

    @Override
    @Transactional
    public void deleteAttachment(String id) {
        zsSqRoomMapper.deleteAttachment(id);
    }

    @Override
    @Transactional
    public void updateRoom(ZsSqRoom zsSqRoom) {
        String roomId = zsBasicRoomMapper.selectRoomId(zsSqRoom.getMeettingRoomName());
        zsSqRoom.setMeettingRoomName(roomId);
        zsSqRoomMapper.updateById(zsSqRoom);
    }

    @Override
    @Transactional
    public void updateStatus(ZsSqRoom zsSqRoom, String status) {
        String id = zsSqRoomMapper.selectById(zsSqRoom.getId()).getMeettingRoomName();
        zsBasicRoomMapper.updateMeettingStatus(id, status);
        zsSqRoomMapper.updateStatus(zsSqRoom.getId(),zsSqRoom.getStatus());
    }
}
