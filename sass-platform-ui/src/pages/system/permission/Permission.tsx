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
import {Button, Col, Form, Input, Modal, Radio, Row, Space, TableColumnsType, Tree, TreeSelect} from "antd";
import {permissionApi} from "@/apis/permission";
import {Menu} from "@/model/menu";
import './index.scss'
import {useTranslation} from "react-i18next";
import {IconFont, SearchHeader, Table} from "@/components";
import {PageQuery, PageResult} from "@/model/pageQuery";
import {AddButton, DeleteButton} from "@components/Button/commonButton";
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

export const Permission: React.FC = () => {
    const [lazyTreeData, setLazyTreeData] = useState<Menu[]>([]);
    const [treeSelectData, setTreeSelectData] = useState<Menu[]>([]);
    const [pageQuery, setPageQuery] = useState<PageQuery>({
        pageNum: 1,
        pageSize: 10,
        parentId: '-1',
    });
    const [search, setSearch] = useState<{ [key: string]: unknown }>({})
    const [pageData, setPageData] = useState<PageResult<Menu>>({} as PageResult<Menu>);
    const [rowKeys, setRowKeys] = useState<React.Key[]>([])
    const [updateId, setUpdateId] = useState<string | undefined>();
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const [form] = Form.useForm();

    /**
     * table列选择
     */
    const rowSelection: TableRowSelection<Menu> = {
        onChange: (selectedRowKeys: React.Key[]) => setRowKeys(selectedRowKeys),
        getCheckboxProps: (record: Menu) => ({
            disabled: !record.isAllowModified
        }),
    };

    const tableSearch = (tableSearch: { [key: string]: unknown }) => {
        setPageQuery({
            ...pageQuery,
            ...search,
            ...tableSearch
        });
    }
    const {t} = useTranslation();
    /**
     * 左侧菜单树
     */
    useEffect(() => {
        // 先查询出一级菜单
        permissionApi.getPermissionListApi()
            .then((res: Menu[]) => {
                res.forEach((item: Menu) => item.permissionName = t(`Menu.${item.permissionName}`))
                setLazyTreeData(res);
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
                    <Space size="middle" style={{whiteSpace: 'nowrap'}}>
                        <a onClick={() => openModal(record.id)}>修改</a>
                    </Space>
                </>)
            }
        }
    ];

    /**
     * 树被点击时的事件
     * @param selectedKeys 当前选中的key
     */
    const onSelectTree = async (selectedKeys: Key[]) => {
        if (!selectedKeys || selectedKeys.length === 0) {
            return
        }
        // 这里只会有一条
        const child = await permissionApi.getPermissionListApi(selectedKeys[0].toLocaleString())
        child.forEach((item: Menu) => item.permissionName = t(`Menu.${item.permissionName}`))
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
        res.forEach((item: Menu) => item.permissionName = t(`Menu.${item.permissionName}`))
        setLazyTreeData((origin) => updateTreeData(origin, key, res));
    }

    /**
     * 打开模态框
     * @param updateId 修改的id
     */
    const openModal = (updateId?: string | undefined) => {
        setUpdateId(updateId);
        permissionApi.getPermissionTreeSelect()
            .then((res) => {
                setTreeSelectData([{
                    id: '-1',
                    permissionName: 'main',
                    permissionCode: '',
                    children: res
                }]);
            });
        setIsModalOpen(true);
    }

    /**
     * 关闭模态组
     */
    const closeModal = () => {
        setIsModalOpen(false);
        setUpdateId(undefined);
        form.resetFields();
    }

    return (
        <>
            <Row gutter={24} className={'main-container'}>
                <Col span={3} className={'tree-container'}>
                    <Input
                        className='search-input'
                        placeholder={t('Permission.namePlaceholder')} allowClear/>
                    <div className='tree-info'>
                        <Tree
                            showLine
                            fieldNames={{key: 'id', title: 'permissionName'}}
                            switcherIcon={<DownOutlined/>}
                            loadData={onLoadData}
                            treeData={lazyTreeData}
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
                                    setPageQuery({...pageQuery})
                                }}/>
                            </>
                        ]}
                    />
                </Col>
            </Row>

            <Modal
                title={updateId ? t('Permission.edit') : t('Permission.add')}
                className="ant-modal-header"
                open={isModalOpen}
                onCancel={() => closeModal()}
                width={600}
                footer={[
                    <Button key='onOk' type="primary"
                    >{t('Button.confirm')}</Button>,
                    <Button key='onCancel' onClick={() => closeModal()}>{t('Button.cancel')}</Button>
                ]}
                closeIcon={<IconFont type="i-Close" style={{
                    fontSize: '24px',
                }}/>}
            >
                <Form
                    name="basic"
                    form={form}
                    style={{maxWidth: 600}}
                    autoComplete="off"
                    initialValues={{
                        permissionType: 'DIR',
                        isFrame: false,
                    }}
                >
                    <Row gutter={24}>
                        <Col span={24}>
                            <Form.Item
                                label={t('Permission.parentPermission')}
                                name="parentId"
                                validateTrigger="onBlur"
                                key="parentId"
                                colon={false}
                                initialValue={{parentId: -1, permissionName: 'main'}}
                                required={true}
                            >
                                <TreeSelect
                                    style={{width: '100%'}}
                                    treeTitleRender={(menu: Menu) => {
                                        if (menu) {
                                            return t(`Menu.${menu.permissionName}`)
                                        }
                                        console.log(menu)
                                    }}
                                    fieldNames={{
                                        label: 'permissionName',
                                        value: 'id',
                                    }}
                                    allowClear
                                    dropdownStyle={{maxHeight: 400, overflow: 'auto'}}
                                    treeData={treeSelectData}
                                    treeDefaultExpandAll
                                />
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row gutter={24}>
                        <Col span={24}>
                            <Form.Item
                                label={t('Permission.type')}
                                name="permissionType"
                                validateTrigger="onBlur"
                                key="permissionType"
                                colon={false}
                                required={true}
                                rules={[{required: true, message: t('Permission.typeCheckMessage')}]}
                            >
                                <Radio.Group>
                                    <Radio value={'DIR'}>{t('Permission.DIR')}</Radio>
                                    <Radio value={'MENU'}>{t('Permission.MENU')}</Radio>
                                    <Radio value={'BUTTON'}>{t('Permission.BUTTON')}</Radio>
                                </Radio.Group>
                            </Form.Item>
                        </Col>
                    </Row>

                    <Row gutter={24}>
                        <Col span={24}>
                            <Form.Item
                                label={t('Permission.icon')}
                                name="icon"
                                key="icon"
                                labelCol={{span: 6}}
                                colon={false}
                            >
                                <Input placeholder={t('Permission.iconPlaceholder')} maxLength={20}/>
                            </Form.Item>
                        </Col>
                    </Row>

                    <Row gutter={24}>
                        <Col span={12}>
                            <Form.Item
                                label={t('Permission.isFrame')}
                                name="isFrame"
                                key="isFrame"
                                colon={false}
                                labelCol={{span: 12}}
                                wrapperCol={{span: 12}}
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
                                label={t('Permission.isFrame')}
                                name="isFrame"
                                key="isFrame"
                                colon={false}
                                required={true}
                                labelCol={{span: 12}}
                                wrapperCol={{span: 12}}
                            >
                                <Input placeholder={t('Permission.routePaht')} maxLength={20}/>
                            </Form.Item>
                        </Col>
                    </Row>
                </Form>


            </Modal>
        </>
    )

}

