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
package com.fuhouyu.sass.platform.system.utils;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.common.utils.JacksonUtil;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

/**
 * <p>
 * 签名url生成的工具类
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/9 18:46
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class SignedUrlUtil {

    private static final String SIGNED_ALGORITHM = "HmacSHA256";

    /**
     * 生成带签名的 API URL
     *
     * @param baseUrl   API 地址
     * @param signedDTO 请求参数
     * @return 带签名的 URL
     */
    public static String generateSignedUrl(String baseUrl, SignedUrlDTO signedDTO) {
        Map<String, Object> signParamsMap = getSignParamsMap(signedDTO);

        // 生成签名
        String signature = generateSignature(signParamsMap, signedDTO.getSecretKey());
        signParamsMap.put("signature", signature);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        signParamsMap.forEach(builder::queryParam);
        return builder.build().toUriString();
    }

    /**
     * 验证签名
     *
     * @param signedDTO 含签名的 DTO
     */
    public static void verifySignedUrl(SignedUrlDTO signedDTO) {
        long currentTime = Instant.now().getEpochSecond();
        if (signedDTO.getExpires() == null || currentTime > signedDTO.getExpires()) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM, "当前url签名已过期");
        }

        Map<String, Object> signParamsMap = getSignParamsMap(signedDTO);
        String expectedSignature = generateSignature(signParamsMap, signedDTO.getSecretKey());

        if (!Objects.equals(signedDTO.getSignature(), expectedSignature)) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM, "当前签名url已失效");
        }
    }

    /**
     * 获取签名参数 Map（排除 signature）
     */
    private static Map<String, Object> getSignParamsMap(SignedUrlDTO dto) {
        Map<String, Object> params = JacksonUtil.tryParse(() -> JacksonUtil.getObjectMapper()
                .convertValue(dto, new TypeReference<TreeMap<String, Object>>() {
                }));
        params.putAll(dto.getParams());
        return params;
    }

    /**
     * 生成签名
     */
    private static String generateSignature(Map<String, Object> params, String secretKey) {
        List<String> sortedKeys = new ArrayList<>(params.keySet());
        Collections.sort(sortedKeys);

        StringBuilder signData = new StringBuilder();
        for (String key : sortedKeys) {
            signData.append(key).append("=").append(params.get(key)).append("&");
        }
        signData.setLength(signData.length() - 1);

        return hmacSha256(signData.toString(), secretKey);
    }

    /**
     * 生成 HMAC-SHA256 签名
     */
    private static String hmacSha256(String data, String secretKey) {
        try {
            Mac mac = Mac.getInstance(SIGNED_ALGORITHM);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SIGNED_ALGORITHM);
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64Encoder.encodeUrlSafe(hash);
        } catch (Exception e) {
            LoggerUtil.error(log, "{} 签名失败, data: {} , secretKey: {}", SIGNED_ALGORITHM, data, secretKey);
            throw new IllegalArgumentException(String.format("%s 算法签名url失败: ", SIGNED_ALGORITHM), e);
        }
    }

    /**
     * 签名 DTO
     */
    @Data
    public static class SignedUrlDTO {

        /**
         * ak
         */
        private String accessKey;

        /**
         * sk
         */
        @JsonIgnore
        private String secretKey;

        /**
         * 参数
         */
        @JsonIgnore
        private Map<String, Object> params;

        /**
         * 随机数（防重放）
         */
        private String nonce;

        /**
         * 过期时间戳（秒）
         */
        private Long expires;

        /**
         * 签名（用于校验）
         */
        @JsonIgnore
        private String signature;


        @Builder(builderMethodName = "signedBuilder")
        public SignedUrlDTO(String accessKey,
                            String secretKey,
                            Map<String, Object> params,
                            String signature,
                            Long expires,
                            long expiresSeconds,
                            String nonce) {
            this.accessKey = accessKey;
            this.secretKey = secretKey;
            this.params = params == null ? Collections.emptyMap() : params;
            this.signature = signature;

            // 初始化 nonce 和 expires 如果没有提供
            this.nonce = nonce != null ? nonce : RandomUtil.randomString(16);
            this.expires = Objects.isNull(expires) ? Instant.now().getEpochSecond() + expiresSeconds : expires;
        }
    }
}