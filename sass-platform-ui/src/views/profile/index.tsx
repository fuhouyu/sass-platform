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


import {FC} from "react";
import "./index.scss"
import {UserOutlined} from "@ant-design/icons";
import {Avatar, Divider, Flex, message, Tooltip} from "antd";
import {useTranslation} from "react-i18next";
import {useUserStore} from "@/store";
import {IconFont, S3Upload} from "@/components";
import Layout, {Content, Header} from "antd/es/layout/layout";
import {useResourceAction} from "@/hooks/useResourceAction.tsx";
import {AccountsSetting} from "@/views/profile/components/AccountsSetting.tsx";


/**
 * 个人中心用户详情
 * @constructor 构造函数
 */
const UserProfile: FC = () => {
  const {t} = useTranslation();
  const {userinfo, fetchEditUserinfo} = useUserStore(state => state);
  const {preview} = useResourceAction();

  /**
   * 验证是否为图片
   * @param file 文件
   */
  const beforeUpload = async (file: File) => {
    const isImage = file.type.startsWith('image/');
    if (!isImage) {
      message.error('只能上传图片文件！');
    }
    return isImage;
  }

  return (
    <Layout className={'profile-container'}>
      <div className="profile-left">

        <div style={{textAlign: 'center'}}>

          <S3Upload
            accept={'image/*'}
            prefix={"user-avatar"}
            isPublic={true}
            showUploadList={false}
            beforeUpload={beforeUpload}
            onUploadSuccess={async (resourceId) => {
              await fetchEditUserinfo({...userinfo, avatar: resourceId});
            }}
          >
            <Tooltip
              className={'cursor-point'}
              title={t('User.updateAvatar')}>
              <Avatar
                size={{xs: 100, sm: 100, md: 100, lg: 100, xl: 100, xxl: 100}}
                icon={<UserOutlined/>}
                src={preview(userinfo.avatar)}
                className="avatar"
              />
            </Tooltip>
          </S3Upload>
          <div>
            <h2 className="text-align-center">
              {userinfo.realName}
            </h2>
          </div>
        </div>
        <div className={'profile-userinfo'}>
          <ul>
           <Flex vertical gap={10}>

             <li>
               <IconFont
                 type={'i-a-Identityshenfenzhiwei'}
                 style={{fontSize: '.9rem', width: '1.5rem'}}
               />
               <span style={{marginLeft: '0.5rem'}}>{userinfo.userPosition?.positionName}</span>
             </li>
             <li>
               <IconFont
                 type={'i-IPdizhi'}
                 style={{fontSize: '.9rem', width: '1.5rem'}}
               />
               <span style={{marginLeft: '0.5rem'}}>{userinfo.loginIp}</span>
             </li>
           </Flex>
          </ul>
        </div>

        <Divider/>

      </div>
      <div className="profile-right">
        <Content>
          <Header className="layout-header">
            <h2 className="profile-right-title">
              {t('Menu.profile')}
            </h2>
          </Header>
          <Divider/>
          <AccountsSetting/>
        </Content>
      </div>
    </Layout>

  );
}

export default UserProfile;
