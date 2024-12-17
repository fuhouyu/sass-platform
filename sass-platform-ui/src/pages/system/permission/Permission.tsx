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


import React, {Key, useEffect, useState} from "react";
import {DownOutlined} from "@ant-design/icons";
import {
    Button,
    Col,
    Form,
    Input,
    InputNumber,
    message,
    Modal,
    Radio,
    Row,
    TableColumnsType,
    Tooltip,
    Tree,
    TreeSelect
} from "antd";
import {permissionApi} from "@/apis/permission";
import {Menu} from "@/model/menu";
import './index.scss'
import {useTranslation} from "react-i18next";
import {IconFont, SearchHeader, Table} from "@/components";
import {PageQuery, PageResult} from "@/model/pageQuery";
import {AddButton, DeleteButton, EditButton} from "@components/Button/commonButton";
import type {TableRowSelection} from "antd/es/table/interface";
import {AnyObject} from "antd/es/_util/type";

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

const mainPermission: Menu = {
    id: '-1',
    permissionName: 'main',
    permissionCode: '',
}

export const Permission: React.FC = () => {

    const [treeSelectData, setTreeSelectData] = useState<Menu[]>([]);
    const [pageQuery, setPageQuery] = useState<PageQuery>({
        pageNum: 1,
        pageSize: 10,
        parentId: '-1',
    });

    const {t} = useTranslation();
    const [search, setSearch] = useState<{ [key: string]: unknown; }>({});
    const [pageData, setPageData] = useState<PageResult<Menu>>({} as PageResult<Menu>);
    const [rowKeys, setRowKeys] = useState<React.Key[]>([]);
    const [updateId, setUpdateId] = useState<string | undefined>();
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const [form] = Form.useForm();
    const [isModalButtonLoading, setIsModalButtonLoading] = useState<boolean>(false);
    const [formParentPermission, setFormParentPermission] = useState<Menu>({});
    const [lazyTreeData, setLazyTreeData] = useState<Menu[]>([]);

    const columns: TableColumnsType<Menu> = [
        {
            title: t('Permission.name'),
            dataIndex: 'permissionName',
            showSorterTooltip: {target: 'full-header'},
        },
        {
            title: t('Permission.code'),
            dataIndex: 'permissionCode',

        },
        {
            title: t('Common.displayOrder'),
            dataIndex: 'displayOrder',
            sorter: true,
            defaultSortOrder: 'descend',
        },
        {
            title: t('Common.updateAt'),
            dataIndex: 'updateAt',
            sorter: true,
            defaultSortOrder: "descend",
            showSorterTooltip: false
        },
        {
            title: t('Common.updateBy'),
            dataIndex: 'updateBy',
        },
        {
            title: t('Common.action'),
            dataIndex: 'action',
            render: (_: AnyObject, record: Menu) => {
                return (<>
                    <EditButton disabled={!record.isAllowModified} onClick={() => openModal(record.id)}/>
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
     * 表单搜索
     * @param tableSearch 表单搜索
     */
    const tableSearch = (tableSearch: { [key: string]: unknown }) => {
        setPageQuery({
            ...pageQuery,
            ...search,
            ...tableSearch
        });
    }

    /**
     * 左侧菜单树
     */
    useEffect(() => {
        // 先查询出一级菜单
        permissionApi.getPermissionListApi()
            .then((res: Menu[]) => {
                setLazyTreeData([{
                    ...mainPermission,
                    children: res
                }]);
            });
    }, [t]);

    /**
     * 右侧列表
     */
    useEffect(() => {
        permissionApi.pageInfoListApi(pageQuery)
            .then((res) => {
                res?.list.forEach(menu => menu.permissionName = t(`Menu.${menu.permissionName}`))
                setPageData(res);
            })
    }, [pageQuery, t]);


    /**
     * 树被点击时的事件
     * @param selectedKeys 当前选中的key
     * @param node 选中的树节点
     */
    const onSelectTree = async (selectedKeys: Key[], {node}: { node: Menu }) => {
        if (!selectedKeys || selectedKeys.length === 0) {
            return
        }
        setFormParentPermission(node);
        // 这里只会有一条
        setPageQuery({...pageQuery, parentId: selectedKeys[0].toLocaleString()})
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
        const menu = mainPermission;
        menu.children = res
        setTreeSelectData([menu])
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
        setFormParentPermission({})
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
    const handlerForm = async () => {
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
            const res = await permissionApi.pageInfoListApi(pageQuery);
            res?.list.forEach(menu => menu.permissionName = t(`Menu.${menu.permissionName}`))
            setPageData(res);
            setIsModalOpen(false);
        } finally {
            setIsModalButtonLoading(false);
        }
    }


    return (
        <>
            <Row gutter={24} className={'main-container'}>
                <Col span={3} className={'tree-container'}>
                    {/*<Input*/}
                    {/*    className='search-input'*/}
                    {/*    placeholder={t('Permission.namePlaceholder')} allowClear/>*/}
                    <div className='tree-info'>
                        <Tree
                            defaultExpandParent={true}
                            showLine
                            fieldNames={{key: 'id', title: 'permissionName'}}
                            switcherIcon={<DownOutlined/>}
                            loadData={onLoadData}
                            treeData={lazyTreeData}
                            titleRender={(menu: Menu) => t(`Menu.${menu.permissionName}`)}
                            onSelect={onSelectTree}
                        />
                    </div>
                </Col>
                <Col span={21}>
                    <SearchHeader
                        components={[
                            <><label htmlFor="permissionName">{t('Permission.name')}</label>
                                <Input placeholder={t('Permission.namePlaceholder')} id={'permissionName'}
                                       onChange={(e) => setSearch({permissionName: e.target.value})}/>
                            </>,
                        ]}
                        onSearchClick={() => {
                            setPageQuery({...pageQuery, ...search})
                        }}
                    />
                    <Table<Menu>
                        tableName={t('Permission.listName')}
                        columns={columns}
                        rowSelection={rowSelection}
                        setPageQuery={tableSearch}
                        pageData={pageData}
                        components={[
                            <>
                                <AddButton onClick={() => openModal()}/>
                                <DeleteButton onClick={async () => {
                                    permissionApi.deleteInfoApi(rowKeys as string[]).then();
                                    setPageQuery({...pageQuery});
                                    await permissionTreeSelect();
                                }}/>
                            </>
                        ]}
                    />
                </Col>
            </Row>

            <Modal
                destroyOnClose={true}
                title={updateId ? t('Permission.edit') : t('Permission.add')}
                className="ant-modal-header"
                open={isModalOpen}
                onCancel={() => closeModal()}
                width={600}
                footer={[
                    <Button key='onOk' type="primary"
                            loading={isModalButtonLoading}
                            onClick={handlerForm}
                    >{t('Button.confirm')}</Button>,
                    <Button key='onCancel' onClick={() => closeModal()}>{t('Button.cancel')}</Button>
                ]}
                closeIcon={<IconFont type="i-Close" style={{
                    fontSize: '24px',
                }}/>}
            >
                <Form
                    clearOnDestroy={true}
                    name="modal-form"
                    form={form}
                    wrapperCol={{offset: 0.5}}
                    style={{width: 600}}
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
                            style={{width: '100%'}}
                            treeTitleRender={(menu: Menu) => {
                                if (menu) {
                                    return t(`Menu.${menu.permissionName}`);
                                }
                                return t('Menu.main')
                            }}
                            fieldNames={{
                                label: 'permissionName',
                                value: 'id',
                            }}
                            onSelect={(_: string, node: Menu) => {
                                // console.log('=====')
                                // console.log(node)
                                // console.log(formParentPermission)
                                setFormParentPermission(node);
                                // setFormParentPermission(node)
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
                                labelCol={{span: 8}}
                                label={t('Permission.name')}
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
                                labelCol={{span: 8}}
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
                                labelCol={{span: 8}}
                                label={t('Common.displayOrder')}
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
                                labelCol={{span: 8}}
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
                                        labelCol={{span: 8}}
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
                                        labelCol={{span: 8}}
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
                                        labelCol={{span: 8}}
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
                                        labelCol={{span: 8}}
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
                                            labelCol={{span: 8}}
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
                                            labelCol={{span: 8}}
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
    )

}

