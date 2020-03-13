package com.zs.create.modules.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.create.modules.system.entity.QueryEmail;
import com.zs.create.modules.system.entity.ZsEmail;
import com.zs.create.modules.system.vo.ZsEmailVO;
import org.apache.ibatis.annotations.Param;



/**
 * <p>
 * 邮件表 mapper接口
 * </p>
 * @Author yaochao
 * @since 2019-10-14
 */
public interface ZsEmailMapper extends BaseMapper<ZsEmail> {

     /*
     * 添加邮件
     * */
    void insertEmail(ZsEmail zsEmail);


    Page<ZsEmailVO> queryPage(@Param("page") Page<ZsEmailVO> page, @Param("queryWrapper") QueryWrapper queryWrapper);

    void updateDel(String id);

    ZsEmailVO findById(String id);

    Page<ZsEmailVO> searchInbox(@Param("page") Page<ZsEmailVO> page, @Param("zsEmailVO")ZsEmailVO zsEmailVO, @Param("id") String id, @Param("queryEmail") QueryEmail  queryEmail);

    Page<ZsEmailVO> searchOutbox(@Param("page") Page<ZsEmailVO> page, @Param("zsEmailVO")ZsEmailVO zsEmailVO, @Param("id") String id, @Param("queryEmail") QueryEmail  queryEmail);

    Page<ZsEmailVO> searchDrafts(@Param("page") Page<ZsEmailVO> page, @Param("zsEmailVO")ZsEmailVO zsEmailVO, @Param("id") String id, @Param("queryEmail") QueryEmail  queryEmail);

    Page<ZsEmailVO> searchDustbin(@Param("page") Page<ZsEmailVO> page, @Param("zsEmailVO")ZsEmailVO zsEmailVO, @Param("id") String id, @Param("queryEmail") QueryEmail  queryEmail);

    Page<ZsEmailVO> search(@Param("page") Page<ZsEmailVO> page, @Param("queryEmail") QueryEmail queryEmail);
}
