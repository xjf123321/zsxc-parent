package com.zs.create.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.zs.create.common.constant.CommonConstant;
import com.zs.create.common.constant.CommonSendStatus;
import com.zs.create.common.system.vo.LoginUser;
import com.zs.create.config.Websocket.WebSocketServer;
import com.zs.create.config.rtxconf.RTXConfig;
import com.zs.create.modules.system.entity.*;
import com.zs.create.modules.system.mapper.*;
import com.zs.create.modules.system.service.ISysAnnouncementService;
import com.zs.create.modules.system.service.NoticeProcessService;
import com.zs.create.modules.system.service.ZsRemindRecordService;
import com.zs.create.modules.system.service.ZsSqNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 公告
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-28
 */
@Service
@Slf4j
public class ZsSqNoticeServiceImpl extends ServiceImpl<ZsSqNoticeMapper, ZsSqNotice> implements ZsSqNoticeService {

    @Autowired
    private ZsSqNoticeMapper zsSqNoticeMapper;

    @Autowired
    private NoticeProcessMapper noticeProcessMapper;

    @Autowired
    private SysUserDepartMapper sysUserDepartMapper;

    @Autowired
    private NoticeProcessService noticeProcessService;

    @Autowired
    private ZsRemindRecordService zsRemindRecordService;

    @Autowired
    private ZsReturnPathMapper zsReturnPathMapper;

    @Autowired
    private ZsPathMapper zsPathMapper;

    @Autowired
    private ISysAnnouncementService sysAnnouncementService;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    @Transactional
    public void add(ZsSqNotice zsSqNotice) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        zsSqNotice.setUsername(sysUser.getRealname());        //申请人真实姓名
        zsSqNotice.setUserId(sysUser.getId());       //申请人id
        zsSqNotice.setStatus("0");
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(sysUser.getId());   //当前登陆人部门id
        zsSqNotice.setApplyerDept(deptId);
        zsSqNotice.setSendState("0");
        zsSqNotice.setDelFlag("0");
        zsSqNotice.setCreateTime(new Date());
        zsSqNotice.setApplyType("5");

        String pdfUrl = getUrl(zsSqNotice.getUrl());
        zsSqNotice.setPdfUrl(pdfUrl);
        zsSqNoticeMapper.add(zsSqNotice);
        noticeProcessService.add(zsSqNotice);
        //pc端消息提醒
        String[] arr = zsSqNotice.getApprovalPerson().split(",");
        for (int i = 0; i < arr.length; i++) {
            SysAnnouncement sysAnnouncement = new SysAnnouncement();
            sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
            sysAnnouncement.setSendStatus(CommonSendStatus.PUBLISHED_STATUS_1);     //已发布
            sysAnnouncement.setUserIds(arr[i]+",");    //指定用户
            sysAnnouncement.setTitile("您有一条公告申请待审批");
            sysAnnouncement.setSendTime(new Date());        //发布时间
            sysAnnouncement.setPriority(zsSqNotice.getEmergencyLevel());     //紧急程度
            sysAnnouncement.setMsgType("USER");             //指定用户
            sysAnnouncement.setMsgCategory("2");
            sysAnnouncementService.saveAnnouncement(sysAnnouncement);

            try {
                SysUser receiver = sysUserMapper.selectById(arr[i]);
                RTXConfig.sendNotify(receiver.getActivitiSync(), "公告申请", "您有一条公告申请待审批");
            } catch (Exception e) {
                e.printStackTrace();
            }


            //pad端消息提醒
            try {
                WebSocketServer.SendMessage("您有一条公文需要审批", arr[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ZsSqNotice selectById(String id) throws Exception {
        ZsSqNotice zsSqNotice = zsSqNoticeMapper.selectById(id);
        return zsSqNotice;
    }

    /*
     * 公文个人表单
     * */
    @Override
    public Map<String, Object> queryZsSqNotice(ZsSqNotice zsSqNotice, Integer pageNo, Integer pageSize) {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        String userId = sysUser.getId();
        Long total  = zsSqNoticeMapper.queryZsSqNoticeCount(userId);
        List<ZsSqNotice> list = null;
        if (total != null && total > 0) {
            //从第几条数据开始
            int firstIndex = (pageNo - 1) * pageSize;
            //到第几条数据结束
            int lastIndex = pageNo * pageSize;
            list = zsSqNoticeMapper.queryZsSqNotice(userId, firstIndex, lastIndex);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", list);
        return map;
    }

    /*
     * 领导待审批表单
     * */
    @Override
    public Map<String, Object> queryLeaderNoticeProcess(ZsSqNotice zsSqNotice, String emergencyLevel, String title, String username, Integer pageNo, Integer pageSize) {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        String userId = sysUser.getId();
        /*List<SysRole> roleList = sysRoleMapper.roleList(userId);*/
        List<String> docIdList = new ArrayList<>();
        docIdList = noticeProcessMapper.selectId(userId, emergencyLevel, title, username);
        /*if (roleList.get(0).getRoleCode().equals("CO")){
            docIdList = noticeProcessMapper.selectId("CO", emergencyLevel, title, username);
        }else {
            docIdList = noticeProcessMapper.selectId(userId, emergencyLevel, title, username);
        }*/
        Long total = Long.valueOf(docIdList.size());
        List<ZsSqNotice> list = null;
        if (total != null && total > 0) {
            //从第几条数据开始
            int firstIndex = (pageNo - 1) * pageSize;
            //到第几条数据结束
            int lastIndex = pageNo * pageSize;
            list = zsSqNoticeMapper.queryLeaderNoticeProcess(userId, emergencyLevel, title, username, firstIndex, lastIndex);
            /*if (roleList.get(0).getRoleCode().equals("CO")) {
                list = zsSqNoticeMapper.queryLeaderNoticeProcess("CO", emergencyLevel, title, username, firstIndex, lastIndex);
            } else {
                list = zsSqNoticeMapper.queryLeaderNoticeProcess(userId, emergencyLevel, title, username, firstIndex, lastIndex);
            }*/
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", list);
        return map;
    }

    /*
     * 收文
     * */
    @Override
    public Map<String, Object> queryCollectList(String number, String createTime,String title, Integer pageNo, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        String userId = sysUser.getId();
        try {
            Date timeStart = null;
            Date timeEnd = null;
            if (createTime != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String createStart = createTime + " 00:00:00";
                String createEnd = createTime + " 23:59:59";
                timeStart = formatter.parse(createStart);
                timeEnd = formatter.parse(createEnd);
            }
            Long total = zsSqNoticeMapper.count(userId, number,title, timeStart, timeEnd);
            List<ZsSqNotice> list = null;
            if (total != null && total > 0) {
                //从第几条数据开始
                int firstIndex = (pageNo - 1) * pageSize;
                //到第几条数据结束
                int lastIndex = pageNo * pageSize;
                list = zsSqNoticeMapper.queryCollectList(userId, number,title, timeStart, timeEnd, firstIndex, lastIndex);
            }
            map.put("total", total);
            map.put("list", list);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public Map formShow(String id) {
        Map map = new HashMap();
        ZsSqNotice zsSqNotice = zsSqNoticeMapper.formShow(id);
        List<NoticeProcess> noticeProcessList = noticeProcessMapper.formShow(id);
        Read read = new Read();
        if (!(zsSqNotice.getLook() == null)) {
            String readName = "";
            String unreadName = "";
            String arr[] = zsSqNotice.getLook().split(";");
            for (int i = 0; i < arr.length; i++) {
                String arr1[] = arr[i].split(",");
                if (arr1[1].equals("1")) {
                    readName = readName + sysUserMapper.selectById(arr1[0]).getRealname() + "、" ;
                } else {
                    unreadName = unreadName + sysUserMapper.selectById(arr1[0]).getRealname() + "、";
                }
            }
            if (readName == "") {
                readName = "暂无接收人查看";
            }else if (unreadName == "") {
                unreadName = "接收人均已查看";
            }
            read.setReadName(readName);
            read.setUnreadName(unreadName);
        }
        map.put("formData", zsSqNotice);
        map.put("processList", noticeProcessList);
        map.put("read",read);
        return map;
    }

    @Override
    @Transactional
    public void release(String id, String receiver, String receiverName) {
        zsSqNoticeMapper.updateReceiver(id,receiver,receiverName);
        zsSqNoticeMapper.release(id);
        String[] arr = receiver.split(",");
        String look = "";
        for (int i = 0; i < arr.length; i++) {
            look = look + arr[i] + "," + "0" + ";";
        }
        zsSqNoticeMapper.look(id,look);
    }

    @Override
    public IPage<ZsSqNotice> haveDoneList(Page<ZsSqNotice> page, String username) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(sysUser.getId());   //当前登陆人部门id
        Long count = noticeProcessMapper.haveDoneCount(sysUser.getId(), username);
        List<ZsSqNotice> bpmnWorkIPage = zsSqNoticeMapper.haveDoneList(page, sysUser.getId(), username);
        page.setRecords(bpmnWorkIPage);
        page.setTotal(count);
        return page;
    }

    @Override
    @Transactional
    public void updateDelFlag(String id) {
        zsSqNoticeMapper.updateDelFlag(id);
    }

    @Override
    public List<ZsSqNotice> findAll() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<ZsSqNotice> noticeList = zsSqNoticeMapper.findAllByStatus(sysUser.getId());
        return noticeList;
    }

    @Override
    public List<ZsSqNotice> padCollectList() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<ZsSqNotice> zsSqNoticeList = zsSqNoticeMapper.padCollectList(sysUser.getId());
        return zsSqNoticeList;
    }

    @Override
    public Map selectAllById(String id) {
        Map map = new HashMap();
        ZsSqNotice zsSqNotice = zsSqNoticeMapper.formShow(id);
        map.put("formData", zsSqNotice);
        return map;
    }

    @Override
    @Transactional
    public void updateSendState(String id, String sendState) {
        zsSqNoticeMapper.updateSendState(id, sendState);
        zsSqNoticeMapper.updateStatus(id,"0");
    }

    @Override
    @Transactional
    public void coApproval(ZsSqNotice zsSqNotice,String userId) {
        SysUser sysUser = sysUserMapper.selectById(userId);
        zsSqNoticeMapper.updateStatus(zsSqNotice.getId(),"1");
        zsSqNoticeMapper.updateSendState(zsSqNotice.getId(),"0");
        NoticeProcess noticeProcess = new NoticeProcess();
        noticeProcess.setPlayName("已盖章");
        noticeProcessMapper.updateNoticeProcess(sysUser.getId(),zsSqNotice.getId(),noticeProcess.getPlayName(), noticeProcess.getApprovalOpinion(),noticeProcess.getAutograph());
    }

    @Override
    public void look(String id) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        ZsSqNotice zsSqNotice = zsSqNoticeMapper.select(id);
        String[] arr = zsSqNotice.getLook().split(";");
        String look = "";
        for (int i = 0; i < arr.length; i++) {
            String[] arr1 = arr[i].split(",");
            if (arr1[0].equals(userId)) {
                arr1[1] = "1";
            }
            arr[i] = arr1[0] +","+ arr1[1];
            look = look + arr[i] + ";";
        }
        zsSqNoticeMapper.look(id,look);
    }


    public String getUrl(String id) {
        QueryWrapper<ZsPath> qw = new QueryWrapper<>();
        QueryWrapper<ZsPath> queryWrapper = new QueryWrapper<>();
        qw.eq("type", 1);
        queryWrapper.eq("type", 2);
        ZsPath wordPath = zsPathMapper.selectOne(qw);
        ZsPath pdfPath = zsPathMapper.selectOne(queryWrapper);
        ZsReturnPath returnPath = zsReturnPathMapper.selectById(id);

        String pdfFile = returnPath.getUrl().substring(0, returnPath.getUrl().lastIndexOf(".") + 1) + "pdf";
        File file = new File(pdfPath.getPath());
        if (!file.exists()) {
            file.mkdirs(); // 创建文件根目录
        }
        File inputWord = new File(wordPath.getPath() + returnPath.getUrl());
        File outputFile = new File(pdfPath.getPath() + pdfFile);

        System.out.println("inputWord："+inputWord);
        System.out.println("outputFile："+outputFile);
        try  {
            InputStream docxInputStream = new FileInputStream(inputWord);
            OutputStream outputStream = new FileOutputStream(outputFile);
            IConverter converter = LocalConverter.builder().build();
            converter.convert(docxInputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return pdfFile;
        }

    }
}
