package com.zs.create.modules.system.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description SysDepartUsersVO
 * @Author HeLiu
 * @Date 2019/9/28 10:01
 **/
@Data
public class SysDepartUsersVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 部门id
     */
    private String depId;
    /**
     * 对应的用户id集合
     */
    private List<String> userIdList;

    public SysDepartUsersVO(String depId, List<String> userIdList) {
        super();
        this.depId = depId;
        this.userIdList = userIdList;
    }


}
