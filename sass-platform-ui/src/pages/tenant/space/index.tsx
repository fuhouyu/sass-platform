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


import React, {useCallback, useEffect, useState} from "react";
import {
    Breadcrumb,
    BreadcrumbProps,
    Button,
    Card,
    Divider,
    Drawer,
    Dropdown,
    Flex,
    List,
    MenuProps,
    Popconfirm,
    Space,
    TableColumnsType
} from "antd";
import {IconFont, PageList, S3Upload} from "@/components";
import {useTranslation} from "react-i18next";
import {resourceApi} from "@/apis/resource.tsx";
import type {TableRowSelection} from "antd/es/table/interface";
import {useResourcePreview} from "@/hooks/useResourcePreview.tsx";
import './index.scss'
import {DownloadOutlined, EyeOutlined, FolderOutlined, LeftOutlined, UploadOutlined} from "@ant-design/icons";
import {DeleteButton} from "@/components/Button/commonButton";
import {TenantSpace as TenantSpaceModel} from "@/model/tenant.tsx";
import {tenantSpaceApi} from "@/apis/tenantSpace.tsx";
import {usePageList} from "@/hooks/usePageList.tsx";
import useRouteSearchParams from "@/hooks/useRouteSearchParams.tsx";
import {Resource} from "@/model/resource.tsx";


const TenantSpace: React.FC = () => {

    const {t} = useTranslation();

    const {pageResult, refreshPageList} = usePageList(resourceApi.pageInfoListApi);
    const [rowKeys, setRowKeys] = useState<React.Key[]>([])
    const {previewUrl} = useResourcePreview();
    const {querySearchParams, updateSearchParams} = useRouteSearchParams();


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
                let type = 'i-weizhi';
                switch (record.mimeType) {
                    case 'image/jpeg':
                        type = 'i-tupian';
                        break;
                    case 'application/zip':
                        type = 'i-zip';
                        break;
                }
                return <Space size={4}>
                    <IconFont type={type}/>
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
            title: t('Common.updateAt'),
            dataIndex: 'updateAt',
            align: "center",
            render: (_, record) => {
                if (record.isDirectory) {
                    return <span>-</span>
                }
                return <span>{record.updateAt}</span>
            }
        },
        {
            title: t('Common.updateBy'),
            dataIndex: 'updateBy',
            align: "center",
            render: (_, record) => {
                if (record.isDirectory) {
                    return <span>-</span>
                }
                return <span>{record.updateBy}</span>
            }
        },
        // {
        //     title: t('Common.action'),
        //     dataIndex: 'action',
        //     align: "center",
        //     render: (_, record) => {
        //         if (record.isDirectory) {
        //             return
        //         }
        //         return <Button
        //             onClick={() => setPicViewUrl(previewUrl(record.id))}
        //             icon={<IconFont type="i-yulan"/>}>
        //             {t('Resource.preview')}
        //         </Button>
        //     }
        // }
    ]

    const initBreadcrumbItems: () => BreadcrumbProps['items'] = (): BreadcrumbProps['items'] => {
        const breadcrumbItems = [
            {
                title: '根目录',
                onClick: () => breadcrumbClick(undefined),
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


    const [breadcrumbItems, setBreadcrumb] = useState<BreadcrumbProps['items']>(initBreadcrumbItems);
    const [tenantSpace, setTenantSpace] = useState<TenantSpaceModel | undefined>(undefined);


    /**
     * 查询租户空间
     */
    const queryTenantSpaceInfo = useCallback(async () => {
        setTenantSpace(await tenantSpaceApi.getTenantSpaceForMe());
    }, []);


    const breadcrumbClick = (prefix?: string) => {
        let urlPrefix = prefix;
        if (prefix) {
            urlPrefix = prefix.endsWith('/') ? prefix : `${prefix}/`;
        }
        if (!prefix) {
            setBreadcrumb(breadcrumbItems?.slice(0, 1));
            updateSearchParams({prefix: urlPrefix})
            return
        }
        updateSearchParams({prefix: urlPrefix})
        prefix.split("/").forEach(p => {
            const index = breadcrumbItems?.findIndex((item => item.title === p)) ?? -1;
            if (index !== -1) {
                setBreadcrumb(breadcrumbItems?.slice(0, index + 1));
                return
            }
            setBreadcrumb([...breadcrumbItems ?? [], {
                title: p,
                onClick: () => breadcrumbClick(p),
            }])
        })
    }


    useEffect(() => {
        queryTenantSpaceInfo().then();
    }, [queryTenantSpaceInfo])


    /**
     * table列选择
     */
    const rowSelection: TableRowSelection<Resource> = {
        onChange: (selectedRowKeys: React.Key[]) => setRowKeys(selectedRowKeys),
    };

    const [picViewUrl, setPicViewUrl] = useState<string | undefined>(undefined);
    const [showFileDetail, setShowFileDetail] = useState<boolean>(false);
    const [selectFile, setSelectFile] = useState<Resource>();
    const onTableRowClick = (record: Resource) => {
        if (record.isDirectory) {
            breadcrumbClick(record.objectKey)
            return
        }
        setShowFileDetail(true);
        setSelectFile(record);
    }


    const uploadButtonItems: MenuProps = {
        items: [
            {
                label: (
                    <S3Upload
                        uploadProps={{
                            isPublic: false,
                            prefix: breadcrumbItems?.length === 1 ? undefined : (breadcrumbItems![breadcrumbItems!.length! - 1].title as string),
                            onUploadSuccess: async () => await refreshPageList(),
                        }
                        }
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
                        uploadProps={{
                            directory: true,
                            isPublic: false,
                            prefix: breadcrumbItems?.length === 1 ? undefined : (breadcrumbItems![breadcrumbItems!.length! - 1].title as string),
                            onUploadSuccess: async () => await refreshPageList(),
                        }
                        }
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
        {icon: <DownloadOutlined/>, text: t('Resource.download')},
        {
            icon: <EyeOutlined/>,
            text: t('Resource.preview'),
            onClick: () => selectFile && setPicViewUrl(previewUrl(selectFile.id))
        },
    ];

    return (<>
        <Card>
            <div className={'tenant-space-header'}>
                <Flex gap={8}>
                    <IconFont type={'i-cunchu'} style={{fontSize: '2.5rem'}}/>
                    <Flex vertical justify={'center'} className={'space-bucket-info'}>
                        <h2>{tenantSpace?.bucketName}</h2>
                        <Space size={24}>
                            <span>创建时间：<strong>{tenantSpace?.createAt}</strong></span>
                            <span>Access: <strong>{(tenantSpace?.acl ?? '').toLocaleUpperCase()}</strong></span>
                            <span>{((tenantSpace?.usedCapacity ?? 0) / 1024 / 1024).toFixed(2)} MiB / {tenantSpace?.capacity ?? 0} GiB - {pageResult?.total} Objects</span>
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
                            await refreshPageList();
                        }}
                    >
                        <DeleteButton disabled={rowKeys === undefined || rowKeys.length === 0}/>
                    </Popconfirm>
                    <Dropdown.Button icon={<UploadOutlined/>} menu={uploadButtonItems}>
                        {t('Resource.uploadFile')}
                    </Dropdown.Button>
                </Flex>
            </div>

            <div className={'tenant-space-content'}>
                <PageList<Resource>
                    tableProps={{
                        onRow: (record) => ({
                            onClick: () => onTableRowClick(record),
                        }),
                        columns: columns,
                        pageApi: resourceApi.pageInfoListApi,
                        rowSelection: rowSelection,
                        tableComponents: [
                            <>
                                <Flex justify={'center'}>
                                    <Button className={'back-button'}
                                            onClick={breadcrumbItems && breadcrumbItems[breadcrumbItems.length - 2]?.onClick}
                                    ><LeftOutlined/></Button>
                                    <Breadcrumb className={'space-bucket-breadcrumb'} items={breadcrumbItems}/>
                                </Flex>
                            </>
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
                        renderItem={(item) => (
                            <List.Item onClick={item.onClick}>
                                {item.icon} {item.text}
                            </List.Item>
                        )}
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

        <div>
            {picViewUrl && (
                <div className={'preview-img-container'}
                     onClick={() => setPicViewUrl(undefined)}
                >
                    <img
                        className={'preview-img'}
                        src={picViewUrl}
                        alt="预览图片"
                    />
                </div>
            )}
        </div>

    </>)
}

export default TenantSpace;