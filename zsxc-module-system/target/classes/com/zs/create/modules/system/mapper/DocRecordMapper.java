package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zs.create.modules.system.entity.DocRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface DocRecordMapper extends BaseMapper<DocRecord> {

    List<String> selectId(@Param("approvalPerson")String approvalPerson, @Param("emergencyLevel")String emergencyLevel,
                          @Param("title")String title, @Param("username")String username);

    List<DocRecord> formShow(String docId);

    Set<String> selectStatus(String docId);

    void updateStatus(@Param("approvalPerson")String approvalPerson, @Param("docId")String docId, @Param("approvalStatus")String approvalStatus);

    void updateDocRecordStatus(@Param("approvalPerson")String approvalPerson, @Param("docId")String docId, @Param("playName")String playName,
                               @Param("approvalOpinion") String approvalOpinion, @Param("userName")String username, @Param("autograph")String autograph);

    void updateNotDocRecordStatus(@Param("approvalPerson")String approvalPerson, @Param("docId")String docId, @Param("playName")String playName,
                                  @Param("approvalOpinion") String approvalOpinion, @Param("userName")String username);

    Long haveDoneCount(@Param("approvalPerson") String approvalPerson, @Param("username") String username);

    void updateDocRecord(@Param("approvalPerson") String approvalPerson, @Param("docId") String docId, @Param("playName") String playName,
                         @Param("approvalOpinion") String approvalOpinion,@Param("autograph")String autograph);
}
