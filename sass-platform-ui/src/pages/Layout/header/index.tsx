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


import {useTranslation} from "react-i18next";
import {useCallback, useEffect} from "react";
import useLanguageSwitcher from "@/hooks/useLanguageSwitcher.tsx";
import {useUserStore} from "@/store";
import {useNavigate} from "react-router-dom";
import {Avatar, Button, Divider, Dropdown, Flex, MenuProps, Segmented, Space, Tooltip} from "antd";
import {LogoutOutlined, MoonOutlined, SunOutlined, UserOutlined} from "@ant-design/icons";
import {Header} from "antd/es/layout/layout";
import './index.scss'
import {BaseUrlConstant} from "@/constants/baseUrlConstant.tsx";
import {useThemeStore} from "@/store/modules/theme.tsx";
import {useResourceAction} from "@/hooks/useResourceAction.tsx";
import {tenantApi} from "@/apis/tenant.tsx";

export const LayoutHeader = () => {
    const {t} = useTranslation();
    const {LanguageSwitcherButton} = useLanguageSwitcher('language-button');
    const {fetchUserinfo, fetchLogout, userinfo, storeTenant} = useUserStore(state => state);
    const navigate = useNavigate();
    const {tenant} = useUserStore(state => state);
    const {theme, changeTheme} = useThemeStore();
    const {preview} = useResourceAction();

    const setTenant = useCallback(async () => {
        storeTenant(await tenantApi.findTenantInfoForMe());
        await fetchUserinfo();
    }, [fetchUserinfo, storeTenant])

    useEffect(() => {
        setTenant().then();
    }, [setTenant])


    /**
     * 下拉选择框
     */
    const dropDownMenus: MenuProps['items'] = [
        {
            key: 'profile',
            label: t('Menu.profile'),
            icon: <UserOutlined/>,
            onClick: () => {
                navigate(BaseUrlConstant.USER_PROFILE_URL);
            }
        },
        {
            key: 'logout',
            label: t('Header.logout'),
            icon: <LogoutOutlined/>,
            onClick: async () => {
                await fetchLogout();
                navigate(BaseUrlConstant.LOGIN_URL);
            },
        },
    ];


    return (
        <>
            <Header className="layout-header">
                <div className={'header-title-container'}>
                    <Tooltip className="platform-title" title={t('Tenant.enterSpace')} placement={'right'}>
                        <Button type={'link'} onClick={() => navigate(BaseUrlConstant.TENANT_SPACE_URL)}>
                            <h2>
                                {tenant?.tenantName}
                            </h2>
                        </Button>
                    </Tooltip>

                    <Divider className={'header-title-divider'} type="vertical"/>
                </div>

                <Flex className={'header-actions-user-container'} justify={'center'} align={'center'} gap={20}>
                    <Flex>
                        <Segmented
                            shape="round"
                            defaultValue={theme}
                            options={[
                                {value: 'light', icon: <SunOutlined/>},
                                {value: 'dark', icon: <MoonOutlined/>},
                            ]}
                            onChange={(value: string) => changeTheme(value)}
                        />
                    </Flex>
                    <Flex>
                        {LanguageSwitcherButton}
                    </Flex>
                    <Flex className={'cursor-point'} justify={'center'} align={'center'}>
                        <Dropdown menu={{items: dropDownMenus}}>
                            <Space>
                                <Avatar size={24}
                                        icon={<UserOutlined/>}
                                        src={preview(userinfo.avatar)}
                                />
                                <span>{userinfo.realName}</span>
                            </Space>
                        </Dropdown>
                    </Flex>
                </Flex>

            </Header>
        </>
    )
}