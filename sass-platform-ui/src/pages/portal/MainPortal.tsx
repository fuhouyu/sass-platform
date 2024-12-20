/*
 * Copyright 2024-2024 the original author or authors.
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
import {Avatar, Button, Card, Col, Divider, Flex, Row} from "antd";
import useTenant from "@/hooks/useTenant";
import {useTranslation} from "react-i18next";
import {useState} from "react";
import {tenantApi} from "@/apis/tenant";
import {useLocation} from "react-router-dom";
import {parseRouters, router} from "@/routes/routers";
import {fetchUserMenus} from "@/store/modules/user";
import {useAppDispatch} from "@/store";

/**
 * 门户页
 */
export const MainPortal = () => {

    const tenantInfos = useTenant();
    const {t} = useTranslation();
    const [chooseTenant, setChooseTenant] = useState<string>();
    const location = useLocation();
    const dispatch = useAppDispatch();
    const confirm = async () => {
        if (!chooseTenant) {
            return
        }
        await tenantApi.switchTenant(chooseTenant);
        const fromRouter = location.state?.from;
        const from = (fromRouter && fromRouter.endsWith('login')) ? '/' : fromRouter || '/';
        router.routes[0]?.children!.push(...parseRouters(await dispatch(fetchUserMenus())))
        router.navigate(from).then()
    }


    return (
        <Flex gap="middle" justify={'center'} className={'portal-container'} onClick={() => setChooseTenant(undefined)}
              vertical>
            <Divider orientation="center">{t('Tenant.list')}</Divider>
            <Row gutter={24}>
                {
                    tenantInfos.map(tenant => {
                        return <Col span={8} key={tenant.id}>
                            <Card
                                onClick={(e) => {
                                    e.stopPropagation();
                                    setChooseTenant(tenant.id)
                                }}
                                key={tenant.id}
                                className={`switch-tenant-container ${chooseTenant === tenant.id ? 'tenant-active' : ''}`}
                                hoverable
                            >
                                <Card.Meta
                                    avatar={<Avatar src="https://api.dicebear.com/7.x/miniavs/svg?seed=1"/>}
                                    title={tenant.tenantName}
                                    description={<p>{tenant.remark}</p>}
                                />
                            </Card>
                        </Col>
                    })
                }

            </Row>
            <Divider orientation="center"/>
            <Flex justify={'center'}>
                <Button onClick={confirm} type="primary">{t('Button.confirm')}</Button>
            </Flex>
        </Flex>

    )
}