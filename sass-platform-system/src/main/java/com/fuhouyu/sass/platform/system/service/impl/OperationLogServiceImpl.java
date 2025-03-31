/*
 * Copyright 2024-2025 fuhouyu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fuhouyu.sass.platform.system.service.impl;

import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.context.request.Request;
import com.fuhouyu.framework.context.user.User;
import com.fuhouyu.framework.log.core.LogRecordStoreService;
import com.fuhouyu.framework.log.model.LogRecordEntity;
import com.fuhouyu.sass.platform.common.constants.HttpRequestAdditionalConstant;
import com.fuhouyu.sass.platform.common.utils.SnowflakeIdWorker;
import com.fuhouyu.sass.platform.system.domain.dto.log.OperationLogDTO;
import com.fuhouyu.sass.platform.system.domain.dto.log.OperationLogPageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.page.PageResultDTO;
import com.fuhouyu.sass.platform.system.domain.entity.OperationLog;
import com.fuhouyu.sass.platform.system.mapper.OperationLogMapper;
import com.fuhouyu.sass.platform.system.service.OperationLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 操作日志接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/28 21:42
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OperationLogServiceImpl implements OperationLogService, LogRecordStoreService {

    private final OperationLogMapper operationLogMapper;

    private final SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public void saveLogRecord(LogRecordEntity logRecordEntity) {
        User user = ContextHolderStrategy.getContext().getUser();
        Request request = ContextHolderStrategy.getContext().getRequest();
        Long tenantId = Objects.isNull(user) ?
                request.getAdditionalInformation(HttpRequestAdditionalConstant.TENANT_ADDITIONAL_INFORMATION_ID)
                : user.getTenantId();

        OperationLog operationLog = new OperationLog();

        BeanUtils.copyProperties(logRecordEntity, operationLog);
        String requestLocation =
                request.getAdditionalInformation(HttpRequestAdditionalConstant.IP_LOCATION_ADDITIONAL_INFORMATION);
        operationLog.setRequestLocation(requestLocation);
        operationLog.setId(snowflakeIdWorker.nextId());

        operationLog.setOwnerTenantId(tenantId);
        operationLog.setOperationTime(LocalDateTime.parse(logRecordEntity.getOperationTime(), LogRecordEntity.DATE_TIME_FORMATTER));
        this.operationLogMapper.insert(operationLog);
    }

    @Override
    public PageResultDTO<OperationLogDTO> page(OperationLogPageQueryDTO operationLogPageQueryDTO) {
        try (Page<Object> page = PageMethod.startPage(operationLogPageQueryDTO.getPageNum(), operationLogPageQueryDTO.getPageSize())) {
            page.setUnsafeOrderBy(operationLogPageQueryDTO.getOrderBy());
            List<OperationLog> operationLogs = this.operationLogMapper.queryList(operationLogPageQueryDTO);
            List<OperationLogDTO> list = operationLogs.stream()
                    .map(log -> {
                        OperationLogDTO dto = new OperationLogDTO();
                        BeanUtils.copyProperties(log, dto);
                        return dto;
                    }).toList();
            return new PageResultDTO<>(page.getPageNum(),
                    page.getPageSize(), page.getTotal(),
                    list);
        }
    }
}
