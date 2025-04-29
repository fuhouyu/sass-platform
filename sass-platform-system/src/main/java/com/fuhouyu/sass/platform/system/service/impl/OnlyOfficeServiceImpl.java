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

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.common.utils.JacksonUtil;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.context.DefaultListableContextFactory;
import com.fuhouyu.framework.context.user.UserEntity;
import com.fuhouyu.framework.security.token.TokenStore;
import com.fuhouyu.sass.platform.system.components.office.OfficeFileContext;
import com.fuhouyu.sass.platform.system.components.security.UserAccountAuthenticationToken;
import com.fuhouyu.sass.platform.system.domain.dto.office.OnlyOfficeCallbackDTO;
import com.fuhouyu.sass.platform.system.domain.dto.office.OnlyOfficeResponseDTO;
import com.fuhouyu.sass.platform.system.domain.dto.resource.ResourceDetailDTO;
import com.fuhouyu.sass.platform.system.service.OnlyOfficeService;
import com.fuhouyu.sass.platform.system.service.ResourceService;
import com.fuhouyu.sass.platform.system.utils.ChunkDownloadUtil;
import com.onlyoffice.manager.url.UrlManager;
import com.onlyoffice.model.documenteditor.Config;
import com.onlyoffice.model.documenteditor.config.document.Type;
import com.onlyoffice.model.documenteditor.config.editorconfig.Mode;
import com.onlyoffice.service.documenteditor.config.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.UploadPartResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.fuhouyu.sass.platform.common.constants.HttpRequestAdditionalConstant.USER_ADDITIONAL_INFORMATION_PERMISSIONS;

/**
 * <p>
 * office实现
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/26 22:54
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OnlyOfficeServiceImpl implements OnlyOfficeService {

    private final TokenStore tokenStore;

    private final ResourceService resourceService;

    private final S3Client s3Client;

    private final ConfigService configService;

    private final UrlManager urlManager;


    @Override
    public OnlyOfficeResponseDTO view(Long id, String mode) {
        ResourceDetailDTO resourceDTO = this.resourceService.checkResourcePermission(id);
        OfficeFileContext.set(resourceDTO);
        try {
            String userAgentString = ContextHolderStrategy.getContext().getRequest().getUserAgent();
            UserAgent userAgent = UserAgentUtil.parse(userAgentString);
            Config config = configService.createConfig(String.valueOf(id), Mode.valueOf(mode.toUpperCase(Locale.ROOT)),
                    userAgent.isMobile() ? Type.MOBILE : Type.DESKTOP);
            return OnlyOfficeResponseDTO.builder()
                    .config(config)
                    .documentServerApiUrl(urlManager.getDocumentServerApiUrl())
                    .documentServerUrl(urlManager.getDocumentServerUrl())
                    .build();
        } finally {
            OfficeFileContext.clear();
        }
    }

    @Override
    public void saveFile(Long id, String token, OnlyOfficeCallbackDTO onlyOfficeCallbackDTO) {
        this.createUserContext(token);
        ResourceDetailDTO resourceDTO = this.resourceService.findDetailById(id);
        // 生成s3的
        CreateMultipartUploadResponse multipartUpload = this.s3Client.createMultipartUpload(builder ->
                builder.bucket(resourceDTO.getBucketName())
                        .key(resourceDTO.getObjectKey())
                        .contentType(resourceDTO.getMimeType())
        );
        String uploadId = multipartUpload.uploadId();
        // 下载文件并上传
        List<CompletedPart> completedParts = new ArrayList<>();
        ChunkDownloadUtil.downloadFile(onlyOfficeCallbackDTO.getUrl(), (partNum, bytes, partSize) -> {
            // 分片上传
            UploadPartResponse uploadPartResponse = this.s3Client.uploadPart(builder ->
                    builder.bucket(resourceDTO.getBucketName())
                            .key(resourceDTO.getObjectKey())
                            .uploadId(uploadId)
                            .partNumber(partNum)
                            .contentLength((long) bytes.length), RequestBody.fromBytes(bytes));
            completedParts.add(CompletedPart.builder()
                    .partNumber(partNum)
                    .eTag(uploadPartResponse.eTag())
                    .build());
        });
        // 完成分片上传
        this.s3Client.completeMultipartUpload(builder -> builder.bucket(resourceDTO.getBucketName())
                .key(resourceDTO.getObjectKey())
                .multipartUpload(CompletedMultipartUpload.builder().parts(completedParts).build())
                .uploadId(uploadId)
        );
        // 获取版本号
        String versionId = this.s3Client.headObject(builder -> builder.bucket(resourceDTO.getBucketName()).key(resourceDTO.getObjectKey())).versionId();
        resourceDTO.setVersion(versionId);
        this.resourceService.edit(resourceDTO);
    }


    /**
     * 创建用户上下文，如果用户不存在，则抛出异常
     *
     * @param token token
     */
    private void createUserContext(String token) {
        Authentication authentication = this.tokenStore.readAuthentication(token);
        if (Objects.isNull(authentication)) {
            LoggerUtil.error(log, "token: {}, 认证信息不存在，保存office失败", token);
            throw new ServiceException(ResponseStatusEnum.TOKEN_EXPIRE);
        }
        UserEntity userEntity = JacksonUtil.tryParse(() -> JacksonUtil.getObjectMapper().convertValue(authentication.getDetails(),
                UserEntity.class));
        userEntity.putAdditionalInformation(USER_ADDITIONAL_INFORMATION_PERMISSIONS, authentication.getAuthorities());
        if (authentication instanceof UserAccountAuthenticationToken userAccountAuthenticationToken) {
            userEntity.putAdditionalInformation(userAccountAuthenticationToken.getLoginUserDetails());
        }
        userEntity.setSessionId(token);
        DefaultListableContextFactory defaultListableContextFactory = new DefaultListableContextFactory();
        defaultListableContextFactory.setUser(userEntity);
        ContextHolderStrategy.setContext(defaultListableContextFactory);
    }
}
