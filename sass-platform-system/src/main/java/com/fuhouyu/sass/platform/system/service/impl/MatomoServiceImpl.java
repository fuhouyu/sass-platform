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
import com.fuhouyu.sass.platform.system.domain.dto.matomo.MatomoVisitQueryDTO;
import com.fuhouyu.sass.platform.system.domain.dto.matomo.MatomoVisitSummaryDTO;
import com.fuhouyu.sass.platform.system.service.MatomoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
    public Map<String, MatomoVisitSummaryDTO> getVisitSummary(MatomoVisitQueryDTO queryDTO) {
        // 创建一个RestClient对象，并设置Matomo的API地址
        String body = RestClient.create(matomoProperties.getApiUrl())
                .get()
                .uri(uriBuilder ->
                        uriBuilder.queryParam("module", "API")
                                .queryParam("format", "json")
                                .queryParam("token_auth", matomoProperties.getToken())
                                .queryParam("method", "VisitsSummary.get")
                                .queryParam("idSite", matomoProperties.getSiteId())
                                .queryParam("period", queryDTO.getPeriod().toLowerCase())
                                .queryParam("date", queryDTO.getDate())
                                .build()
                ).accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);
        HashMap<String, JsonNode> rawMap = JacksonUtil.readValue(body, new TypeReference<HashMap<String, JsonNode>>() {
        });
        Map<String, MatomoVisitSummaryDTO> finalMap = new TreeMap<>();

        for (Map.Entry<String, JsonNode> entry : rawMap.entrySet()) {
            if (entry.getValue().isObject()) {
                MatomoVisitSummaryDTO dto = JacksonUtil.tryParse(() -> JacksonUtil.getObjectMapper().convertValue(entry.getValue(), MatomoVisitSummaryDTO.class));
                finalMap.put(entry.getKey(), dto);
            } else {
                finalMap.put(entry.getKey(), null);
            }
        }
        return finalMap;

    }
}
