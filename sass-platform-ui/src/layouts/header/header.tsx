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


import {Avatar, Card, Col, Dropdown, Flex, Image, MenuProps, Modal, Row, Space} from "antd";
import {Bread, IconFont} from "@/components";
import {DownOutlined, LogoutOutlined, UserOutlined} from "@ant-design/icons";
import {Header as _Header} from "antd/es/layout/layout";
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import type {ItemType} from "antd/es/menu/interface";
import './index.scss'
import {useTranslation} from "react-i18next";
import {BASE_LOGIN_URL, BASE_USER_PROFILE_URL} from "@/constants/commonConstant";
import useTenant from "@/hooks/useTenant";
import {tenantApi} from "@/apis/tenant";
import useLanguageSwitcher from "@/hooks/useLanguageSwitcher";
import {useUserStore} from "@/store";


export const Header = () => {

    const {t} = useTranslation();
    const [switchTenantModalOpen, setSwitchTenantModalOpen] = useState<boolean>(false);
    const {LanguageSwitcherButton} = useLanguageSwitcher('language-button');
    const tenantInfos = useTenant();
    const {fetchUserinfo, fetchLogout, userinfo, storeTenant} = useUserStore(state => state);

    useEffect(() => {
        fetchUserinfo().then();
        const currentTenant = tenantInfos.find(t => t.id === userinfo.tenantId);
        storeTenant(currentTenant);
    }, [fetchUserinfo, storeTenant, tenantInfos, userinfo, userinfo.tenantId])

    const navigate = useNavigate();

    /**
     * 下拉选择框
     */
    const dropDownMenus: MenuProps['items'] = [
        {
            key: 'profile',
            label: t('Menu.profile'),
            icon: <UserOutlined/>,
        },
        {
            key: 'logout',
            label: t('Header.logout'),
            icon: <LogoutOutlined/>,
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

    // onClick
    const onDropDownClick: MenuProps['onClick'] = async (e: ItemType) => {
        if (!e) {
            return;
        }
        switch (e.key) {
            case 'logout':
                await fetchLogout();
                navigate(BASE_LOGIN_URL);
                break;
            case 'profile':
                navigate(BASE_USER_PROFILE_URL);
                break;
        }
    };

    return (
        <>
            <_Header className="layout-header">
                <Row gutter={24} align={"middle"}>
                    <Col>
                        <Bread/>
                    </Col>
                    <Col className="user-header">
                        {LanguageSwitcherButton}
                        <div>
                                      <span className="tenant">
                               <IconFont type='i-qiehuan' onClick={() => setSwitchTenantModalOpen(true)}/>
                               </span>
                            <Dropdown menu={{
                                items: dropDownMenus,
                                onClick: onDropDownClick
                            }}>
                           <span>

                                <Space>
                                    {t('Common.welcome')},{userinfo.realName}
                                    <Image
                                        className="avatar"
                                        preview={false}
                                        fallback="https://oss.fuhouyu.com/2.jpeg"
                                    />
                                    <DownOutlined/>
                                </Space>
                           </span>
                            </Dropdown>
                        </div>
                    </Col>
                </Row>

            </_Header>
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