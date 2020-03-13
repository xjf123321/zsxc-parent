package com.zs.create.modules.homepage.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.zs.create.common.api.vo.Result;
import com.zs.create.modules.homepage.service.IndexCountService;
import com.zs.create.modules.process.service.ZsSqLeaveTwoService;
import com.zs.create.modules.system.entity.*;
import com.zs.create.modules.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 首页统计
 * </p>
 *
 * @Author yaochao
 * @since 2019-11-7
 */
@RestController
@RequestMapping("/count")
@Api(tags = "首页统计")
@Slf4j
public class IndexCountController {

    @Autowired
    private ZsSqCarTwoService zsSqCarTwoService;

    @Autowired
    private ZsSqRoomService zsSqRoomService;

    @Autowired
    private ZsSqBackService zsSqBackService;

    @Autowired
    private ZsSqLeaveTwoService zsSqLeaveTwoService;

    @Autowired
    private ZsSqNoticeService zsSqNoticeService;

    @Autowired
    private ZsDocRecordService zsDocRecordService;

    @Autowired
    private ZsBasicCarService zsBasicCarService;

    @Autowired
    private ZsBasicRoomService zsBasicRoomService;

    @Autowired
    private IndexCountService indexCountService;

    /**
     * 待办事项统计
     * @return
     */
    @GetMapping(value = "/needDo")
    @ApiOperation("待办事项统计")
    public Result<Map> needDo() {
        Boolean flag = indexCountService.selectRole();
        String title = null;
        String username = null;
        Integer pageNo = 1;
        Integer pageSize = 10;
        Result<Map> result = new Result<>();
        Page<ZsSqRoom> pageRoom = new Page<>(pageNo, pageSize);
        Page<ZsSqCar> pageCar = new Page<>(pageNo, pageSize);
        Page<ZsSqLeave> pageLeave = new Page<>(pageNo, pageSize);
        Page<ZsSqBack> pageBack = new Page<>(pageNo, pageSize);

        Map<String, Object> carApplyList = zsSqCarTwoService.carApplyList("0", title, username, pageNo, pageSize);
        IPage<ZsSqRoom> meettingApplyList = zsSqRoomService.meettingApplyList("0", pageRoom, title, username);
        IPage<ZsSqBack> backApplyList = zsSqBackService.backApplyList("0", pageBack, username);
        IPage<ZsSqLeave> leaveApplyList = zsSqLeaveTwoService.leaveApplyList(pageLeave, "0", title, username);
        ZsSqNotice zsSqNotice = new ZsSqNotice();
        Map<String, Object> noticResultMap = zsSqNoticeService.queryLeaderNoticeProcess(zsSqNotice, "0", title, username, pageNo, pageSize);
        ZsDocRecord zsDocRecord = new ZsDocRecord();
        Map<String, Object> docResultMap = zsDocRecordService.queryLeaderDocRecord(zsDocRecord, "0", title, username, pageNo, pageSize);
        Long count = (Long) carApplyList.get("total") + meettingApplyList.getTotal() + backApplyList.getTotal()
                    + leaveApplyList.getTotal() + (Long) noticResultMap.get("total") + (Long) docResultMap.get("total");
        result.setSuccess(true);
        Map map = new HashMap();
        map.put("count", count);
        map.put("flag", flag);
        map.put("car", (Long) carApplyList.get("total"));
        map.put("meetting", meettingApplyList.getTotal());
        map.put("back", backApplyList.getTotal());
        map.put("leave", leaveApplyList.getTotal());
        map.put("notice", noticResultMap.get("total"));
        map.put("doc", docResultMap.get("total"));
        result.setResult(map);
        return result;
    }


    /**
     * 催办督办统计
     * @return
     */
    @GetMapping(value = "/urgeDo")
    @ApiOperation("催办督办统计")
    public Result<Map> urgeDo() {
        String title = null;
        String username = null;
        Integer pageNo = 1;
        Integer pageSize = 10;

        Result<Map> result = new Result<>();
        Page<ZsSqRoom> pageRoom = new Page<>(pageNo, pageSize);
        Page<ZsSqCar> pageCar = new Page<>(pageNo, pageSize);
        Page<ZsSqLeave> pageLeave = new Page<>(pageNo, pageSize);
        Page<ZsSqBack> pageBack = new Page<>(pageNo, pageSize);

        Map<String, Object> carApplyList = zsSqCarTwoService.carApplyList("1", title, username, pageNo, pageSize);
        IPage<ZsSqRoom> meettingApplyList = zsSqRoomService.meettingApplyList("1", pageRoom, title, username);
        IPage<ZsSqBack> backApplyList = zsSqBackService.backApplyList("1", pageBack, username);
        IPage<ZsSqLeave> leaveApplyList = zsSqLeaveTwoService.leaveApplyList(pageLeave, "1", title, username);
        ZsSqNotice zsSqNotice = new ZsSqNotice();
        Map<String, Object> noticResultMap = zsSqNoticeService.queryLeaderNoticeProcess(zsSqNotice, "1", title, username, pageNo, pageSize);
        ZsDocRecord zsDocRecord = new ZsDocRecord();
        Map<String, Object> docResultMap = zsDocRecordService.queryLeaderDocRecord(zsDocRecord, "1", title, username, pageNo, pageSize);
        Long count = (Long) carApplyList.get("total") + meettingApplyList.getTotal() + backApplyList.getTotal() + leaveApplyList.getTotal()
                    + (Long) noticResultMap.get("total") + (Long) docResultMap.get("total");
        result.setSuccess(true);
        Map map = new HashMap();
        map.put("count", count);
        map.put("car", (Long) carApplyList.get("total"));
        map.put("meetting", meettingApplyList.getTotal());
        map.put("back", backApplyList.getTotal());
        map.put("leave", leaveApplyList.getTotal());
        map.put("notice", noticResultMap.get("total"));
        map.put("doc", docResultMap.get("total"));
        result.setResult(map);
        return result;
    }


    /**
     * 已办事项统计
     * @return
     */
    @GetMapping(value = "/alreadyDo")
    @ApiOperation("已办事项统计")
    public Result<Map> alreadyDo() {
        String realname = null;
        String username = null;
        Integer pageNo = 1;
        Integer pageSize = 10;

        Result<Map> result = new Result<>();
        Page<ZsSqRoom> pageRoom = new Page<>(pageNo, pageSize);
        Page<ZsSqCar> pageCar = new Page<>(pageNo, pageSize);
        Page<ZsSqNotice> pageNotice = new Page<>(pageNo, pageSize);
        Page<ZsSqLeave> pageLeave = new Page<>(pageNo, pageSize);
        Page<ZsDocRecord> pageDocRecord = new Page<>(pageNo, pageSize);
        Page<ZsSqBack> pageBack = new Page<>(pageNo, pageSize);

        IPage<ZsSqRoom> roomAllowList = zsSqRoomService.allowList(realname, pageRoom, username);
        IPage<ZsSqCar> carHaveDoneList = zsSqCarTwoService.haveDoneList(pageCar, username);
        IPage<ZsSqNotice> noticeHaveDoneList = zsSqNoticeService.haveDoneList(pageNotice, username);
        IPage<ZsSqLeave> leaveHaveDoneList = zsSqLeaveTwoService.haveDoneList(pageLeave, username);
        IPage<ZsDocRecord> docHaveDoneList = zsDocRecordService.haveDoneList(pageDocRecord, username);
        IPage<ZsSqBack> backAllowList = zsSqBackService.allowList(realname, pageBack, username);
        Long count  = roomAllowList.getTotal() + carHaveDoneList.getTotal() + noticeHaveDoneList.getTotal() + leaveHaveDoneList.getTotal() + docHaveDoneList.getTotal() + backAllowList.getTotal();
        result.setSuccess(true);
        Map map = new HashMap();
        map.put("count", count);
        map.put("car", carHaveDoneList.getTotal());
        map.put("meetting", roomAllowList.getTotal());
        map.put("back", backAllowList.getTotal());
        map.put("leave", leaveHaveDoneList.getTotal());
        map.put("notice", noticeHaveDoneList.getTotal());
        map.put("doc", docHaveDoneList.getTotal());
        result.setResult(map);
        return result;
    }


    /**
     * 车辆使用情况分析
     * @return
     */
    @GetMapping(value = "/carStateCount")
    @ApiOperation("车辆使用情况分析")
    public Result<Map<String, Integer>> carRate() {
        Result<Map<String, Integer>> result = new Result<Map<String, Integer>>();
        Map<String, Integer> map = new HashMap<String, Integer>();
        map = zsBasicCarService.status();
        result.setResult(map);
        result.setSuccess(true);
        return result;
    }

    /**
     * 会议室使用情况分析
     * @return
     */
    @GetMapping(value = "/roomStateCount")
    @ApiOperation("会议室使用情况分析")
    public Result<Map<String, Integer>>roomStateCount() {
        Result<Map<String, Integer>> result = new Result<Map<String, Integer>>();
        Map<String, Integer> map = zsBasicRoomService.status();
        result.setResult(map);
        result.setSuccess(true);
        return result;
    }

    /**
     * pad端流程统计
     * @return
     */
    @GetMapping(value = "/padCount")
    @ApiOperation("pad端流程统计")
    public Result<Map<String, Long>> padCount(@RequestParam(name = "emergencyLevel", required = false) String emergencyLevel) {
        String title = null;
        String username = null;
        Integer pageNo = 1;
        Integer pageSize = 10;

        Result<Map<String, Long>> result = new Result<>();
        Page<ZsSqRoom> pageRoom = new Page<>(pageNo, pageSize);
        Page<ZsSqCar> pageCar = new Page<>(pageNo, pageSize);
        Page<ZsSqLeave> pageLeave = new Page<>(pageNo, pageSize);
        Page<ZsSqBack> pageBack = new Page<>(pageNo, pageSize);

        Map<String, Object> carNeedDo = zsSqCarTwoService.carApplyList(emergencyLevel, title, username, pageNo, pageSize);
        IPage<ZsSqRoom> meettingNeedDo = zsSqRoomService.meettingApplyList(emergencyLevel, pageRoom, title, username);
        IPage<ZsSqLeave> leaveNeedDo = zsSqLeaveTwoService.leaveApplyList(pageLeave, emergencyLevel, title, username);
        ZsSqNotice zsSqNotice = new ZsSqNotice();
        Map<String, Object> noticNeedDo = zsSqNoticeService.queryLeaderNoticeProcess(zsSqNotice, emergencyLevel, title, username, pageNo, pageSize);
        ZsDocRecord zsDocRecord = new ZsDocRecord();
        Map<String, Object> docNeedDo = zsDocRecordService.queryLeaderDocRecord(zsDocRecord, emergencyLevel, title, username, pageNo, pageSize);
        IPage<ZsSqBack> backNeedDo = zsSqBackService.backApplyList(emergencyLevel, pageBack, username);

        Map<String, Long> map = new HashMap<>();
        map.put("car", (Long) carNeedDo.get("total"));
        map.put("meetting", meettingNeedDo.getTotal());
        map.put("leave", leaveNeedDo.getTotal());
        map.put("notice", (Long) noticNeedDo.get("total"));
        map.put("doc", (Long) docNeedDo.get("total"));
        map.put("back", backNeedDo.getTotal());
        result.setResult(map);
        result.setSuccess(true);
        return result;
    }




    /**
     * 通知公告列表
     * @return
     */
    @GetMapping(value = "/noticeList")
    @ApiOperation("通知公告列表")
    public Result<List<ZsSqNotice>> noticeList() {
        Result<List<ZsSqNotice>> result = new Result<>();
        List<ZsSqNotice> noticePage = zsSqNoticeService.findAll();
        result.setResult(noticePage);
        result.setSuccess(true);
        return result;
    }

    /*
    *首页申请统计
    * */
    @GetMapping(value = "/indexCount")
    @ApiOperation("首页申请统计")
    public Result<Map<String, Long>>indexCount() {
        Result<Map<String, Long>> result = new Result<>();
        Map<String, Long> map = indexCountService.indexCount();
        result.setResult(map);
        result.setSuccess(true);
        return result;
    }

    /*
    * 保密员列表
    * */
    @ApiOperation("保密员列表")
    @RequestMapping(value = "/secrecy",method = RequestMethod.GET)
    public Result secrecy(){
        Result result = new Result();
        Map map = indexCountService.selectSecrecy();
        result.setResult(map);
        return result;
    }

    /*
     * 已办数据
     * */
    @ApiOperation("已办数据")
    @RequestMapping(value = "/haveDone",method = RequestMethod.GET)
    public Result haveDone(){
        Result result = new Result();
        Page<Done> page = new Page<Done>(1, 10);
        IPage<Done> pageList = indexCountService.queryDone(page,null);
        List<Done> list = pageList.getRecords();
        result.setResult(list);
        return result;
    }

    /**
     * 公文列表
     * @return
     */
    @GetMapping(value = "/docList")
    @ApiOperation("公文列表")
    public Result<List<ZsDocRecord>> docList() {
        Result<List<ZsDocRecord>> result = new Result<>();
        List<ZsDocRecord> docPage = zsDocRecordService.findAll();
        result.setResult(docPage);
        result.setSuccess(true);
        return result;
    }

    /*
     * 已办数据
     * */
    @ApiOperation("已办数据列表")
    @RequestMapping(value = "/queryDone",method = RequestMethod.GET)
    public Result<IPage<Done>> queryDone(@RequestParam(name = "applyer",required = false) String applyer,
                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        Result<IPage<Done>> result = new Result<>();
        Page<Done> page = new Page<Done>(pageNo, pageSize);
        IPage<Done> pageList = indexCountService.queryDone(page,applyer);
        result.setSuccess(true);
        result.setResult(pageList);
        Long pages = null;
        if (pageList.getSize()/pageSize == 0) {
            pages = pageList.getSize()/pageSize;
        }else {
            pages = pageList.getSize()/pageSize + 1;
        }
        result.getResult().setPages(pages);
        return result;
    }
}
