package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.modules.system.entity.Done;
import com.zs.create.modules.system.entity.ZsDocRecord;
import com.zs.create.modules.system.vo.ZsDocRecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ZsDocRecordMapper extends BaseMapper<ZsDocRecord> {

    ZsDocRecord select(String id);

    List<ZsDocRecord> queryLeaderDocRecord(@Param("approvalPerson")String approvalPerson, @Param("emergencyLevel")String emergencyLevel,
                                           @Param("title")String title, @Param("username")String username,
                                           @Param("firstIndex")int firstIndex, @Param("lastIndex")int lastIndex);

    Long queryZsDocRecordCount(String userId);

    List<ZsDocRecord> queryZsDocRecord(@Param("userId")String userId, @Param("firstIndex")int firstIndex, @Param("lastIndex")int lastIndex);

    Long count(@Param("receiver") String receiver, @Param("number") String number, @Param("title") String title,
               @Param("timeStart") Date timeStart, @Param("timeEnd") Date timeEnd);

    List<ZsDocRecord> queryCollectList(@Param("receiver")String receiver, @Param("number")String number,@Param("title")String title,
                                       @Param("timeStart") Date timeStart, @Param("timeEnd") Date timeEnd,
                                       @Param("firstIndex")int firstIndex, @Param("lastIndex")int lastIndex);

    ZsDocRecord formShow(String id);

    void updateStatus(@Param("docId")String docId, @Param("status")String status);

    void add(ZsDocRecord zsDocRecord);

    void release(String id);

    List<ZsDocRecord> haveDoneList(Page<ZsDocRecord> page, @Param("approvalPerson")String approvalPerson, @Param("username")String username);

    void updateDelFlag(String id);

    List<ZsDocRecord> padCollectList(@Param("receiver") String receiver);

    void updateReceiver(@Param("id") String id, @Param("receiver") String receiver, @Param("receiverName") String receiverName);

    Long docCount(String userId);

    void updateSendState(@Param("id") String id,@Param("sendState") String sendState);

    ZsDocRecord findByUrl(String url);

    List<Done> done(String userId);

    void look(@Param("id") String id, @Param("look") String look);

    List<ZsDocRecord> findAllByStatus(@Param("id") String id);
}
