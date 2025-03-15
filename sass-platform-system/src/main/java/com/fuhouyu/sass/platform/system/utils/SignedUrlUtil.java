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
import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
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

    private static final String RANDOM_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final String SIGNED_ALGORITHM = "HmacSHA256";

    /**
     * 生成带签名的 API URL
     *
     * @param baseUrl      API 地址
     * @param urlSignedDTO url 签名的dto 对象
     * @return 带签名的 URL
     */
    public static String generateSignedUrl(String baseUrl,
                                           UrlSignedDTO urlSignedDTO) {

        Map<String, Object> signParamsMap = getSignParamsMap(urlSignedDTO);

        // 生成签名
        String secretKey = urlSignedDTO.getSecretKey();
        String signature = generateSignature(signParamsMap, secretKey);
        signParamsMap.put("signature", signature);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        signParamsMap.forEach(builder::queryParam);
        return builder.build().toUriString();
    }


    /**
     * 检查签名是否有效，失效将会抛出异常
     *
     * @param urlSignedDTO 签名的dto对象
     */
    public static void verifySignedUrl(VerifySignedUrlDTO urlSignedDTO) {
        Long expires = urlSignedDTO.getExpires();
        // 检查 URL 是否过期
        long currentTime = Instant.now().getEpochSecond();
        if (Objects.isNull(expires) || currentTime > expires) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM, "当前url签名已过期");
        }
        Map<String, Object> signParamsMap = getSignParamsMap(urlSignedDTO);
        // 生成签名
        String secretKey = urlSignedDTO.getSecretKey();
        String expectedSignature = generateSignature(signParamsMap, secretKey);
        // 比较签名
        String originSignedData = urlSignedDTO.getSignature();
        if (!Objects.equals(originSignedData, expectedSignature)) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "当前签名url已失效");
        }
    }


    /**
     * 生成签名
     *
     * @param params    请求参数（已包含 accessKey, timestamp, nonce）
     * @param secretKey 签名密钥
     * @return HMAC-SHA256 签名字符串
     */
    private static String generateSignature(Map<String, Object> params, String secretKey) {
        // 1. 按照 key 进行字典序排序
        List<String> sortedKeys = new ArrayList<>(params.keySet());
        Collections.sort(sortedKeys);

        // 2. 构造签名字符串
        StringBuilder signData = new StringBuilder();
        for (String key : sortedKeys) {
            signData.append(key).append("=").append(params.get(key)).append("&");
        }
        // 移除最后的 "&"
        signData.setLength(signData.length() - 1);

        // 3. 使用 HMAC-SHA256 计算签名
        return hmacSha256(signData.toString(), secretKey);
    }

    /**
     * 生成随机字符串（nonce）
     *
     * @return 随机字符串
     */
    private static String generateNonce() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            sb.append(RANDOM_CHARS.charAt(random.nextInt(RANDOM_CHARS.length())));
        }
        return sb.toString();
    }

    /**
     * 计算 HMAC-SHA256 签名
     *
     * @param data      需要加密的数据
     * @param secretKey 密钥
     * @return HMAC-SHA256 签名
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
     * 生成需要名称的map 对象
     *
     * @param urlSignedDTO dto对象
     * @return 需要签名的map对象
     */
    private static Map<String, Object> getSignParamsMap(UrlSignedDTO urlSignedDTO) {
        String accessKey = urlSignedDTO.getAccessKey();
        Map<String, Object> params = urlSignedDTO.getParams();
        if (Objects.isNull(params)) {
            params = new HashMap<>(3);
        }
        if (urlSignedDTO instanceof VerifySignedUrlDTO verifySignedUrlDTO) {
            params.put("accessKey", accessKey);
            params.put("nonce", verifySignedUrlDTO.getNonce());
            params.put("expires", verifySignedUrlDTO.getExpires());

        } else {
            params.put("accessKey", accessKey);
            params.put("nonce", generateNonce());
            long expirationTime = Instant.now().getEpochSecond() + urlSignedDTO.getExpiresSeconds();
            params.put("expires", expirationTime);
        }

        return params;
    }


    /**
     * url签名dto对象
     */
    @Data
    @SuperBuilder
    public static class UrlSignedDTO {

        /**
         * ak
         */
        private String accessKey;

        /**
         * sk
         */
        private String secretKey;

        /**
         * 参数
         */
        private Map<String, Object> params;

        /**
         * url过期时间
         */
        private long expiresSeconds;

    }

    /**
     * 检查url签名的dto对象
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder
    public static class VerifySignedUrlDTO extends UrlSignedDTO {


        /**
         * 随机数
         */
        private String nonce;

        /**
         * 过期时间
         */
        private Long expires;

        /**
         * 当前签名值
         */
        private String signature;
    }
}
