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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fuhouyu.framework.common.utils.JacksonUtil;
import com.fuhouyu.sass.platform.system.components.properties.MatomoProperties;
import com.fuhouyu.sass.platform.system.domain.dto.matomo.MatomoCountryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.matomo.MatomoVisitQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.matomo.MatomoVisitSummaryDTO;
import com.fuhouyu.sass.platform.system.service.MatomoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.*;

/**
 * <p>
 * matomo 接口实现
 * </p>
 *
 * @author fuhouyu
 * @since 2025/5/4 20:13
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = MatomoProperties.PREFIX,
        name = {"api-url", "token", "site-id"}
)
public class MatomoServiceImpl implements MatomoService {

    private final MatomoProperties matomoProperties;

    @Override
    public List<MatomoVisitSummaryDTO> getVisitSummary(MatomoVisitQueryDTO queryDTO) {
        String body = this.request("VisitsSummary.get", queryDTO, String.class);
        Map<String, JsonNode> rawMap = JacksonUtil.readValue(body, new TypeReference<HashMap<String, JsonNode>>() {
        });
        List<MatomoVisitSummaryDTO> list = new ArrayList<>(rawMap.size());

        for (Map.Entry<String, JsonNode> entry : rawMap.entrySet()) {
            if (entry.getValue().isObject()) {
                MatomoVisitSummaryDTO dto = JacksonUtil.tryParse(() -> JacksonUtil.getObjectMapper().convertValue(entry.getValue(), MatomoVisitSummaryDTO.class));
                dto.setDate(entry.getKey());
                list.add(dto);
            } else {
                MatomoVisitSummaryDTO matomoVisitSummaryDTO = new MatomoVisitSummaryDTO();
                matomoVisitSummaryDTO.setDate(entry.getKey());
                list.add(matomoVisitSummaryDTO);
            }
        }
        list.sort(Comparator.comparing(MatomoVisitSummaryDTO::getDate));
        return list;

    }

    @Override
    public List<MatomoCountryDTO> getCountryVisit(MatomoVisitQueryDTO queryDTO) {
        HashMap<String, ArrayList<MatomoCountryDTO>> resultsMap = this.request("UserCountry.getCountry", queryDTO, new ParameterizedTypeReference<>() {
        });
        List<MatomoCountryDTO> list = new ArrayList<>(resultsMap.size());
        resultsMap.forEach((key, values) -> {
            values.forEach(value -> value.setDate(key));
            list.addAll(values);
        });
        list.sort(Comparator.comparing(MatomoCountryDTO::getDate));
        return list;
    }


    /**
     * 执行请求
     *
     * @param method   方法
     * @param queryDTO 查询dto对象
     * @param bodyType 返回值类型
     * @param <T>      返回值类型
     * @return 返回请求结果
     */
    private <T> T request(String method, MatomoVisitQueryDTO queryDTO, Class<T> bodyType) {
        return this.doRequest(method, queryDTO).body(bodyType);
    }

    /**
     * 执行请求
     *
     * @param method   方法
     * @param queryDTO 查询dto对象
     * @param bodyType 返回值类型
     * @param <T>      返回值类型
     * @return 返回请求结果
     */
    private <T> T request(String method, MatomoVisitQueryDTO queryDTO, ParameterizedTypeReference<T> bodyType) {
        return this.doRequest(method, queryDTO).body(bodyType);
    }


    /**
     * 执行请求
     *
     * @param method   方法名称
     * @param queryDTO 查询条件
     * @return 请求结果
     */
    private RestClient.ResponseSpec doRequest(String method, MatomoVisitQueryDTO queryDTO) {
        return RestClient.create(matomoProperties.getApiUrl())
                .get()
                .uri(uriBuilder ->
                        uriBuilder.queryParam("module", "API")
                                .queryParam("format", "json")
                                .queryParam("token_auth", matomoProperties.getToken())
                                .queryParam("method", method)
                                .queryParam("idSite", matomoProperties.getSiteId())
                                .queryParam("period", queryDTO.getPeriod().toLowerCase())
                                .queryParam("date", queryDTO.getDate())
                                .build()
                ).accept(MediaType.APPLICATION_JSON)
                .retrieve();
    }
}
