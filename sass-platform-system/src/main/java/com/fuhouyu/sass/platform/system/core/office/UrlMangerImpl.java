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
package com.fuhouyu.sass.platform.system.core.office;

import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.sass.platform.system.domain.dto.resource.ResourceDetailDTO;
import com.fuhouyu.sass.platform.system.utils.BaseUrlUtil;
import com.onlyoffice.manager.settings.SettingsManager;
import com.onlyoffice.manager.url.DefaultUrlManager;
import lombok.NonNull;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * <p>
 * url 管理实现
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/2 15:32
 */
public class UrlMangerImpl extends DefaultUrlManager {

    private final S3Presigner s3Presigner;

    public UrlMangerImpl(SettingsManager settingsManager, S3Presigner s3Presigner) {
        super(settingsManager);
        this.s3Presigner = s3Presigner;
    }

    @Override
    public String getFileUrl(@NonNull String fileId) {

        ResourceDetailDTO resourceDetailDTO = OfficeFileContext.get();
        PresignedGetObjectRequest presignedGetObjectRequest = this.s3Presigner.presignGetObject(request -> {
            request.signatureDuration(Duration.ofHours(1));
            request.getObjectRequest(getObject -> {
                getObject.responseContentType(resourceDetailDTO.getMimeType());
                getObject.bucket(resourceDetailDTO.getBucketName())
                        .key(resourceDetailDTO.getObjectKey())
                        .versionId(resourceDetailDTO.getVersion())
                        .responseContentDisposition(String.format("attachment; filename=\"%s\"",
                                URLEncoder.encode(resourceDetailDTO.getName(), StandardCharsets.UTF_8)));
            });
        });
        return presignedGetObjectRequest.url().toExternalForm();
    }

    @Override
    public String getCallbackUrl(String fileId) {
        return String.format("%s/v1/office/%s/callback?token=%s", BaseUrlUtil.getBaseUrl(), fileId,
                URLEncoder.encode(ContextHolderStrategy.getContext().getUser().getSessionId(), StandardCharsets.UTF_8));
    }
}
