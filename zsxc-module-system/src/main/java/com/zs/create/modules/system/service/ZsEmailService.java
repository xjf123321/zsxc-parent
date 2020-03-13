package com.zs.create.modules.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.create.modules.system.entity.ZsEmail;
import com.zs.create.modules.system.vo.ZsEmailVO;

/**
 * <p>
 * 邮件表service接口
 * </p>
 * @Author yaochao
 * @since 2019-10-14
 */
public interface ZsEmailService extends IService<ZsEmail> {

    /**
     * 添加邮箱表数据
     *
     * @param zsEmail
     * @param ZsEmailVO
     */
     void addEmail(ZsEmailVO zsEmailVO);

    /**
     * 分页查询
     *
     * @param Page
     * @param queryWrapper
     */
     Page<ZsEmailVO> queryPage(Page<ZsEmailVO> page, QueryWrapper queryWrapper);

    /**
     * 跟新邮件表del_flag状态
     * @param id
     */
     void updateDel(String id);

    /**
     * * 批量跟新邮件表del_flag状态
     * @param id
     */
     void  updateByIds(String ids);

    /**
     * 根据id查邮件
     * @param id
     */
    ZsEmailVO findById(String id);

    /**
     * 收件箱邮件检索
     * @param Page
     * @param ZsEmailVO
     */
    Page<ZsEmailVO> searchInbox(Page<ZsEmailVO> page, ZsEmailVO zsEmailVO, String keyword);

    /**
     * 发件箱邮件检索
     * @param Page
     * @param ZsEmailVO
     */
    Page<ZsEmailVO> searchOutbox(Page<ZsEmailVO> page, ZsEmailVO zsEmailVO, String keyword);

    /**
     * 草稿箱邮件检索
     * @param Page
     * @param queryEmail
     */
    Page<ZsEmailVO> searchDrafts(Page<ZsEmailVO> page, ZsEmailVO zsEmailVO, String keyword);

    /**
     * 垃圾箱邮件检索
     * @param Page
     * @param ZsEmailVO
     */
    Page<ZsEmailVO> searchDustbin(Page<ZsEmailVO> page,  ZsEmailVO zsEmailVO, String keyword);

    void addDraft(ZsEmailVO zsEmailVO);

    void updateStatus(ZsEmail zsEmail);

    ZsEmailVO reply(String id);

    ZsEmailVO forward(String id);
}
