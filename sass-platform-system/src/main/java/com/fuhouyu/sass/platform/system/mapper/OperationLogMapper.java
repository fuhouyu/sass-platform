package com.fuhouyu.sass.platform.system.mapper;

import com.fuhouyu.framework.database.annotations.TenantQuery;
import com.fuhouyu.sass.platform.system.domain.dto.log.OperationLogPageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.entity.OperationLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 查询列表
     *
     * @param pageQuery 分页查询
     * @return 操作日志集合
     */
    @TenantQuery
    List<OperationLog> queryList(@Param("pageQuery") OperationLogPageQueryDTO pageQuery);

    /**
     * 获取模块列表
     *
     * @return 模块列表
     */
    List<String> getModuleList();

    /**
     * 通过id查询出操作日志
     *
     * @param id 主键id
     * @return 操作日志
     */
    @TenantQuery
    OperationLog queryById(Long id);
}