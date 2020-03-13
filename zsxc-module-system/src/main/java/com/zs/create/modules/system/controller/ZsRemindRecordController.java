package com.zs.create.modules.system.controller;


import com.zs.create.common.api.vo.Result;
import com.zs.create.common.aspect.annotation.AutoLog;
import com.zs.create.modules.system.entity.ZsRemindRecord;
import com.zs.create.modules.system.service.ZsRemindRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;
import java.util.List;


/**
 * <p>
 * 消息提醒
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-11-20
 * */
@RestController
@RequestMapping("/zsRemindRecord")
@Api(tags = "消息提醒")
@Slf4j
public class ZsRemindRecordController {

    @Autowired
    private ZsRemindRecordService zsRemindRecordService;

    /*
    * 消息提醒查看
    * */
    @AutoLog(value = "消息提醒查看")
    @ApiOperation(value = "消息提醒查看", notes = "消息提醒查看")
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public Result<List<ZsRemindRecord>> select(@RequestParam(name = "txsj", required = true) Date txsj) {
        Result<List<ZsRemindRecord>> result = new Result<>();
        List<ZsRemindRecord> zsRemindRecordList = zsRemindRecordService.select(txsj);
        result.setResult(zsRemindRecordList);
        return result;
    }
}
