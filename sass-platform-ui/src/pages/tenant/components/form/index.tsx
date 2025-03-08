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

import {
    Avatar,
    Button,
    Col,
    DatePicker,
    Flex,
    Form,
    Input,
    InputNumber,
    message,
    Radio,
    Select,
    Space,
    Steps,
    Tooltip
} from "antd";
import React, {Key, useCallback, useEffect, useState} from "react";
import {tenantApi} from "@/apis/tenant.tsx";
import {FormTree, OrganizationUserModal, S3Upload} from "@/components";
import {Menu} from "@/model/menu.tsx";
import TextArea from "antd/es/input/TextArea";
import {useTranslation} from "react-i18next";
import {TenantInfo, TenantSpace} from "@/model/tenant.tsx";
import {useLocaleStore} from "@/store";
import './index.scss'
import type {TableRowSelection} from "antd/es/table/interface";
import {Userinfo} from "@/model/user.tsx";
import {tenantSpaceApi} from "@/apis/tenantSpace.tsx";
import {CommonConstant} from "@/constants/commonConstant";
import {TenantFormProps} from "@/pages/tenant/components/form/interface.ts";
import {useResourceAction} from "@/hooks/useResourceAction.tsx";
import dayjs, {Dayjs} from 'dayjs';

interface _TenantForm extends TenantInfo {
    dateRange?: Dayjs[] | null[]
}

const DATE_FORMAT = 'YYYY-MM-DD';

const TenantForm = (tenantFormProps: TenantFormProps) => {
    const {tenantId, callback, permissionTreeData} = tenantFormProps;
    const [current, setCurrent] = useState(0);
    const {t} = useTranslation();
    const [tenantInfoForm] = Form.useForm<_TenantForm>();
    const [tenantSpaceForm] = Form.useForm<TenantSpace>();
    const [tenantInfo, setTenantInfo] = useState<_TenantForm | undefined>(undefined);
    const [tenantSpace, setTenantSpace] = useState<TenantSpace | undefined>(undefined);
    const [permissionIds, setPermissionIds] = useState<React.Key[]>([]);
    const language = useLocaleStore((state) => state.language);
    const [isChooseUserModalOpen, setIsChooseUserModalOpen] = useState<boolean>(false);
    const {preview} = useResourceAction();

    /**
     * 查询租户
     */
    const queryTenant = useCallback(async () => {
        if (tenantId === undefined) {
            return
        }
        const res = await tenantApi.getInfoByIdApi(tenantId);
        setPermissionIds(res.permissionIds ?? []);
        tenantInfoForm.setFieldsValue({...res});
        setTenantInfo(res);
    }, [tenantId, tenantInfoForm]);


    useEffect(() => {
        if (tenantInfo === undefined) {
            queryTenant().then();
        } else {
            const {startDate, endDate} = tenantInfo;
            const dateRange = [
                startDate ? dayjs(startDate) : null,
                endDate ? dayjs(endDate) : null,
            ];
            tenantInfoForm.setFieldsValue({...tenantInfo, dateRange});
        }
    }, [queryTenant, tenantInfo, tenantInfoForm])


    useEffect(() => {
        if (current !== 1) {
            return
        }
        const initSpaceForm = async () => {
            if (tenantId) {
                const res = await tenantSpaceApi.getTenantSpaceByTenantId(tenantId);
                setTenantSpace({...res})
                tenantSpaceForm.setFieldsValue({...res})
            }
        };
        if (tenantSpace === undefined) {
            initSpaceForm().then();
        } else {
            tenantSpaceForm.setFieldsValue({...tenantSpace})
        }

    }, [current, tenantId, tenantSpace, tenantSpaceForm]);

    /**
     * 处理租户
     */
    const handleTenant = async () => {
        await tenantSpaceForm.validateFields();
        const tenant = tenantInfo!
        const [startDate, endDate] = tenant.dateRange ?? [];
        tenant.startDate = startDate?.format(DATE_FORMAT);
        tenant.endDate = endDate?.format(DATE_FORMAT)
        tenant.tenantSpace = {...tenantSpaceForm.getFieldsValue()}
        tenant.permissionIds = permissionIds;
        await (tenantId ? tenantApi.editInfoApi(tenantId, tenant) : tenantApi.saveInfoApi(tenant));
        message.success(t('Common.success'));
        callback()
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


    const next = async () => {
        if (current === 0) {
            await tenantInfoForm.validateFields()
            setTenantInfo(tenantInfoForm.getFieldsValue());
        }
        setCurrent(current + 1);
    };

    const prev = () => {
        setCurrent(current - 1);
        tenantInfoForm.setFieldsValue({...tenantInfo})
    };

    const steps = [
        {
            title: t('Tenant.basicInfo'),
            content:
                <Form
                    layout={'inline'}
                    className={'tenant-form'}
                    clearOnDestroy={true}
                    name="tenant-form"
                    form={tenantInfoForm}
                    labelCol={{span: language == CommonConstant.ZH_CN_LANGUAGE ? 5 : 7}}
                    wrapperCol={{span: 18}}
                    autoComplete="off"
                    initialValues={{
                        isEnabled: true,
                    }}
                >
                    <Col className={'form-item-col'}
                         span={24}>
                        <Flex align={"center"} justify={'center'}>
                            <Form.Item
                                name="icon"
                                key="icon"
                                colon={false}
                                required={true}
                                hasFeedback
                            >
                                <S3Upload
                                    uploadProps={{
                                        prefix: 'user-avatar',
                                        isPublic: true,
                                        onUploadSuccess: async (resourceId) => {
                                            setTenantInfo({...tenantInfo, icon: resourceId})
                                            message.success(t('Common.success'));
                                        },
                                    }}
                                >
                                    <Tooltip
                                        className={'cursor-point'}
                                        title={t('Tenant.updateIcon')}>
                                        <Avatar
                                            size={100}
                                            src={preview(tenantInfo?.icon)}
                                        />
                                    </Tooltip>
                                </S3Upload>
                            </Form.Item>
                        </Flex>
                    </Col>
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
                            rules={tenantId ? [] : [
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
                            <Input disabled={tenantId != null} placeholder={t('Tenant.codePlaceholder')}
                                   maxLength={20}/>
                        </Form.Item>
                    </Col>

                    <Col className={'form-item-col'} span={12}>
                        <Form.Item
                            hidden
                            name={['adminUserId']}
                            key="adminUserId"
                        >
                            <Input hidden/>
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
                            required
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
                                    treeData: permissionTreeData,

                                }}
                                onSelectedAll={(ids: string[]) => setPermissionIds(ids)}
                            />
                        </Form.Item>
                    </Col>

                    <Col className={'form-item-col'} span={12}>
                        <Form.Item
                            name="dateRange"
                            label={t('Tenant.startAndEndDate')}
                            colon={false}
                            hasFeedback={true}
                        >
                            <DatePicker.RangePicker
                                placeholder={[t('Tenant.startDatePlaceholder'), t('Tenant.endDatePlaceholder')]}
                                disabledDate={(current) => current && current < dayjs().subtract(1, 'day')}
                                format="YYYY-MM-DD"
                            />
                        </Form.Item>
                    </Col>

                    <Col className={'form-item-col'} span={24} style={{
                        paddingTop: '1rem',
                    }}>
                        <Form.Item
                            label={t('Common.remark')}
                            labelCol={{span: 2}}
                            wrapperCol={{span: 22}}
                            name="remark"
                            key="remark"
                            colon={false}
                        >
                            <TextArea className="remark"
                                      style={{
                                          height: '10rem',
                                      }}
                                      placeholder={t('Common.remark')} showCount maxLength={500}/>
                        </Form.Item>
                    </Col>
                </Form>
        },
        {
            title: t('Tenant.space'),
            content: <div>
                <Form
                    className={'space-form'}
                    clearOnDestroy={true}
                    name="space-form"
                    form={tenantSpaceForm}
                    labelCol={{span: language == CommonConstant.ZH_CN_LANGUAGE ? 4 : 7}}
                    wrapperCol={{span: 15, offset: 2}}
                    autoComplete="off"
                    initialValues={{
                        acl: 'private'
                    }}
                >
                    <Form.Item
                        label={t('Tenant.bucketName')}
                        key="bucketName"
                        name='bucketName'
                        colon={false}
                        required={true}
                        validateTrigger="onBlur"
                        hasFeedback={true}
                        rules={tenantId ? [] : [{required: true, message: t('Tenant.bucketNamePlaceholder')},
                            {
                                required: true,
                                validator: async (_, value: string) => {
                                    if (value == null || value == '') {
                                        return;
                                    }
                                    const exists = await tenantSpaceApi.checkSpaceNameExists(value);
                                    if (exists) {
                                        return Promise.reject(new Error(t('Tenant.bucketNameExistsErrorMessage')));
                                    }
                                }
                            }]}
                    >
                        <Input disabled={tenantId !== undefined} placeholder={t('Tenant.bucketNamePlaceholder')}
                               maxLength={20}/>
                    </Form.Item>
                    <Space/>

                    <Form.Item
                        label={t('Tenant.spaceAcl')}
                        key="acl"
                        name='acl'
                        colon={false}
                        required={true}
                        validateTrigger="onBlur"
                        hasFeedback={true}
                        rules={[
                            {required: true, message: t('Tenant.spaceAclPlaceholder')},
                        ]}
                    >
                        <Radio.Group
                            options={[
                                {value: 'private', label: t('Tenant.privateAcl')},
                                {value: 'public-read', label: t('Tenant.publicReadAcl')},
                                {value: 'public-read-write', label: t('Tenant.publicWriteAcl')},
                                {value: 'authenticated-read', label: t('Tenant.authenticationRead')},
                            ]}
                        />
                    </Form.Item>

                    <Form.Item
                        label={t('Tenant.capacity')}
                        key="capacity"
                        name='capacity'
                        colon={false}
                        required={true}
                        validateTrigger="onBlur"
                        hasFeedback={true}
                        rules={[
                            {required: true, message: t('Tenant.capacityPlaceholder')},
                        ]}
                    >
                        <InputNumber addonAfter="Gi"/>
                    </Form.Item>
                    <Space/>

                </Form>
            </div>
        },
    ];


    const items = steps.map((item) => ({key: item.title, title: item.title}));

    return (
        <>
            <Steps current={current} items={items}/>
            <div className={'tenant-content'}>{steps[current].content}</div>
            <div className={'step-action'}>
                {current > 0 && (
                    <Button style={{margin: '0 8px'}} onClick={() => prev()}>
                        上一步
                    </Button>
                )}
                {current === steps.length - 1 && (
                    <Button type="primary" onClick={handleTenant}>
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