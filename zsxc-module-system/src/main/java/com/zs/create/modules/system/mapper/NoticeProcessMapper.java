package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.create.modules.system.entity.NoticeProcess;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface NoticeProcessMapper extends BaseMapper<NoticeProcess> {
    List<String> selectId(@Param("approvalPerson")String approvalPerson, @Param("emergencyLevel")String emergencyLevel, @Param("title")String title, @Param("username")String username);

    List<NoticeProcess> formShow(String docId);

    Set<String> selectStatus(String docId);

    void updateStatus(@Param("approvalPerson")String approvalPerson, @Param("docId")String docId, @Param("status")String approvalStatus);

    void updateNoticeProcessStatus(@Param("approvalPerson")String approvalPerson, @Param("docId")String docId, @Param("playName")String playName,
                                   @Param("approvalOpinion")String approvalOpinion, @Param("userName")String userName, @Param("autograph")String autograph);

    void updateNotNoticeProcessStatus(@Param("approvalPerson")String approvalPerson, @Param("docId")String docId, @Param("playName")String playName,
                                      @Param("approvalOpinion")String approvalOpinion, @Param("userName")String userName);

    Long haveDoneCount(@Param("approvalPerson") String approvalPerson, @Param("username")String username);

    void updateNoticeProcess(@Param("approvalPerson") String approvalPerson, @Param("docId") String docId, @Param("playName") String playName,
                             @Param("approvalOpinion") String approvalOpinion,@Param("autograph")String autograph);
}
