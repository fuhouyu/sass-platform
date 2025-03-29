package com.fuhouyu.sass.platform.system.mapper;

import com.fuhouyu.sass.platform.system.domain.entity.OperationLog;

/**
 * <p>
 * ${describe}
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/28 21:25
 */
public interface OperationLogMapper {

    /**
     * 插入日志
     *
     * @param record 日志记录
     */
    void insert(OperationLog record);
}