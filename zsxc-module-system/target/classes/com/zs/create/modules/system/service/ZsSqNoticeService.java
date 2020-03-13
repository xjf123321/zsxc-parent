package com.zs.create.modules.system.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.ZsSqNotice;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 公告
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-28
 */
public interface ZsSqNoticeService extends IService<ZsSqNotice> {

    Map<String, Object> queryZsSqNotice(ZsSqNotice zsSqNotice, Integer pageNo, Integer pageSize);

    void add(ZsSqNotice zsSqNotice) throws Exception;

    ZsSqNotice selectById(String id) throws Exception;

    Map<String, Object> queryLeaderNoticeProcess(ZsSqNotice zsSqNotice, String emergencyLevel, String title, String username, Integer pageNo, Integer pageSize);

    Map<String, Object> queryCollectList(String number, String createTime,String title, Integer pageNo, Integer pageSize);

    Map formShow(String id);

    void release(String id, String receiver, String receiverName);

    IPage<ZsSqNotice> haveDoneList(Page<ZsSqNotice> page, String username);

    void updateDelFlag(String id);

    List<ZsSqNotice> findAll();

    List<ZsSqNotice> padCollectList();

    Map selectAllById(String id);

    void updateSendState(String id, String s);

    void coApproval(ZsSqNotice zsSqNotice,String userId);

    void look(String id);
}
