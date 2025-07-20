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
import './index.scss'
import {Button, Divider, Flex, Input, message} from "antd";
import logo from '@/assets/images/logo.png'
import {useState} from "react";
import {passkeyApi} from "@/apis/passkey.ts";
import {useNavigate} from "react-router-dom";

export const AddPasskey = () => {
  const navigate = useNavigate();
  const [passkeyName, setPasskeyName] = useState<string>();
  return (
    <Flex className={'passkey-add-container'} align={'center'} justify={'center'}>
      <div className={'passkey-add-body'}>
        <Flex className={'passkey-add-header'} align={'center'}>
          <img
            width={42}
            height={42}
            src={logo} alt="logo"
            style={{mixBlendMode: 'multiply'}}
          />
          <h1>Sass Platform</h1>
        </Flex>

        <Flex className={'passkey-add-content'} justify={'space-between'} align={'center'} gap={20}>
          <Flex className={'add-content-left'} flex={1} vertical gap={25}>
            <div className={'passkey-bind-title'}><h3>绑定通行密钥</h3></div>
            <p>
              创建通行密钥允许您使用设备支持的认证方式（可能包括指纹识别、人脸识别、安全密钥等）进行登录。也可使用通行密钥作为多因素认证手段进行登录二次认证和操作认证。
              通行密钥采用 FIDO2 标准的加密技术，使认证更方便和安全。
            </p>
            <span>
                 <p>绑定步骤如下：</p>
                  <ol>
                    <li><p>1. 输入自定义通行密钥名称。</p></li>
                    <li><p>2. 触发绑定，在设备上选择通行密钥类型并进行认证。</p></li>
                    <li><p>3. 完成通行密钥的创建。</p></li>
                  </ol>
            </span>
          </Flex>

          <Divider type="vertical" variant={'solid'} style={{
            height: '240px',
            backgroundColor: 'black'
          }}/>

          <Flex className={'add-content-right'} flex={1} vertical gap={25}>
            <div className={'passkey-bind-account'}>
              <span>账号 admin</span>
            </div>
            <p>
              通行密钥名称
            </p>
            <Input placeholder={'请输入名称'} value={passkeyName}
                   onChange={e => setPasskeyName(e.target.value)}></Input>

            <div>
              <Button disabled={passkeyName === undefined} onClick={async () => {
                const response = await passkeyApi.passkeyGeneratorApi();
                const cleanedOptions = JSON.parse(response);
                const credentialCreationOptions = PublicKeyCredential.parseCreationOptionsFromJSON(cleanedOptions);
                const publicKeyCredential = await navigator.credentials.create({publicKey: credentialCreationOptions}); // create PublicKeyCredential
                await passkeyApi.registerPasskeyApi({
                  passkeyName: passkeyName!,
                  registrationResponse: JSON.stringify(publicKeyCredential)
                });
                message.success("通行证密钥创建成功");
                navigate('/profile')
              }}>开始绑定</Button>
            </div>
          </Flex>
        </Flex>
      </div>
    </Flex>
  );
}
