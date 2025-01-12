package com.fuhouyu.sass.platform.system.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class TenantHasUser extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1612316435792834514L;


    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 用户id
     */
    private Long userId;


}