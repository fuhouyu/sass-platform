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


import React, {Key, useEffect, useRef, useState} from "react";
import {
    Button,
    Col,
    Form,
    Input,
    InputNumber,
    message,
    Popconfirm,
    Radio,
    Row,
    Splitter,
    Switch,
    TableColumnsType,
    Tooltip,
    Tree,
    TreeSelect
} from "antd";
import {permissionApi} from "@/apis/permission";
import {Menu} from "@/model/menu";
import './index.scss'
import {useTranslation} from "react-i18next";
import {IconFont, Modal, PermissionButton, SearchHeader, Table} from "@/components";
import {AddButton, DeleteButton, EditButton} from "@components/Button/commonButton";
import type {TableRowSelection} from "antd/es/table/interface";
import {AnyObject} from "antd/es/_util/type";
import {PermissionConstant} from "@/constants/permissionConstant.tsx";
import {useButton} from "@/hooks/useButton";
import {useLocaleStore} from "@/store";
import {CommonConstant} from "@/constants/commonConstant";
import useRouteSearchParams from "@/hooks/useRouteSearchParams";
import {TableRefType} from "@components/List/table/interface.tsx";
import {usePageTitle} from "@/hooks/usePageTitle.tsx";


/**
 * 设置树数据
 * @param list 菜单集合
 * @param key key
 * @param children 子集
 */
const updateTreeData = (list: Menu[], key: React.Key, children: Menu[]): Menu[] => {
    return list.map((node: Menu) => {
        if (node.id === key) {
            return {
                ...node,
                children,
            };
        }
        if (node.children) {
            return {
                ...node,
                children: updateTreeData(node.children, key, children),
            };
        }
        return node;
    });

}

export const Permission: React.FC = () => {
    usePageTitle('Menu.permissionManage');
    const [treeSelectData, setTreeSelectData] = useState<Menu[]>([]);

    const {t} = useTranslation();
    const buttonPermissions = useButton(PermissionConstant.List);
    const [rowKeys, setRowKeys] = useState<React.Key[]>([]);
    const [updateId, setUpdateId] = useState<string | undefined>();
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const [form] = Form.useForm();
    const tableRef = useRef<TableRefType<Menu>>(null);
    const {querySearchParams, updateSearchParams} = useRouteSearchParams();
    const [permissionQuery, setPermissionQuery] = useState<Record<string, string>>({
        ...querySearchParams()
    });
    const [isModalButtonLoading, setIsModalButtonLoading] = useState<boolean>(false);
    const [formParentPermission, setFormParentPermission] = useState<Menu>({} as Menu);
    const [lazyTreeData, setLazyTreeData] = useState<Menu[]>([]);
    const language = useLocaleStore((state) => state.language);

    const columns: TableColumnsType<Menu> = [
        {
            title: t('Permission.name'),
            dataIndex: 'permissionName',
            showSorterTooltip: {target: 'full-header'},
            align: 'center',
            render: (permissionName: string) => t(`${permissionName}`)
        },
        {
            title: t('Permission.code'),
            dataIndex: 'permissionCode',
            align: 'center',
        },
        {
            title: t('Common.displayOrder'),
            dataIndex: 'displayOrder',
            align: 'center',
            sorter: true,
            defaultSortOrder: 'descend',
        },
        {
            title: t('Common.status'),
            dataIndex: 'isEnabled',
            align: 'center',
            render: (_, record: Menu) => (
                <Switch
                    disabled={!record.isAllowModified}
                    defaultChecked={record.isEnabled} onChange={async (checked) => {
                    await permissionApi.status(record.id!, checked);
                    await tableRef?.current?.refreshPageList();
                }}/>
            )
        },
        {
            title: t('Common.updatedAt'),
            dataIndex: 'updatedAt',
            align: 'center',
            showSorterTooltip: false
        },
        {
            title: t('Common.updatedBy'),
            align: 'center',
            dataIndex: 'updatedBy',
        },
        {
            title: t('Common.action'),
            dataIndex: 'action',
            align: 'center',
            width: 120,
            fixed: 'right',
            render: (_: AnyObject, record: Menu) => {
                return (<>
                    <PermissionButton buttonPermissions={buttonPermissions} permissionStr={PermissionConstant.EDIT}>
                        <EditButton disabled={!record.isAllowModified} onClick={() => openModal(record.id)}/>
                    </PermissionButton>
                </>)
            }
        }
    ];
    /**
     * table列选择
     */
    const rowSelection: TableRowSelection<Menu> = {
        onChange: (selectedRowKeys: React.Key[]) => setRowKeys(selectedRowKeys),
        getCheckboxProps: (record: Menu) => ({
            disabled: !record.isAllowModified
        }),
    };

    /**
     * 初始化权限
     */
    const initPermission = async () => {
        const menus = await permissionApi.getPermissionListApi();
        setLazyTreeData([...menus]);
    }

    /**
     * 左侧菜单树
     */
    useEffect(() => {
        initPermission().then();
    }, [t]);


    /**
     * 树被点击时的事件
     * @param selectedKeys 当前选中的key
     * @param node 选中的树节点
     */
    const onSelectTree = async (selectedKeys: Key[], {node}: { node: Menu }) => {
        if (!selectedKeys || selectedKeys.length === 0) {
            // 查询一级菜单
            updateSearchParams({...permissionQuery, parentId: null})
            return
        }
        setFormParentPermission(node);
        // 这里只会有一条
        updateSearchParams({...permissionQuery, parentId: selectedKeys[0].toLocaleString()})
    }

    /**
     * 懒加载菜单
     * @param key key，这里是主键id
     * @param children 子菜单
     */
    const onLoadData = async ({key, children}: { key: React.Key, children?: Menu[] | undefined }) => {
        if (children) {
            return new Promise<void>((resolve) => {
                resolve()
            })
        }
        const res = await permissionApi.getPermissionListApi(key.toString());
        setLazyTreeData((origin) => updateTreeData(origin, key, res));
    }

    /**
     * 权限树
     */
    const permissionTreeSelect = async () => {
        const res = await permissionApi.getPermissionTreeSelect()
        setTreeSelectData([...res])
    }

    /**
     * 打开模态框
     * @param updateId 修改的id
     */
    const openModal = async (updateId?: string | undefined) => {
        setUpdateId(updateId);
        await permissionTreeSelect();
        if (updateId) {
            const permissionDetails = await permissionApi.getInfoByIdApi(updateId);
            form.setFieldsValue({...permissionDetails})
        }
        setIsModalOpen(true);
    }

    /**
     * 关闭模态组
     */
    const closeModal = () => {
        setIsModalOpen(false);
        setUpdateId(undefined);
        setFormParentPermission({} as Menu)
    }

    /**
     * 拼接父级权限
     * @param value value
     */
    const concatPermissionCode = (value: string): string => {
        return formParentPermission.permissionCode ?
            formParentPermission.permissionCode.concat(`:${value}`)
            : value;
    }

    /**
     * 处理表单
     */
    const handleForm = async () => {
        let values: Menu;
        try {
            values = await form.validateFields();
        } catch {
            return
        }
        values.permissionCode = concatPermissionCode(form.getFieldValue('permissionCode'));
        try {
            setIsModalButtonLoading(true);
            await (updateId ? permissionApi.editInfoApi(updateId, values) : permissionApi.saveInfoApi(values));
            message.success(t('Common.success')).then()

            await tableRef?.current?.refreshPageList({
                dataCallback: (res) => {
                    res?.list.forEach(menu => menu.permissionName = t(`${menu.permissionName}`));
                }
            });
            const parentId = values.parentId;
            await onLoadData({key: parentId ?? '-1'});
            setIsModalOpen(false);
        } finally {
            setIsModalButtonLoading(false);
        }
    }


    return (
        <>
            <Splitter>
                <Splitter.Panel className={'tree-container'} defaultSize="10%" min="10%" max="70%">
                    <div className='tree-info'>
                        {lazyTreeData && <Tree
                            defaultExpandParent={true}
                            defaultSelectedKeys={[permissionQuery.parentId ?? -1]}
                            showIcon={false}
                            blockNode
                            motion={false}
                            fieldNames={{key: 'id', title: 'permissionName'}}
                            loadData={onLoadData}
                            treeData={lazyTreeData}
                            titleRender={(menu: Menu) => t(`${menu.permissionName}`)}
                            onSelect={onSelectTree}
                        />}

                    </div>
                </Splitter.Panel>
                <Splitter.Panel>
                    <SearchHeader
                        components={[
                            <><label htmlFor="permissionName">{t('Permission.name')}</label>
                                <Input
                                    defaultValue={permissionQuery.permissionName}
                                    allowClear
                                    placeholder={t('Permission.namePlaceholder')} id={'permissionName'}
                                    onChange={(e) => setPermissionQuery({permissionName: e.target.value})}/>
                            </>,
                        ]}
                        onSearchClick={() => updateSearchParams(permissionQuery)}
                    />
                    <Table<Menu>
                        tableRef={tableRef}
                        tableName={t('Permission.list')}
                        columns={columns}
                        rowSelection={rowSelection}
                        pageApi={permissionApi.pageInfoListApi}
                        scroll={{x: 1500}}
                        tableComponents={[
                            <>
                                <PermissionButton buttonPermissions={buttonPermissions}
                                                  permissionStr={PermissionConstant.ADD}>
                                    <AddButton onClick={() => openModal()}/>
                                </PermissionButton>
                                <PermissionButton buttonPermissions={buttonPermissions}
                                                  permissionStr={PermissionConstant.DELETE}>
                                    <Popconfirm
                                        title={t('Button.delete')}
                                        description={t('Button.deleteConfirm')}
                                        okText={t('Common.yes')}
                                        cancelText={t('Common.no')}
                                        onConfirm={async () => {
                                            permissionApi.deleteInfoApi(rowKeys as string[]).then();
                                            await tableRef?.current?.refreshPageList();
                                            await permissionTreeSelect();
                                        }}
                                    >
                                        <DeleteButton
                                            disabled={rowKeys === undefined || rowKeys.length === 0}/>
                                    </Popconfirm>
                                </PermissionButton>
                            </>
                        ]}
                    />
                </Splitter.Panel>
            </Splitter>
            <Modal
                destroyOnClose={true}
                title={updateId ? t('Permission.edit') : t('Permission.add')}
                open={isModalOpen}
                onCancel={() => closeModal()}
                width={800}
                footer={[
                    <Button key='onOk' type="primary"
                            loading={isModalButtonLoading}
                            onClick={handleForm}
                    >{t('Button.confirm')}</Button>,
                    <Button key='onCancel' onClick={() => closeModal()}>{t('Button.cancel')}</Button>
                ]}
            >
                <Form
                    clearOnDestroy={true}
                    name="modal-form"
                    form={form}
                    autoComplete="off"
                    initialValues={{
                        parentId: formParentPermission.id,
                        permissionType: 'DIR',
                        isFrame: false,
                        isVisible: true,
                        isEnabled: true,
                    }}
                >
                    <Form.Item
                        label={t('Permission.parentPermission')}
                        name="parentId"
                        validateTrigger="onBlur"
                        colon={false}
                        required={true}
                    >
                        <TreeSelect
                            treeTitleRender={(menu: Menu) => {
                                if (menu) {
                                    return t(`${menu.permissionName}`);
                                }
                                return t('Menu.main')
                            }}
                            fieldNames={{
                                label: 'permissionName',
                                value: 'id',
                            }}
                            onSelect={(_: string, node: Menu) => {
                                setFormParentPermission(node);
                            }}
                            allowClear
                            dropdownStyle={{maxHeight: 400, overflow: 'auto'}}
                            treeData={treeSelectData}
                            treeDefaultExpandAll
                        />
                    </Form.Item>
                    <Form.Item
                        label={t('Permission.type')}
                        name="permissionType"
                        validateTrigger="onBlur"
                        key="permissionType"
                        colon={false}
                        required={true}
                        rules={[
                            {
                                required: true,
                                type: "string",
                                message: t('Permission.typeCheckMessage')
                            }
                        ]}
                    >
                        <Radio.Group onChange={(e) => {
                            form.setFieldValue('permissionType', e.target.value)
                        }}>
                            <Radio value={'DIR'}>{t('Permission.DIR')}</Radio>
                            <Radio value={'MENU'}>{t('Permission.MENU')}</Radio>
                            <Radio value={'BUTTON'}>{t('Permission.BUTTON')}</Radio>
                        </Radio.Group>
                    </Form.Item>

                    <Row gutter={24}>
                        <Col span={12}>
                            <Form.Item
                                label={t('Permission.name')}
                                labelCol={{span: language == CommonConstant.ZH_CN_LANGUAGE ? 6 : 10}}
                                name="permissionName"
                                key="permissionName"
                                colon={false}
                                required={true}
                                rules={[
                                    {
                                        required: true,
                                        type: "string",
                                        message: t('Permission.nameCheckMessage')
                                    }
                                ]}
                            >
                                <Input placeholder={t('Permission.namePlaceholder')} maxLength={20}/>
                            </Form.Item>
                        </Col>
                        <Col span={12}>
                            <Form.Item
                                label={t('Permission.code')}
                                name="permissionCode"
                                key="permissionCode"
                                colon={false}
                                required={true}
                                validateTrigger="onBlur"
                                rules={updateId ? [] : [
                                    {
                                        required: true,
                                        type: "string",
                                        message: t('Permission.codeCheckMessage')
                                    },
                                    () => ({
                                        validator: async (_, value: string) => {
                                            if (!value || value === '') {
                                                return
                                            }
                                            const exists: boolean = await permissionApi.checkPermissionCodeExistsApi(concatPermissionCode(value));
                                            if (exists) {
                                                return Promise.reject(new Error(t('Permission.codeExistsErrorMessage')));
                                            }

                                        }
                                    })
                                ]}
                            >
                                <Input
                                    disabled={updateId !== undefined}
                                    addonBefore={formParentPermission.permissionCode}
                                    suffix={<Tooltip title={t('Permission.codeTips')}>
                                        <IconFont type={'i-tips-hint'}/>
                                    </Tooltip>}
                                    placeholder={t('Permission.codePlaceholder')} maxLength={20}/>
                            </Form.Item>
                        </Col>

                    </Row>

                    <Row gutter={24}>
                        <Col span={12}>
                            <Form.Item
                                label={t('Common.displayOrder')}
                                labelCol={{span: language == CommonConstant.ZH_CN_LANGUAGE ? 6 : 10}}
                                name="displayOrder"
                                key="displayOrder"
                                colon={false}
                                required={true}
                                validateTrigger="onBlur"
                                rules={[
                                    {
                                        required: true,
                                        type: "number",
                                        message: t('Common.displayOrderPlaceholder')
                                    }
                                ]}
                            >
                                <InputNumber placeholder={t('Common.displayOrderPlaceholder')} style={{width: '100%'}}
                                             min={1}/>
                            </Form.Item>
                        </Col>
                        <Col span={12}>
                            <Form.Item
                                labelCol={{span: language == CommonConstant.ZH_CN_LANGUAGE ? 6 : 9}}
                                label={t('Common.status')}
                                name="isEnabled"
                                key="isEnabled"
                                colon={false}
                                required={true}
                            >
                                <Radio.Group>
                                    <Radio value={true}>{t('Common.enabled')}</Radio>
                                    <Radio value={false}>{t('Common.disabled')}</Radio>
                                </Radio.Group>
                            </Form.Item>
                        </Col>
                    </Row>

                    <Form.Item noStyle shouldUpdate>
                        {() => form.getFieldValue('permissionType') !== 'BUTTON' && (
                            <Row gutter={24}>
                                <Col span={12}>
                                    <Form.Item
                                        labelCol={{span: language == CommonConstant.ZH_CN_LANGUAGE ? 6 : 10}}
                                        label={t('Permission.isFrame')}
                                        name="isFrame"
                                        key="isFrame"
                                        colon={false}
                                        required={true}
                                    >
                                        <Radio.Group>
                                            <Radio value={true}>{t('Common.yes')}</Radio>
                                            <Radio value={false}>{t('Common.no')}</Radio>
                                        </Radio.Group>
                                    </Form.Item>
                                </Col>
                                <Col span={12}>
                                    <Form.Item
                                        labelCol={{span: language == CommonConstant.ZH_CN_LANGUAGE ? 6 : 9}}
                                        label={t('Permission.routePath')}
                                        name="routePath"
                                        key="routePath"
                                        colon={false}
                                        required={true}
                                        rules={[
                                            {
                                                required: true,
                                                type: "string",
                                                message: t('Permission.routePathCheckMessage'),
                                                validateTrigger: 'onBlur',

                                            }
                                        ]}
                                    >
                                        <Input placeholder={t('Permission.routePath')} maxLength={20}
                                               addonBefore={formParentPermission.routePath}
                                               suffix={
                                                   <Tooltip title={t('Permission.routePathTips')}>
                                                       <IconFont type={'i-tips-hint'}/>
                                                   </Tooltip>
                                               }
                                        />
                                    </Form.Item>
                                </Col>
                            </Row>
                        )}
                    </Form.Item>

                    <Form.Item noStyle shouldUpdate>
                        {() => form.getFieldValue('permissionType') === 'MENU' && (
                            <Row gutter={24}>
                                <Col span={12}>
                                    <Form.Item
                                        labelCol={{span: language == CommonConstant.ZH_CN_LANGUAGE ? 6 : 10}}
                                        label={t('Permission.componentPath')}
                                        name="componentPath"
                                        key="componentPath"
                                        colon={false}
                                        required={true}
                                        rules={[
                                            {
                                                required: true,
                                                type: "string",
                                                message: t('Permission.componentPathCheckMessage')
                                            }
                                        ]}
                                    >
                                        <Input placeholder={t('Permission.componentPathPlaceholder')}
                                               maxLength={50}/>
                                    </Form.Item>
                                </Col>
                                <Col span={12}>
                                    <Form.Item
                                        labelCol={{span: language == CommonConstant.ZH_CN_LANGUAGE ? 6 : 9}}
                                        label={t('Permission.routeParams')}
                                        name="urlParams"
                                        key="urlParams"
                                        colon={false}
                                    >
                                        <Input placeholder={t('Permission.routeParamsPlaceholder')}
                                               maxLength={100}/>
                                    </Form.Item>
                                </Col>
                            </Row>
                        )}
                    </Form.Item>

                    <Form.Item noStyle shouldUpdate>
                        {
                            () => form.getFieldValue('permissionType') !== 'BUTTON' &&
                                <Row gutter={24}>
                                    <Col span={12}>
                                        <Form.Item
                                            labelCol={{span: language == CommonConstant.ZH_CN_LANGUAGE ? 6 : 10}}
                                            label={t('Permission.icon')}
                                            name="icon"
                                            key="icon"
                                            colon={false}
                                        >
                                            <Input placeholder={t('Permission.iconPlaceholder')} maxLength={20}/>
                                        </Form.Item>
                                    </Col>
                                    <Col span={12}>
                                        <Form.Item
                                            labelCol={{span: language == CommonConstant.ZH_CN_LANGUAGE ? 6 : 9}}
                                            label={t('Permission.displayStatus')}
                                            name="isVisible"
                                            key="isVisible"
                                            colon={false}
                                            required={true}
                                        >
                                            <Radio.Group>
                                                <Radio value={true}>{t('Common.yes')}</Radio>
                                                <Radio value={false}>{t('Common.no')}</Radio>
                                            </Radio.Group>
                                        </Form.Item>
                                    </Col>

                                </Row>
                        }
                    </Form.Item>


                </Form>


            </Modal>
        </>
    );

}

