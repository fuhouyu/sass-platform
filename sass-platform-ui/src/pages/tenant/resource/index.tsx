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


import React, {useCallback, useEffect, useRef, useState} from "react";
import {
    Breadcrumb,
    BreadcrumbProps,
    Button,
    Card,
    Divider,
    Drawer,
    Dropdown,
    Flex,
    Input,
    List,
    MenuProps,
    Modal,
    Popconfirm,
    Space,
    TableColumnsType,
    Tag
} from "antd";
import {IconFont, PageList, PermissionButton, S3Upload} from "@/components";
import {Trans, useTranslation} from "react-i18next";
import {resourceApi} from "@/apis/resource.tsx";
import type {TableRowSelection} from "antd/es/table/interface";
import './index.scss'
import {
    DownloadOutlined,
    EditOutlined,
    EyeOutlined,
    FolderOutlined,
    LeftOutlined,
    LockOutlined,
    SearchOutlined,
    UploadOutlined
} from "@ant-design/icons";
import {DeleteButton} from "@/components/Button/commonButton";
import {TenantSpace as TenantSpaceModel} from "@/model/tenant.tsx";
import {tenantSpaceApi} from "@/apis/tenantSpace.tsx";
import useRouteSearchParams from "@/hooks/useRouteSearchParams.tsx";
import {Resource} from "@/model/resource.tsx";
import {ResourceView} from "@components/ResourceView/resourceView.tsx";
import {useResourceAction} from "@/hooks/useResourceAction.tsx";
import {TableRefType} from "@/components/List/table/interface";
import {BaseUrlConstant} from "@/constants/baseUrlConstant.tsx";
import {usePageTitle} from "@/hooks/usePageTitle.tsx";
import {TenantResourcePermissionConstant} from "@/constants/permissionConstant.tsx";
import {useButton} from "@/hooks/useButton.tsx";
import {AnyObject} from "antd/es/_util/type";
import {useNotification} from "@/hooks/useNotification.tsx";
import {getCategoryInfo, ResourceCategoryEnum} from "@/enums/ResourceCategoryEnum.tsx";

const TenantResource: React.FC = () => {
    usePageTitle('Menu.resourceManage');
    const {t} = useTranslation();

    const tableRef = useRef<TableRefType<Resource>>(null);
    const [rowKeys, setRowKeys] = useState<React.Key[]>([])
    const {querySearchParams, updateSearchParams} = useRouteSearchParams();
    const {generateSignedUrl} = useResourceAction();
    const [tenantSpace, setTenantSpace] = useState<TenantSpaceModel | undefined>(undefined);
    const [countObjects, setCountObjects] = useState<number>(0)
    const [showFileDetail, setShowFileDetail] = useState<boolean>(false);
    const [selectFile, setSelectFile] = useState<Resource>();
    const [previewModal, setPreviewModal] = useState<boolean>(false);
    const buttonPermissions = useButton(TenantResourcePermissionConstant.List);
    const {notificationMessage, contextHolder} = useNotification();

    const columns: TableColumnsType<Resource> = [
        {
            title: t('Resource.name'),
            dataIndex: 'name',
            align: 'center',
            render: (_, record) => {
                if (record.isDirectory) {
                    return <Button type={'link'} onClick={() => {
                        breadcrumbClick(record.objectKey)
                    }}>
                        <Space size={4}>
                            <IconFont type={'i-dir'}/>
                            {record.name}
                        </Space>
                    </Button>
                }
                const categoryInfo = getCategoryInfo(record.category!);
                return <Space size={4}>
                    {categoryInfo.icon}
                    {record.name}
                </Space>
            }
        },
        {
            title: t('Resource.size'),
            dataIndex: 'size',
            align: "center",
            render: (_, record) => {
                if (record.isDirectory) {
                    return <span>-</span>
                }
                const fileSize = Math.round((record.size / 1024) * 100) / 100;
                if (fileSize > 1024) {
                    return (<span>{(fileSize / 1024).toFixed(2)} MB</span>)
                } else if (fileSize > (1024 * 1024)) {
                    return (<span>{(fileSize / (1024 * 1024)).toFixed(2)} GB</span>)
                }
                return (<span>{fileSize} KB</span>);
            }
        },
        {
            title: t('Resource.type'),
            dataIndex: 'mimeType',
            align: "center",
            render: (_, record) => {
                if (record.isDirectory) {
                    return <span>-</span>
                }
                return <span>{record.mimeType}</span>
            }
        },
        {
            title: t('Resource.category'),
            dataIndex: 'category',
            align: "center",
            render: (_, record) => {
                if (record.isDirectory) {
                    return <span>-</span>
                }
                return <span>{record.category}</span>
            }
        },
        {
            title: t('Resource.access'),
            dataIndex: 'isPublic',
            align: "center",
            render: (_, record: Resource) => {
                if (record.isDirectory) {
                    return <div>-</div>;
                }
                return record.isPublic ?
                    <Tag icon={<EyeOutlined/>} color="success">
                        {t('Resource.public')}
                    </Tag>
                    :
                    <Tag icon={<LockOutlined/>} color="warning">
                        {t('Resource.private')}
                    </Tag>;
            }
        },
        {
            title: t('Common.updatedAt'),
            dataIndex: 'updatedAt',
            align: "center",
            render: (_, record) => {
                if (record.isDirectory) {
                    return <span>-</span>
                }
                return <span>{record.updatedAt}</span>
            }
        },
        {
            title: t('Common.updatedBy'),
            dataIndex: 'updatedBy',
            align: "center",
            render: (_, record) => {
                if (record.isDirectory) {
                    return <span>-</span>
                }
                return <span>{record.updatedBy}</span>
            }
        },
        {
            title: t('Common.action'),
            dataIndex: 'action',
            align: 'center',
            width: 240,
            fixed: 'right',
            render: (_: AnyObject, record: Resource) => {
                if (record.isDirectory) {
                    return <span>-</span>
                }
                const label = record.isPublic ? t('Resource.setPrivate') : t('Resource.setPublic');
                const icon = record.isPublic ? <LockOutlined/> : <EyeOutlined/>;
                const successTips = record.isPublic ? t('Resource.setPrivateTips') : t('Resource.setPublicSuccessTips');
                return (<PermissionButton buttonPermissions={buttonPermissions}
                                          permissionStr={TenantResourcePermissionConstant.EDIT}>
                    <Button
                        color={record.isPublic ? 'pink' : 'cyan'}
                        variant={'solid'}
                        icon={icon}
                        disabled={record.isDirectory}
                        onClick={async (event) => {
                            event.stopPropagation();
                            await resourceApi.status(record.id!, !record.isPublic);
                            notificationMessage({
                                type: 'success',
                                message: label,
                                description: <Trans
                                    i18nKey={successTips}
                                    values={{name: record.name}}
                                    components={{strong: <span className="highlight"/>}}
                                />
                            });
                            await tableRef.current?.refreshPageList();
                        }}>
                        {label}
                    </Button>
                </PermissionButton>)
            }
        }
    ]

    const initBreadcrumbItems: () => BreadcrumbProps['items'] = (): BreadcrumbProps['items'] => {
        const breadcrumbItems = [
            {
                title: t('Resource.rootPath'),
                onClick: () => breadcrumbClick(),
            }];
        const prefix: string = querySearchParams()['prefix'];
        if (!prefix) {
            return breadcrumbItems;
        }
        prefix.split("/").forEach(p => {
            breadcrumbItems.push({
                title: p,
                onClick: () => breadcrumbClick(p),
            })
        })
        return breadcrumbItems
    };

    const [breadcrumbItems, setBreadcrumbItems] = useState<BreadcrumbProps['items']>(initBreadcrumbItems);

    /**
     * 初始化数据
     */
    const init = useCallback(async () => {
        setTenantSpace(await tenantSpaceApi.getTenantSpaceForMe());
        const number = await resourceApi.countObjects();
        setCountObjects(number);
    }, []);


    const breadcrumbClick = (prefix?: string) => {
        let urlPrefix = prefix;
        if (prefix) {
            urlPrefix = prefix.endsWith('/') ? prefix : `${prefix}/`;
        }
        if (!prefix) {
            setBreadcrumbItems(breadcrumbItems?.slice(0, 1));
            updateSearchParams({prefix: urlPrefix})
            return
        }
        updateSearchParams({prefix: urlPrefix})
        prefix.split("/").forEach(p => {
            const index = breadcrumbItems?.findIndex((item => item.title === p)) ?? -1;
            if (index !== -1) {
                setBreadcrumbItems(breadcrumbItems?.slice(0, index + 1));
                return
            }
            setBreadcrumbItems([...breadcrumbItems ?? [], {
                title: p,
                onClick: () => breadcrumbClick(p),
            }])
        })
    }


    useEffect(() => {
        init().then();
    }, [init])


    /**
     * table列选择
     */
    const rowSelection: TableRowSelection<Resource> = {
        onChange: (selectedRowKeys: React.Key[]) => setRowKeys(selectedRowKeys),
    };

    const onTableRowClick = (record: Resource) => {
        if (record.isDirectory) {
            breadcrumbClick(record.objectKey)
            return
        }
        setShowFileDetail(true);
        setSelectFile({...record});
    }

    /**
     * 查询资源
     */
    const queryResource = useCallback(async () => {
        await tableRef?.current?.refreshPageList();
    }, []);


    const uploadButtonItems: MenuProps = {
        items: [
            {
                label: (
                    <S3Upload
                        isPublic={false}
                        showUploadList={false}
                        showUploadFloatButton
                        prefix={breadcrumbItems?.length === 1 ? undefined : (breadcrumbItems![breadcrumbItems!.length - 1].title as string)}
                        onUploadSuccess={queryResource}
                    >
                        {t('Resource.uploadFile')}
                    </S3Upload>
                ),
                key: 'upload-file',
                icon: <UploadOutlined/>,
            },
            {
                label: (
                    <S3Upload
                        directory
                        isPublic={false}
                        showUploadList={false}
                        prefix={breadcrumbItems?.length === 1 ? undefined : (breadcrumbItems![breadcrumbItems!.length - 1].title as string)}
                        onUploadSuccess={queryResource}
                    >
                        {t('Resource.uploadFolder')}
                    </S3Upload>
                ),
                key: 'upload-folder',
                icon: <FolderOutlined/>,
            }
        ]
    }

    const fileActions = [
        {
            key: TenantResourcePermissionConstant.DOWNLOAD,
            icon: <DownloadOutlined/>,
            text: t('Resource.download'),
            onClick: async () => selectFile && window.open(await generateSignedUrl(selectFile.id, false))
        },
        {
            key: TenantResourcePermissionConstant.OFFICE_EDIT,
            icon: <EditOutlined/>, text: t('Resource.editor'),
            onClick: () => {
                window.open(`${BaseUrlConstant.OFFICE_URL}/${selectFile?.id}?mode=EDIT`)
            },
        },
        {
            key: TenantResourcePermissionConstant.PREVIEW,
            icon: <EyeOutlined/>,
            text: t('Resource.preview'),
            onClick: () => {
                if (selectFile?.category === ResourceCategoryEnum.DOCUMENT) {
                    window.open(`${BaseUrlConstant.OFFICE_URL}/${selectFile?.id}?mode=VIEW`)
                    return
                }
                setPreviewModal(true);
            },
        },
    ];

    return (<>
        {contextHolder}
        <Flex className={'resource-search-container'} justify={'center'} align={'center'}>
            <Input
                className={'resource-search-input'}
                prefix={<SearchOutlined/>}
                placeholder={t('Resource.search')}
                value={querySearchParams().name}
                onChange={(e) => updateSearchParams({
                    name: e.target.value
                })}
            />
        </Flex>
        <Card>
            <div className={'tenant-space-header'}>
                <Flex gap={8}>
                    <IconFont type={'i-cunchu'} style={{fontSize: '2.5rem'}}/>
                    <Flex vertical justify={'center'} className={'space-bucket-info'}>
                        <h2>{tenantSpace?.bucketName}</h2>
                        <Space size={24}>
                            <span>{t('Common.createdAt')}：<strong>{tenantSpace?.createdAt}</strong></span>
                            <span>Access: <strong>{(tenantSpace?.acl ?? '').toLocaleUpperCase()}</strong></span>
                            <span>{((tenantSpace?.usedCapacity ?? 0) / 1024 / 1024).toFixed(2)} MiB / {tenantSpace?.capacity ?? 0} GiB - {countObjects} Objects
                            </span>
                        </Space>
                    </Flex>
                </Flex>
                <Flex gap={8}>
                    <Popconfirm
                        title={t('Button.delete')}
                        description={t('Button.deleteConfirm')}
                        okText={t('Common.yes')}
                        cancelText={t('Common.no')}
                        onConfirm={async () => {
                            await resourceApi.deleteInfoApi(rowKeys as string[]);
                            await tableRef?.current?.refreshPageList();
                        }}
                    >
                        <DeleteButton
                            disabled={rowKeys === undefined || rowKeys.length === 0}/>
                    </Popconfirm>
                    <Dropdown.Button icon={<UploadOutlined/>} menu={uploadButtonItems}>
                        {t('Resource.uploadFile')}
                    </Dropdown.Button>
                </Flex>
            </div>

            <div className={'tenant-space-content'}>
                <PageList<Resource>
                    tableProps={{
                        tableRef: tableRef,
                        onRow: (record) => ({
                            onClick: () => onTableRowClick(record),
                        }),
                        columns: columns,
                        pageApi: resourceApi.pageInfoListApi,
                        rowSelection: rowSelection,
                        tableComponents: [
                            <Flex
                                key={'back-button'}
                                justify={'center'}>
                                <Button className={'back-button'}
                                        onClick={breadcrumbItems?.[breadcrumbItems.length - 2]?.onClick}
                                ><LeftOutlined/></Button>
                                <Breadcrumb className={'space-bucket-breadcrumb'} items={breadcrumbItems}/>
                            </Flex>
                        ]
                    }}
                />
                <Drawer
                    title={selectFile?.name}
                    placement="right"
                    closable={false}
                    onClose={() => setShowFileDetail(false)}
                    open={showFileDetail}
                >
                    <List
                        className={'file-actions-list'}
                        header={<span><strong>Actions: </strong></span>}
                        bordered
                        dataSource={fileActions}
                        renderItem={(item) => {
                            if (item.text === t('Resource.editor')) {
                                if (!(selectFile?.category === ResourceCategoryEnum.DOCUMENT.toString())) {
                                    return null;
                                }
                            }
                            console.log(buttonPermissions)
                            return <PermissionButton buttonPermissions={buttonPermissions}
                                                     permissionStr={item.key}>
                                <List.Item onClick={item.onClick}>
                                    {item.icon} {item.text}
                                </List.Item>
                            </PermissionButton>

                        }}
                    />
                    <h3>{t('Resource.info')}</h3>
                    <Divider/>
                    <div className={'file-detail-container'}>
                        <strong>{t('Resource.name')}: </strong>
                        <br/>
                        <span>{selectFile?.name}</span>
                    </div>
                    <div className={'file-detail-container'}>
                        <strong>{t('Resource.size')}: </strong>
                        <br/>
                        <span>{selectFile?.size}</span>
                    </div>
                    <div className={'file-detail-container'}>
                        <strong>Etag: </strong>
                        <br/>
                        <span>{selectFile?.eTag}</span>
                    </div>
                </Drawer>
            </div>
        </Card>

        <Modal
            title={selectFile?.name}
            className={'preview-modal'}
            destroyOnClose
            open={previewModal}
            footer={null}
            width={'80%'}
            height={'80%'}
            closable

            onCancel={() => setPreviewModal(false)}
        >
            <ResourceView
                mimeType={selectFile?.mimeType ?? ''}
                id={selectFile?.id ?? ''}
                isPublic={selectFile?.isPublic ?? false}
                category={selectFile?.category ?? ''}/>
        </Modal>

    </>)
}

export default TenantResource;