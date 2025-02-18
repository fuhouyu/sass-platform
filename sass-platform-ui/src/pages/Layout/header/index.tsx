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
import {useCallback, useEffect, useState} from "react";
import useLanguageSwitcher from "@/hooks/useLanguageSwitcher.tsx";
import useTenant from "@/hooks/useTenant.tsx";
import {useUserStore} from "@/store";
import {useNavigate} from "react-router-dom";
import {Avatar, Card, Divider, Dropdown, Flex, MenuProps, Modal, Space} from "antd";
import {LogoutOutlined, UserOutlined} from "@ant-design/icons";
import {tenantApi} from "@/apis/tenant.tsx";
import {BASE_LOGIN_URL, BASE_USER_PROFILE_URL} from "@/constants/commonConstant.tsx";
import {Header} from "antd/es/layout/layout";
import './index.scss'
import {BaseUrlConstant} from "@/constants/baseUrlConstant.tsx";

export const LayoutHeader = () => {
    const {t} = useTranslation();
    const [switchTenantModalOpen, setSwitchTenantModalOpen] = useState<boolean>(false);
    const {LanguageSwitcherButton} = useLanguageSwitcher('language-button');
    const tenantInfos = useTenant();
    const {fetchUserinfo, fetchLogout, userinfo, storeTenant} = useUserStore(state => state);
    const navigate = useNavigate();
    const {tenant} = useUserStore(state => state);

    const setTenant = useCallback(async () => {
        if (tenantInfos.length === 0) {
            return;
        }
        const currentUserinfo = await fetchUserinfo();
        const currentTenant = tenantInfos.find(t => t.id === currentUserinfo.tenantId);
        storeTenant(currentTenant);
    }, [fetchUserinfo, storeTenant, tenantInfos])

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
                navigate(BASE_USER_PROFILE_URL);
            }
        },
        {
            key: 'logout',
            label: t('Header.logout'),
            icon: <LogoutOutlined/>,
            onClick: async () => {
                await fetchLogout();
                navigate(BASE_LOGIN_URL);
            },
        },
    ];

    /**
     * 租户切换
     * @param tenantId 租户id
     */
    const switchTenant = async (tenantId: string) => {
        await tenantApi.switchTenant(tenantId);
        setSwitchTenantModalOpen(false);
        window.location.reload();
    }


    return (
        <>
            <Header className="layout-header">
                <div className={'header-title-container'}>
                    <h2 className="platform-title">
                        {tenant?.tenantName}
                    </h2>
                    <Divider className={'header-title-divider'} type="vertical"/>
                </div>

                <Flex className={'header-actions-user-container'} justify={'center'} align={'center'} gap={20}>
                    <Flex>
                        {LanguageSwitcherButton}
                    </Flex>
                    <Flex justify={'center'} align={'center'}>
                        <Dropdown menu={{items: dropDownMenus}}>
                            <Space>
                                <Avatar size={24}
                                        src={userinfo.avatar ? `${import.meta.env.VITE_API_URL}${BaseUrlConstant.RESOURCE_API_PREFIX}/preview/${userinfo.avatar}` : ''}
                                />
                                <span>{userinfo.realName}</span>
                            </Space>
                        </Dropdown>
                        {/*<Space>*/}


                        {/*<Image*/}
                        {/*    className="avatar"*/}
                        {/*    preview={false}*/}
                        {/*    fallback="https://oss.fuhouyu.com/2.jpeg"*/}
                        {/*/>*/}
                        {/*<DownOutlined/>*/}
                        {/*</Space>*/}
                    </Flex>
                </Flex>

                {/*<Row gutter={24} align={"middle"}>*/}
                {/*    <Col>*/}
                {/*        <h2 className="platform-title">*/}
                {/*            {tenant?.tenantName}*/}
                {/*        </h2>*/}
                {/*    </Col>*/}
                {/*    <Col className="user-header">*/}
                {/*        <Flex>*/}
                {/*            {LanguageSwitcherButton}*/}
                {/*        </Flex>*/}
                {/*        <div className={'header-actions-avatar'}>*/}
                {/*            /!*       <span className="tenant">*!/*/}
                {/*            /!*<IconFont type='i-qiehuan' onClick={() => setSwitchTenantModalOpen(true)}/>*!/*/}
                {/*            /!*</span>*!/*/}
                {/*            <Dropdown menu={{*/}
                {/*                items: dropDownMenus,*/}
                {/*                onClick: onDropDownClick*/}
                {/*            }}>*/}
                {/*           <span>*/}

                {/*                <Space>*/}
                {/*                    {t('Common.welcome')},{userinfo.realName}*/}
                {/*                    <Image*/}
                {/*                        className="avatar"*/}
                {/*                        preview={false}*/}
                {/*                        fallback="https://oss.fuhouyu.com/2.jpeg"*/}
                {/*                    />*/}
                {/*                    <DownOutlined/>*/}
                {/*                </Space>*/}
                {/*           </span>*/}
                {/*            </Dropdown>*/}
                {/*        </div>*/}
                {/*    </Col>*/}
                {/*</Row>*/}

            </Header>
            <Modal
                centered
                destroyOnClose={true}
                title={t('Tenant.list')}
                closable={false}
                onCancel={() => setSwitchTenantModalOpen(false)}
                open={switchTenantModalOpen}
                width={'auto'}
                footer={[]}>
                <Flex justify="space-around" vertical>
                    {tenantInfos?.map(tenant => {
                        return <Card
                            onClick={() => switchTenant(tenant.id!)}
                            key={tenant.id}
                            className={"switch-tenant-container"}
                            hoverable
                        >
                            <Card.Meta
                                avatar={<Avatar src="https://api.dicebear.com/7.x/miniavs/svg?seed=1"/>}
                                title={tenant.tenantName}
                                description={<p>{tenant.remark}</p>}
                            />

                        </Card>

                    })}
                </Flex>
            </Modal>
        </>
    )
}