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

import {Button, Card, Col, Form, Input, message, Radio, Select, Steps} from "antd";
import React, {Key, useEffect, useState} from "react";
import {ZH_CN_LANGUAGE} from "@/constants/commonConstant.tsx";
import {tenantApi} from "@/apis/tenant.tsx";
import {FormTree, OrganizationUserModal} from "@/components";
import {Menu} from "@/model/menu.tsx";
import TextArea from "antd/es/input/TextArea";
import {useTranslation} from "react-i18next";
import {TenantInfo} from "@/model/tenant.tsx";
import {useLocaleStore} from "@/store";
import './index.scss'
import {permissionApi} from "@/apis/permission.tsx";
import type {TableRowSelection} from "antd/es/table/interface";
import {Userinfo} from "@/model/user.tsx";
import {useParams} from "react-router-dom";

const TenantForm = () => {
    const [current, setCurrent] = useState(0);
    const {t} = useTranslation();
    const initForm: TenantInfo = {
        isEnabled: true,
    }
    const [tenantInfoForm] = Form.useForm();
    const [permissionIds, setPermissionIds] = useState<React.Key[]>([]);
    const [treeSelectData, setTreeSelectData] = useState<Menu[]>([]);
    const language = useLocaleStore((state) => state.language);
    const [updateId, setUpdateId] = useState<string>();
    const params = useParams();
    const [isChooseUserModalOpen, setIsChooseUserModalOpen] = useState<boolean>(false);

    useEffect(() => {
        const tenantId = params.tenantId;
        setUpdateId(tenantId);
        const init = async () => {
            if (tenantId) {
                const tenantInfo = await tenantApi.getInfoByIdApi(tenantId);
                tenantInfoForm.setFieldsValue({...tenantInfo})
            }
            const treeData = await permissionApi.getPermissionTreeSelect();
            setTreeSelectData(treeData);
        }
        init().then();
    }, [tenantInfoForm, params.tenantId]);

    /**
     * 处理租户
     */
    const handleTenant = async () => {
        const tenantInfo: TenantInfo = tenantInfoForm.getFieldsValue();
        tenantInfo.permissionIds = permissionIds;
        await tenantInfoForm.validateFields();
        await (updateId ? tenantApi.editInfoApi(updateId, tenantInfo) : tenantApi.saveInfoApi(tenantInfo));
        message.success(t('Common.success')).then();
    }

    /**
     * 用户列选择
     */
    const userRowSelection: TableRowSelection<Userinfo> = {
        onChange: (_: React.Key[], selectedRows: Userinfo[]) => {
            if (selectedRows.length === 0) {
                return
            }
            tenantInfoForm.setFieldValue('adminUserId', selectedRows[0].id);
            tenantInfoForm.setFieldValue('adminUserRealName', selectedRows[0].realName);
        },
    }


    const steps = [
        {
            title: '租户详情',
            content:
                <Form
                    layout={'inline'}
                    className={'tenant-form'}
                    clearOnDestroy={true}
                    name="modal-form"
                    form={tenantInfoForm}
                    labelCol={{span: language == ZH_CN_LANGUAGE ? 4 : 7}}
                    wrapperCol={{span: 15}}
                    autoComplete="off"
                    onFinish={handleTenant}
                    initialValues={initForm}
                >
                    <Col className={'form-item-col'} span={12}>
                        <Form.Item
                            label={t('Tenant.name')}
                            name="tenantName"
                            validateTrigger="onBlur"
                            key="tenantName"
                            colon={false}
                            required={true}
                            hasFeedback
                            rules={[{required: true, message: t('Tenant.namePlaceholder')}]}
                        >
                            <Input placeholder={t('Tenant.namePlaceholder')} maxLength={20}/>
                        </Form.Item>
                    </Col>
                    <Col className={'form-item-col'} span={12}>
                        <Form.Item
                            label={t('Tenant.code')}
                            name="tenantCode"
                            validateTrigger="onBlur"
                            key="tenantCode"
                            colon={false}
                            required={true}
                            hasFeedback={true}
                            validateFirst={true}
                            rules={updateId ? [] : [
                                {required: true, message: t('Tenant.codePlaceholder')},
                                {
                                    required: true,
                                    validator: async (_, value: string) => {
                                        if (value == null || value == '') {
                                            return;
                                        }
                                        const exists = await tenantApi.checkTenantCodeExists(value);
                                        if (exists) {
                                            return Promise.reject(new Error(t('Tenant.codeExistsErrorMessage')));
                                        }
                                    }
                                }
                            ]}
                        >
                            <Input disabled={updateId != null} placeholder={t('Tenant.codePlaceholder')}
                                   maxLength={20}/>
                        </Form.Item>
                    </Col>

                    <Col className={'form-item-col'} span={12}>
                        <Form.Item
                            hidden
                            name={['adminUserId']}
                            key="adminUserId"
                        >
                        </Form.Item>
                        <Form.Item
                            label={t('Tenant.adminUser')}
                            name={['adminUserRealName']}
                            validateTrigger="onBlur"
                            key="adminUserRealName"
                            colon={false}
                            required={true}
                            hasFeedback
                            rules={[{required: true, message: t('Tenant.adminUserPlaceholder')}]}
                        >
                            <Select
                                onDropdownVisibleChange={() => false}
                                allowClear
                                onClick={() => setIsChooseUserModalOpen(true)}
                                notFoundContent={null}
                                placeholder={t('Tenant.adminUserPlaceholder')}
                                mode="multiple"
                            />
                        </Form.Item>
                    </Col>

                    <Col className={'form-item-col'} span={12}>
                        <Form.Item
                            label={t('Tenant.contactPerson')}
                            name="contactPerson"
                            validateTrigger="onBlur"
                            key="contactPerson"
                            colon={false}
                            required={true}
                            hasFeedback
                            rules={[{required: true, message: t('Tenant.contactPersonPlaceholder')}]}
                        >
                            <Input placeholder={t('Tenant.contactPersonPlaceholder')} maxLength={20}/>
                        </Form.Item>
                    </Col>
                    <Col className={'form-item-col'} span={12}>
                        <Form.Item
                            label={t('Tenant.contactInfo')}
                            name="contactInfo"
                            validateTrigger="onBlur"
                            key="contactInfo"
                            colon={false}
                            required={true}
                            hasFeedback
                            rules={[{required: true, message: t('Tenant.contactInfoPlaceholder')}]}
                        >
                            <Input placeholder={t('Tenant.contactInfoPlaceholder')} maxLength={20}/>
                        </Form.Item>
                    </Col>
                    <Col className={'form-item-col'} span={12}>
                        <Form.Item
                            label={t('Common.status')}
                            name='isEnabled'
                            key="isEnabled"
                            colon={false}
                            hasFeedback
                        >
                            <Radio.Group>
                                <Radio value={true}>{t('Common.enabled')}</Radio>
                                <Radio value={false}>{t('Common.disabled')}</Radio>
                            </Radio.Group>
                        </Form.Item>
                    </Col>


                    <Col className={'form-item-col'} span={12}>
                        <Form.Item
                            label={t('Tenant.permissions')}
                            key="permissionIds"
                            name='permissionIds'
                            colon={false}
                            required={true}
                            hasFeedback={true}
                        >
                            <FormTree<Menu>
                                formTreeProps={{
                                    fieldNames: {key: 'id'},
                                    checkedKeys: permissionIds,
                                    onCheck: (checked: {
                                        checked: Key[];
                                        halfChecked: Key[];
                                    } | Key[]) => {
                                        if (checked instanceof Array) {
                                            setPermissionIds(checked);
                                            return
                                        }
                                        setPermissionIds(checked.checked);
                                    },
                                    titleRender: (menu: Menu) => t(`Menu.${menu.permissionName}`),
                                    treeData: treeSelectData,
                                }}
                                onSelectedAll={(ids: string[]) => setPermissionIds(ids)}
                            />
                        </Form.Item>
                    </Col>

                    <Col className={'form-item-col'} span={12}>
                        <Form.Item
                            label={t('Common.remark')}
                            name="remark"
                            key="remark"
                            colon={false}
                        >
                            <TextArea className="remark" placeholder={t('Common.remark')} showCount maxLength={500}/>
                        </Form.Item>
                    </Col>
                </Form>
        },
        {
            title: 'Second',
            content: 'Second-content',
        },
        {
            title: 'Last',
            content: 'Last-content',
        },
    ];

    const next = () => {
        setCurrent(current + 1);
    };

    const prev = () => {
        setCurrent(current - 1);
    };

    const items = steps.map((item) => ({key: item.title, title: item.title}));

    return (
        <>
            <Steps current={current} items={items}/>
            <Card className={'tenant-content'}>
                {steps[current].content}
            </Card>
            <div className={'step-action'}>
                {current > 0 && (
                    <Button style={{margin: '0 8px'}} onClick={() => prev()}>
                        上一步
                    </Button>
                )}
                {current === steps.length - 1 && (
                    <Button type="primary" onClick={() => message.success('Processing complete!')}>
                        完成
                    </Button>
                )}

                {current < steps.length - 1 && (
                    <Button type="primary" onClick={() => next()}>
                        下一步
                    </Button>
                )}
            </div>

            <OrganizationUserModal
                isModalOpen={isChooseUserModalOpen}
                setIsModalOpen={setIsChooseUserModalOpen}
                rowSelection={userRowSelection}
            />
        </>
    );
}


export default TenantForm;