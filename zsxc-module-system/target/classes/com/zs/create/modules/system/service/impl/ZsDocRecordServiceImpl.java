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
import com.zs.create.modules.system.service.DocRecordService;
import com.zs.create.modules.system.service.ISysAnnouncementService;
import com.zs.create.modules.system.service.ZsDocRecordService;
import com.zs.create.modules.system.vo.ZsDocRecordVo;
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
 * 公文
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-25
 */
@Service
@Slf4j
public class ZsDocRecordServiceImpl extends ServiceImpl<ZsDocRecordMapper, ZsDocRecord> implements ZsDocRecordService {

    @Autowired
    private ZsDocRecordMapper zsDocRecordMapper;

    @Autowired
    private DocRecordMapper docRecordMapper;

    @Autowired
    private SysUserDepartMapper sysUserDepartMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private DocRecordService docRecordService;

    @Autowired
    private ZsReturnPathMapper zsReturnPathMapper;

    @Autowired
    private ZsPathMapper zsPathMapper;

    @Autowired
    private ISysAnnouncementService sysAnnouncementService;

    @Autowired
    private SysRoleMapper sysRoleMapper;




    @Override
    @Transactional
    public void add(ZsDocRecord zsDocRecord) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        zsDocRecord.setUsername(sysUser.getRealname());        //申请人真实姓名
        zsDocRecord.setUserId(sysUser.getId());       //申请人id
        zsDocRecord.setStatus("0");
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(sysUser.getId());   //当前登陆人部门id
        zsDocRecord.setApplyerDept(deptId);
        zsDocRecord.setSendState("0");
        zsDocRecord.setDelFlag("0");
        zsDocRecord.setCreateTime(new Date());
        zsDocRecord.setApplyType("4");
        String pdfUrl = getUrl(zsDocRecord.getUrl());
        zsDocRecord.setPdfUrl(pdfUrl);
        if (zsDocRecord.getEnclosure() == ""){
            zsDocRecord.setEnclosure(null);
        }
        zsDocRecordMapper.add(zsDocRecord);
        //数据插入过程表
        docRecordService.add(zsDocRecord);
        //pc端消息提醒
        String[] arr = zsDocRecord.getApprovalPerson().split(",");
        for (int i = 0; i < arr.length; i++) {
            SysAnnouncement sysAnnouncement = new SysAnnouncement();
            sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
            sysAnnouncement.setSendStatus(CommonSendStatus.PUBLISHED_STATUS_1);     //已发布
            sysAnnouncement.setUserIds(arr[i]+",");    //指定用户
            sysAnnouncement.setTitile("您有一条公文申请待审批");
            sysAnnouncement.setSendTime(new Date());        //发布时间
            sysAnnouncement.setPriority(zsDocRecord.getEmergencyLevel());     //紧急程度
            sysAnnouncement.setMsgType("USER");             //指定用户
            sysAnnouncement.setMsgCategory("2");
            sysAnnouncementService.saveAnnouncement(sysAnnouncement);
            try {
                SysUser receiver = sysUserMapper.selectById(arr[i]);
                RTXConfig.sendNotify(receiver.getActivitiSync(), "公文申请", "您有一条公文申请待审批");
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
    public ZsDocRecord selectById(String id) throws Exception {
        ZsDocRecord zsDocRecord = zsDocRecordMapper.selectById(id);
        return zsDocRecord;
    }

    /*
    * 公文个人表单
    * */
    @Override
    public Map<String, Object> queryZsDocRecord(ZsDocRecord zsDocRecord, Integer pageNo, Integer pageSize) {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        String userId = sysUser.getId();
        Long total  = zsDocRecordMapper.queryZsDocRecordCount(userId);
        List<ZsDocRecord> list = null;
        if (total != null && total > 0) {
            //从第几条数据开始
            int firstIndex = (pageNo - 1) * pageSize;
            //到第几条数据结束
            int lastIndex = pageNo * pageSize;
            list = zsDocRecordMapper.queryZsDocRecord(userId, firstIndex, lastIndex);
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
    public Map<String, Object> queryLeaderDocRecord(ZsDocRecord zsDocRecord, String emergencyLevel, String title, String username, Integer pageNo, Integer pageSize) {
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        String userId = sysUser.getId();
        /*List<SysRole> roleList = sysRoleMapper.roleList(userId);*/
        List<String> docIdList = new ArrayList<>();
        /*if (roleList.get(0).getRoleCode().equals("CO")){
            docIdList = docRecordMapper.selectId("CO", emergencyLevel, title, username);
        }else {
            docIdList = docRecordMapper.selectId(userId, emergencyLevel, title, username);
        }*/
        docIdList = docRecordMapper.selectId(userId, emergencyLevel, title, username);
        Long total = Long.valueOf(docIdList.size());
        List<ZsDocRecord> list = null;
        if (total != null && total > 0) {
            //从第几条数据开始
            int firstIndex = (pageNo - 1) * pageSize;
            //到第几条数据结束
            int lastIndex = pageNo * pageSize;
            list = zsDocRecordMapper.queryLeaderDocRecord(userId, emergencyLevel, title, username, firstIndex, lastIndex);
            /*if (roleList.get(0).getRoleCode().equals("CO")) {
                list = zsDocRecordMapper.queryLeaderDocRecord("CO", emergencyLevel, title, username, firstIndex, lastIndex);
            } else {
                list = zsDocRecordMapper.queryLeaderDocRecord(userId, emergencyLevel, title, username, firstIndex, lastIndex);
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
            Long total = zsDocRecordMapper.count(userId, number,title, timeStart, timeEnd);
            List<ZsDocRecord> list = null;
            if (total != null && total > 0) {
                //从第几条数据开始
                int firstIndex = (pageNo - 1) * pageSize;
                //到第几条数据结束
                int lastIndex = pageNo * pageSize;
                list = zsDocRecordMapper.queryCollectList(userId, number,title, timeStart, timeEnd, firstIndex, lastIndex);
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
        ZsDocRecord zsDocRecord = zsDocRecordMapper.formShow(id);
        List<DocRecord> docRecordList = docRecordMapper.formShow(id);
        Read read = new Read();
        if (!(zsDocRecord.getLook() == null)) {
            String readName = "";
            String unreadName = "";
            String arr[] = zsDocRecord.getLook().split(";");
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
        map.put("formData", zsDocRecord);
        map.put("processList", docRecordList);
        map.put("read",read);
        return map;
}

    @Override
    @Transactional
    public void release(String id, String receiver, String receiverName) {
        zsDocRecordMapper.updateReceiver(id,receiver,receiverName);
        zsDocRecordMapper.release(id);
        String[] arr = receiver.split(",");
        String look = "";
        for (int i = 0; i < arr.length; i++) {
            look = look + arr[i] + "," + "0" + ";";
        }
        zsDocRecordMapper.look(id,look);
    }

    @Override
    @Transactional
    public IPage<ZsDocRecord> haveDoneList(Page<ZsDocRecord> page, String username) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String deptId = sysUserDepartMapper.selectDeptIdByUserId(sysUser.getId());   //当前登陆人部门id
        Long count = docRecordMapper.haveDoneCount(sysUser.getId(), username);
        List<ZsDocRecord> bpmnWorkIPage = zsDocRecordMapper.haveDoneList(page, sysUser.getId(), username);
        page.setRecords(bpmnWorkIPage);
        page.setTotal(count);
        return page;
    }

    @Override
    @Transactional
    public void updateDelFlag(String id) {
        zsDocRecordMapper.updateDelFlag(id);
    }

    @Override
    public List<ZsDocRecord> padCollectList() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<ZsDocRecord> zsDocRecordList = zsDocRecordMapper.padCollectList(sysUser.getId());
        return zsDocRecordList;
    }

    @Override
    public Map selectAllById(String id) {
        Map map = new HashMap();
        ZsDocRecord zsDocRecord = zsDocRecordMapper.formShow(id);
        map.put("formData", zsDocRecord);
        return map;
    }

    @Override
    @Transactional
    public void updateSendState(String id, String sendState) {
        zsDocRecordMapper.updateSendState(id, sendState);
        zsDocRecordMapper.updateStatus(id,"0");
    }

    @Override
    @Transactional
    public void coApproval(ZsDocRecord zsDocRecord, String userId) {
        SysUser sysUser = sysUserMapper.selectById(userId);
        zsDocRecordMapper.updateStatus(zsDocRecord.getId(),"1");
        zsDocRecordMapper.updateSendState(zsDocRecord.getId(),"0");
        DocRecord docRecord = new DocRecord();
        docRecord.setPlayName("已盖章");
        docRecordMapper.updateDocRecord(sysUser.getId(),zsDocRecord.getId(),docRecord.getPlayName(),
                docRecord.getApprovalOpinion(),docRecord.getAutograph());
    }

    @Override
    public void look(String id) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        ZsDocRecord zsDocRecord = zsDocRecordMapper.select(id);
        String[] arr = zsDocRecord.getLook().split(";");
        String look = "";
        for (int i = 0; i < arr.length; i++) {
            String[] arr1 = arr[i].split(",");
            if (arr1[0].equals(userId)) {
                arr1[1] = "1";
            }
            arr[i] = arr1[0] +","+ arr1[1];
            look = look + arr[i] + ";";
        }
        zsDocRecordMapper.look(id,look);
    }

    @Override
    public List<ZsDocRecord> findAll() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<ZsDocRecord> docRecordList = zsDocRecordMapper.findAllByStatus(sysUser.getId());
        return docRecordList;
    }

    @Override
    public ZsDocRecordVo read(String id) {
        String readName = "";
        String unreadName = "";
        ZsDocRecordVo zsDocRecordVo = new ZsDocRecordVo();
        ZsDocRecord zsDocRecord = zsDocRecordMapper.select(id);
        zsDocRecordVo.setLook(zsDocRecord.getLook());
        String arr[] = zsDocRecordVo.getLook().split(";");
        for (int i = 0; i < arr.length; i++) {
            String arr1[] = arr[i].split(",");
            for (int j = 0; j < arr1.length; j++) {
                if (arr1[1].equals("1")) {
                    readName = readName + sysUserMapper.selectById(arr1[0]).getUsername() + "、" ;
                } else {
                    unreadName = unreadName + sysUserMapper.selectById(arr1[0]).getUsername() + "、";
                }
            }
        }
        if (readName == "") {
            readName = "暂无接收人查看";
        }else if (unreadName == "") {
            unreadName = "接收人均已查看";
        }
        zsDocRecordVo.setReadName(readName);
        zsDocRecordVo.setUnreadName(unreadName);
        return zsDocRecordVo;
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

