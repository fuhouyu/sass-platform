package com.fuhouyu.sass.platform.system.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * <p>
 * 用户角色实体
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/13 22:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserHasRole extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 182936196731987123L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;
}