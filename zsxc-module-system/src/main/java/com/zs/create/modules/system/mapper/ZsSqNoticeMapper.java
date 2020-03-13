package com.zs.create.modules.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.modules.system.entity.Done;
import com.zs.create.modules.system.entity.ZsSqNotice;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 公告
 * </p>
 *
 * @Author xiajunfeng
 * @since 2019-10-28
 */
public interface ZsSqNoticeMapper extends BaseMapper<ZsSqNotice> {
    ZsSqNotice select(String id);

    List<ZsSqNotice> queryLeaderNoticeProcess(@Param("approvalPerson") String approvalPerson, @Param("emergencyLevel")String emergencyLevel,
                                              @Param("title")String title, @Param("username")String username, @Param("firstIndex")int firstIndex,
                                              @Param("lastIndex")int lastIndex);

    Long queryZsSqNoticeCount(String userId);

    List<ZsSqNotice> queryZsSqNotice(@Param("userId")String userId, @Param("firstIndex")int firstIndex, @Param("lastIndex")int lastIndex);

    Long count(@Param("receiver") String receiver, @Param("number") String number, @Param("title") String title,
               @Param("timeStart") Date timeStart, @Param("timeEnd") Date timeEnd);

    List<ZsSqNotice> queryCollectList(@Param("receiver")String receiver, @Param("number")String number, @Param("title") String title,
                                      @Param("timeStart") Date timeStart, @Param("timeEnd") Date timeEnd,
                                      @Param("firstIndex")int firstIndex, @Param("lastIndex")int lastIndex);

    ZsSqNotice formShow(String id);

    void updateStatus(@Param("docId")String docId, @Param("status")String status);

    void add(ZsSqNotice zsSqNotice);

    void release(String id);

    List<ZsSqNotice> haveDoneList(Page<ZsSqNotice> page, @Param("approvalPerson") String approvalPerson, @Param("username")String username);

    void updateDelFlag(String id);

    List<ZsSqNotice> findAllByStatus(@Param("id") String id);

    List<ZsSqNotice> padCollectList(@Param("receiver") String receiver);

    void updateReceiver(@Param("id") String id, @Param("receiver") String receiver, @Param("receiverName") String receiverName);

    Long noticeCont(String userId);

    void updateSendState(@Param("id") String id, @Param("sendState") String sendState);

    ZsSqNotice findByUrl(String url);

    List<Done> done(String userId);

    void look(@Param("id") String id, @Param("look") String look);
}
