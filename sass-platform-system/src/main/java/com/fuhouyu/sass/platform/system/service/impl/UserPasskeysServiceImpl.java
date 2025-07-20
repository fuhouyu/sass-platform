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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.context.user.User;
import com.fuhouyu.sass.platform.system.components.properties.UserPasskeyProperties;
import com.fuhouyu.sass.platform.system.domain.dto.passkey.AuthenticationPasskeyDTO;
import com.fuhouyu.sass.platform.system.domain.dto.passkey.RegisterPasskeyDTO;
import com.fuhouyu.sass.platform.system.domain.dto.passkey.UserPasskeyListDTO;
import com.fuhouyu.sass.platform.system.domain.dto.user.UserTokenDTO;
import com.fuhouyu.sass.platform.system.domain.entity.UserPasskey;
import com.fuhouyu.sass.platform.system.mapper.UserPasskeyMapper;
import com.fuhouyu.sass.platform.system.service.UserPasskeysService;
import com.webauthn4j.WebAuthnManager;
import com.webauthn4j.converter.exception.DataConversionException;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.credential.CredentialRecord;
import com.webauthn4j.credential.CredentialRecordImpl;
import com.webauthn4j.data.*;
import com.webauthn4j.data.attestation.AttestationObject;
import com.webauthn4j.data.attestation.authenticator.AttestedCredentialData;
import com.webauthn4j.data.attestation.authenticator.AuthenticatorData;
import com.webauthn4j.data.attestation.authenticator.COSEKey;
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier;
import com.webauthn4j.data.client.CollectedClientData;
import com.webauthn4j.data.client.Origin;
import com.webauthn4j.data.client.challenge.Challenge;
import com.webauthn4j.data.client.challenge.DefaultChallenge;
import com.webauthn4j.data.extension.authenticator.RegistrationExtensionAuthenticatorOutput;
import com.webauthn4j.server.ServerProperty;
import com.webauthn4j.verifier.exception.VerificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户通行证密钥实现类
 * </p>
 *
 * @author fuhouyu
 * @since 2025/7/19 23:05
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserPasskeysServiceImpl extends ServiceImpl<UserPasskeyMapper, UserPasskey> implements UserPasskeysService {

    private static final List<PublicKeyCredentialParameters> DEFAULT_PUBLIC_KEY_CREDENTIAL_PARAMETERS_LIST;
    private static final ObjectConverter OBJECT_CONVERTER;
    private static final WebAuthnManager WEB_AUTHN_MANAGER;


    static {
        DEFAULT_PUBLIC_KEY_CREDENTIAL_PARAMETERS_LIST = List.of(
                new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.ES256),
                new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.RS256)
        );
        OBJECT_CONVERTER = new ObjectConverter();
        WEB_AUTHN_MANAGER = WebAuthnManager.createNonStrictWebAuthnManager();
    }

    private final UserPasskeyProperties userPasskeyProperties;

    @Override
    public String generateRegistrationOptions() {
        // Relying Party 信息
        PublicKeyCredentialCreationOptions publicKeyCredentialCreationOptions = getPublicKeyCredentialCreationOptions();
        return OBJECT_CONVERTER.getJsonConverter().writeValueAsString(publicKeyCredentialCreationOptions);
    }

    @Override
    public void registerPasskey(RegisterPasskeyDTO registerPasskey) {
        RegistrationData registrationData = this.parseRegistrationResponse(registerPasskey.getRegistrationResponse());
        RegistrationParameters registrationParameters = this.getRegistrationParameters();
        try {
            WEB_AUTHN_MANAGER.verify(registrationData, registrationParameters);
        } catch (VerificationException e) {
            LoggerUtil.error(log, "通行密钥验证失败,registrationData:[{}], serverProperty:[{}]", registrationData, registrationParameters, e);
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "通行密钥验证失败");
        }

        // 从 AttestationObject 中提取 credentialId 和 publicKey
        UserPasskey userPasskey = this.convertRegistrationData(registerPasskey.getPasskeyName(), registrationData);
        this.baseMapper.insert(userPasskey);
    }

    @Override
    public List<UserPasskeyListDTO> passkeyList() {
        LambdaQueryWrapper<UserPasskey> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPasskey::getUsername, ContextHolderStrategy.getContext().getUser().getUsername());
        List<UserPasskey> userPasskeys = this.baseMapper.selectList(queryWrapper);
        return userPasskeys.stream().map(userPasskey -> {
            UserPasskeyListDTO userPasskeyListDTO = new UserPasskeyListDTO();
            userPasskeyListDTO.setPasskeyName(userPasskey.getPasskeyName());
            userPasskeyListDTO.setPasskeyId(userPasskey.getPasskeyId());
            userPasskeyListDTO.setLastUseTime(userPasskey.getLastUseTime());
            userPasskeyListDTO.setCreatedAt(userPasskey.getCreatedAt());
            return userPasskeyListDTO;
        }).toList();
    }

    @Override
    public String getAttestationOptionsByUsername(String username) {
        LambdaQueryWrapper<UserPasskey> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPasskey::getUsername, username);
        List<UserPasskey> userPasskeys = this.baseMapper.selectList(queryWrapper);
        if (userPasskeys.isEmpty()) {
            return null;
        }
        List<PublicKeyCredentialDescriptor> allowCredentials = userPasskeys.stream()
                .map(passkey -> new PublicKeyCredentialDescriptor(
                        PublicKeyCredentialType.PUBLIC_KEY,
                        Base64.getUrlDecoder().decode(passkey.getPasskeyId()),
                        StringUtils.commaDelimitedListToSet(passkey.getTransports())
                                .stream().map(AuthenticatorTransport::create).collect(Collectors.toSet())
                ))
                .toList();

        return OBJECT_CONVERTER.getJsonConverter().writeValueAsString(new PublicKeyCredentialRequestOptions(
                new DefaultChallenge(username.getBytes(StandardCharsets.UTF_8)),
                60000L,
                this.userPasskeyProperties.getDomain(),
                allowCredentials,
                UserVerificationRequirement.REQUIRED,
                null
        ));
    }

    @Override
    public void verifyAuthentication(AuthenticationPasskeyDTO authenticationPasskeyDTO) {
        AuthenticationData authenticationData = this.parseAuthenticationData(authenticationPasskeyDTO.getAuthentication());
        ServerProperty serverProperty = getServerProperty(authenticationData);
        // Step 3: 从数据库中查找 Credential（即用户注册通行密钥时保存的）
        byte[] credentialId = authenticationData.getCredentialId();
        LambdaQueryWrapper<UserPasskey> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPasskey::getPasskeyId, Base64.getUrlEncoder().withoutPadding().encodeToString(credentialId));
        queryWrapper.eq(UserPasskey::getUsername, authenticationPasskeyDTO.getUsername());
        UserPasskey userPasskey = this.baseMapper.selectOne(queryWrapper);
        if (Objects.isNull(userPasskey)) {
            throw new ServiceException(ResponseStatusEnum.NOT_FOUND,
                    "通行证密钥不存在");
        }
        CredentialRecord credentialRecord = this.getCredentialRecord(userPasskey);

        // Step 4: 准备认证参数
        AuthenticationParameters authenticationParameters = getAuthenticationParameters(credentialId, serverProperty, credentialRecord);

        try {
            WEB_AUTHN_MANAGER.verify(authenticationData, authenticationParameters);
        } catch (VerificationException e) {
            LoggerUtil.error(log, "通行密钥验证失败，authenticationData: {}, authenticationParameters: {}", authenticationData, authenticationParameters, e);
            // 可以自定义异常抛出，或者包装成业务异常
            throw new ServiceException(ResponseStatusEnum.SERVER_ERROR, "通行密钥验证失败");
        }
    }

    /**
     * 解析RegistrationData
     *
     * @param registrationResponseJson json串
     * @return RegistrationData
     */
    private RegistrationData parseRegistrationResponse(String registrationResponseJson) {
        try {
            return WEB_AUTHN_MANAGER.parseRegistrationResponseJSON(registrationResponseJson);
        } catch (DataConversionException e) {
            LoggerUtil.error(log, "解析通行密钥的json失败, json 串:{}", registrationResponseJson, e);
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM, "Parse registration response JSON failed.");
        }
    }

    /**
     * 获取注册参数
     *
     * @return 注册参数
     */
    private RegistrationParameters getRegistrationParameters() {
        Origin origin = new Origin(String.format("https://%s", this.userPasskeyProperties.getDomain()));
        Challenge challenge = new DefaultChallenge(ContextHolderStrategy.getContext().getUser().getUsername().getBytes(StandardCharsets.UTF_8));
        ServerProperty serverProperty = new ServerProperty(origin, this.userPasskeyProperties.getDomain(), challenge);
        return new RegistrationParameters(serverProperty, DEFAULT_PUBLIC_KEY_CREDENTIAL_PARAMETERS_LIST,
                false, true);
    }

    /**
     * 解析AuthenticationData
     *
     * @param authenticationResponseJson 认证的json字符串
     * @return AuthenticationData
     */
    private AuthenticationData parseAuthenticationData(String authenticationResponseJson) {

        AuthenticationData authenticationData;
        try {
            authenticationData = WEB_AUTHN_MANAGER.parseAuthenticationResponseJSON(authenticationResponseJson);
        } catch (DataConversionException e) {
            LoggerUtil.error(log, "解析通行密钥认证的json失败, json 串:{}", authenticationResponseJson, e);
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM, "Parse authentication response JSON failed.");
        }
        return authenticationData;
    }

    /**
     * 获取公钥凭证创建选项
     *
     * @return 公钥凭证创建选项
     */
    private PublicKeyCredentialCreationOptions getPublicKeyCredentialCreationOptions() {

        User user = ContextHolderStrategy.getContext().getUser();
        DefaultChallenge defaultChallenge = new DefaultChallenge(user.getUsername().getBytes(StandardCharsets.UTF_8));

        // 用户信息
        PublicKeyCredentialUserEntity userEntity = new PublicKeyCredentialUserEntity(
                user.getUsername().getBytes(StandardCharsets.UTF_8),
                user.getUsername(),
                user.getRealName()
        );

        PublicKeyCredentialRpEntity rpEntity = new PublicKeyCredentialRpEntity(
                userPasskeyProperties.getDomain(),
                userPasskeyProperties.getName()
        );

        return new PublicKeyCredentialCreationOptions(
                rpEntity,
                userEntity,
                defaultChallenge,
                DEFAULT_PUBLIC_KEY_CREDENTIAL_PARAMETERS_LIST,
                60000L,
                Collections.emptyList(),
                new AuthenticatorSelectionCriteria(
                        AuthenticatorAttachment.PLATFORM,
                        ResidentKeyRequirement.PREFERRED,
                        UserVerificationRequirement.REQUIRED
                ),
                AttestationConveyancePreference.NONE,
                null
        );
    }

    /**
     * 通过通行密钥获取CredentialRecord
     *
     * @param userPasskey 通行密钥
     * @return CredentialRecord
     */
    private CredentialRecord getCredentialRecord(UserPasskey userPasskey) {
        AttestationObject attestationObject = OBJECT_CONVERTER.getCborConverter().readValue(Base64.getUrlDecoder()
                .decode(userPasskey.getAttestationObject()), AttestationObject.class);
        if (Objects.isNull(attestationObject)) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "attestationObject is null");
        }
        CollectedClientData collectedClientData = OBJECT_CONVERTER.getJsonConverter().readValue(userPasskey.getClientData(), CollectedClientData.class);
        return new CredentialRecordImpl(attestationObject, collectedClientData, null, StringUtils.commaDelimitedListToSet(userPasskey.getTransports())
                .stream().map(AuthenticatorTransport::create).collect(Collectors.toSet()));
    }

    /**
     * 转换为注册的data
     *
     * @param keyName          ke name
     * @param registrationData 注册的data数据
     * @return 用户通行密钥
     */
    private UserPasskey convertRegistrationData(String keyName, RegistrationData registrationData) {
        // 1. 解析 AttestationObject 和 AuthenticatorData
        AttestationObject attestationObject = registrationData.getAttestationObject();
        AuthenticatorData<RegistrationExtensionAuthenticatorOutput> authData = null;
        if (attestationObject != null) {
            authData = attestationObject.getAuthenticatorData();
        }

        // 2. 获取 CredentialId 和 PublicKey（字节数组）
        byte[] credentialIdBytes = null;
        if (authData != null && authData.getAttestedCredentialData() != null) {
            credentialIdBytes = authData.getAttestedCredentialData().getCredentialId();
        }
        AttestedCredentialData attestedCredentialData = null;
        if (authData != null) {
            attestedCredentialData = authData.getAttestedCredentialData();
        }
        COSEKey coseKey = null;
        if (attestedCredentialData != null) {
            coseKey = attestedCredentialData.getCOSEKey();
        }
        byte[] publicKeyBytes = null;
        if (coseKey != null) {
            publicKeyBytes = Objects.requireNonNull(coseKey.getPublicKey()).getEncoded();
        }
        byte[] aaguidBytes = null;
        if (authData != null) {
            aaguidBytes = authData.getAttestedCredentialData().getAaguid().getBytes();
        }
        String credentialId = Base64.getUrlEncoder().withoutPadding().encodeToString(credentialIdBytes);
        String publicKey = Base64.getUrlEncoder().withoutPadding().encodeToString(publicKeyBytes);
        String aaguid = Base64.getUrlEncoder().withoutPadding().encodeToString(aaguidBytes);
        String clientData = OBJECT_CONVERTER.getJsonConverter().writeValueAsString(registrationData.getCollectedClientData());
        String attestationObjStr = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(OBJECT_CONVERTER.getCborConverter().writeValueAsBytes(attestationObject));
        String transportsJson = registrationData.getTransports() == null ? null :
                OBJECT_CONVERTER.getJsonConverter().writeValueAsString(registrationData.getTransports());

        UserPasskey userPasskey = new UserPasskey();
        userPasskey.setUsername(ContextHolderStrategy.getContext().getUser().getUsername());
        userPasskey.setPasskeyName(keyName);
        userPasskey.setPasskeyId(credentialId);
        userPasskey.setPublicKey(publicKey);
        userPasskey.setAaguid(aaguid);
        userPasskey.setClientData(clientData);
        userPasskey.setAttestationObject(attestationObjStr);
        userPasskey.setSignCount(Objects.isNull(authData) ? 0 : authData.getSignCount());
        userPasskey.setTransports(transportsJson);

        return userPasskey;
    }

    /**
     * 获取认证参数信息
     * @param credentialId 凭证id
     * @param serverProperty serverProperty
     * @param credentialRecord credentialRecord
     * @return 认证参数信息
     */
    private  AuthenticationParameters getAuthenticationParameters(byte[] credentialId, ServerProperty serverProperty, CredentialRecord credentialRecord) {
        List<byte[]> allowCredentials = Collections.singletonList(credentialId);
        boolean userVerificationRequired = true;
        boolean userPresenceRequired = true;
        return new AuthenticationParameters(
                serverProperty,
                credentialRecord,
                allowCredentials,
                userVerificationRequired,
                userPresenceRequired
        );
    }


    /**
     * 获取serverProperty
     * @param authenticationData 认证的data数据
     * @return serverProperty
     */
    private ServerProperty getServerProperty(AuthenticationData authenticationData) {
        Origin origin = new Origin(String.format("https://%s", this.userPasskeyProperties.getDomain()));
        String rpId = this.userPasskeyProperties.getDomain();
        if (Objects.isNull(authenticationData.getCollectedClientData())) {
            throw new ServiceException(ResponseStatusEnum.INVALID_PARAM,
                    "认证信息的challenge不正确");
        }
        Challenge challenge = authenticationData.getCollectedClientData().getChallenge();
        return new ServerProperty(origin, rpId, challenge);
    }
}
