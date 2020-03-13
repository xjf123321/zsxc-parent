package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.ZsDocRecord;
import com.zs.create.modules.system.vo.ZsDocRecordVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 公文
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-25
 * */
public interface ZsDocRecordService extends IService<ZsDocRecord> {
    void add(ZsDocRecord zsDocRecord) throws Exception;

    ZsDocRecord selectById(String id) throws Exception;

    Map<String, Object> queryZsDocRecord(ZsDocRecord zsDocRecord, Integer pageNo, Integer pageSize);

    Map<String, Object> queryLeaderDocRecord(ZsDocRecord zsDocRecord, String emergencyLevel, String title, String username, Integer pageNo, Integer pageSize);

    Map<String, Object> queryCollectList(String number, String createTime,String title, Integer pageNo, Integer pageSize);

    Map formShow(String id);

    void release(String id, String receiver, String receiverName);

    IPage<ZsDocRecord> haveDoneList(Page<ZsDocRecord> page, String username);

    void updateDelFlag(String id);

    List<ZsDocRecord> padCollectList();

    Map selectAllById(String id);

    void updateSendState(String id, String sendState);

    void coApproval(ZsDocRecord zsDocRecord, String userId);

    void look(String id);

    List<ZsDocRecord> findAll();

    ZsDocRecordVo read(String id);
}
