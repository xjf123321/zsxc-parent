package com.zs.create.modules.system.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Read {

    private static final long serialVersionUID = 1L;

    private String readName;

    private String unreadName;
}
