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
    Dropdown,
    Flex,
    MenuProps,
    Popconfirm,
    Space,
    TableColumnsType
} from "antd";
import {IconFont, PageList, S3Upload} from "@/components";
import {useTranslation} from "react-i18next";
import {PageQuery, PageResult} from "@/model/pageQuery";
import {Resource} from "@/model/resource.tsx";
import {resourceApi} from "@/apis/resource.tsx";
import type {TableRowSelection} from "antd/es/table/interface";
import {Userinfo} from "@/model/user.tsx";
import {useResourcePreview} from "@/hooks/useResourcePreview.tsx";
import './index.scss'
import {FolderOutlined, LeftOutlined, UploadOutlined} from "@ant-design/icons";
import {useLocation, useNavigate} from "react-router-dom";
import qs from 'query-string';
import {BaseUrlConstant} from "@/constants/baseUrlConstant.tsx";
import {DeleteButton} from "@/components/Button/commonButton";
import {RcFile} from "antd/es/upload";


const TenantSpace: React.FC = () => {

    const {t} = useTranslation();

    const [pageResult, setPageResult] = useState<PageResult<Resource>>();
    const [rowKeys, setRowKeys] = useState<React.Key[]>([])
    const {previewUrl} = useResourcePreview();
    const navigate = useNavigate();
    const location = useLocation();
    const urlQueryParams = qs.parse(location.search) as PageQuery;
    /**
     * 分页查询
     */
    const [pageQuery, setPageQuery] = useState<PageQuery>({
        ...urlQueryParams,
        pageNum: 1,
        pageSize: 10,
    });


    const columns: TableColumnsType = [
        {
            title: t('Resource.name'),
            dataIndex: 'name',
            align: 'center',
            render: (_, record) => {
                if (record.isDirectory) {
                    return <Button type={'link'} onClick={() => {
                        // setPageQuery({...pageQuery, parentId: record.id})
                        handleBreadcrumb(record.objectKey)
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
        {
            title: t('Common.action'),
            dataIndex: 'action',
            align: "center",
            render: (_, record) => {
                if (record.isDirectory) {
                    return
                }
                return <Button
                    onClick={() => setPicViewUrl(previewUrl(record.id))}
                    icon={<IconFont type="i-yulan"/>}>
                    {t('Resource.preview')}
                </Button>
            }
        }
    ]

    const initBreadcrumbItems: () => BreadcrumbProps['items'] = (): BreadcrumbProps['items'] => {
        const breadcrumbItems = [
            {
                title: '根目录',
                onClick: () => handleBreadcrumb(undefined),
            }];
        const prefix: string = urlQueryParams.prefix as string;
        if (!prefix) {
            return breadcrumbItems;
        }
        breadcrumbItems.push({
            title: prefix,
            onClick: () => handleBreadcrumb(prefix)
        })
        return breadcrumbItems
    }


    const [breadcrumbItems, setBreadcrumb] = useState<BreadcrumbProps['items']>(initBreadcrumbItems);

    /**
     * 查询资源
     */
    const queryResource = useCallback(async () => {
        setPageResult(await resourceApi.pageInfoListApi(pageQuery));
    }, [pageQuery])

    /**
     * 处理面包屑
     * @param record 记录
     */
    const handleBreadcrumb = useCallback((prefix?: string | undefined) => {
        let urlPrefix = prefix;
        if (prefix) {
            urlPrefix = prefix.endsWith('/') ? prefix : `${prefix}/`;
        }
        setPageQuery({...pageQuery, prefix: urlPrefix});
        if (!prefix) {
            setBreadcrumb(breadcrumbItems?.slice(0, 1));
            return
        }
        const index = breadcrumbItems?.findIndex((item => item.title === prefix)) ?? -1;
        if (index !== -1) {
            setBreadcrumb(breadcrumbItems?.slice(0, index + 1));
            return
        }
        setBreadcrumb([...breadcrumbItems ?? [], {
            title: prefix,
            onClick: () => handleBreadcrumb(prefix),
        }])
    }, [breadcrumbItems, pageQuery])


    useEffect(() => {
        queryResource().then();
        const query = qs.stringify(pageQuery);
        navigate(`${BaseUrlConstant.TENANT_SPACE_URL}?${query}`)
    }, [navigate, pageQuery, queryResource])


    /**
     * table列选择
     */
    const rowSelection: TableRowSelection<Userinfo> = {
        onChange: (selectedRowKeys: React.Key[]) => setRowKeys(selectedRowKeys),
    };

    const [picViewUrl, setPicViewUrl] = useState<string | undefined>(undefined);
    const uploadFolderHandle = (fileList: RcFile[]) => {
        console.log(fileList)

    }

    const uploadButtonItems: MenuProps = {
        items: [
            {
                label: (
                    <S3Upload
                        uploadProps={{
                            isPublic: false,
                            businessName: breadcrumbItems?.length === 1 ? undefined : (breadcrumbItems![breadcrumbItems!.length! - 1].title as string),
                            onUploadSuccess: queryResource
                        }
                        }
                    >
                        {t('Common.uploadFile')}
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
                            businessName: breadcrumbItems?.length === 1 ? undefined : (breadcrumbItems![breadcrumbItems!.length! - 1].title as string),
                            onUploadSuccess: queryResource,
                            // beforeUpload: (file, fileList) => {
                            //     uploadFolderHandle(fileList);
                            //     return false; // 阻止默认上传行为
                            // }
                            onChange: (info) => {
                                console.log(info)
                            }
                        }
                        }
                    >
                        {t('Common.uploadFolder')}
                    </S3Upload>
                ),
                key: 'upload-folder',
                icon: <FolderOutlined/>,
            }
        ]
    }

    return (<>
        <Card>
            <div className={'tenant-space-header'}>
                <Flex gap={8}>
                    <IconFont type={'i-cunchu'} style={{fontSize: '2.5rem'}}/>
                    <Flex vertical justify={'center'} className={'space-bucket-info'}>
                        <h2>platform-bucket</h2>
                        <Space>
                            <span>创建时间：2025-01-10</span>
                            <span>Access: Private</span>
                            <span>4.5 MiB / 1.0 TiB - {pageResult?.total} Objects</span>
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
                            await queryResource();
                        }}
                    >
                        <DeleteButton disabled={rowKeys === undefined || rowKeys.length === 0}/>
                    </Popconfirm>
                    <Dropdown.Button icon={<UploadOutlined/>} menu={uploadButtonItems}>
                        上传文件
                    </Dropdown.Button>
                </Flex>
            </div>

            <div>
                <PageList
                    tableProps={{
                        // tableName: t('Resource.list'),
                        columns: columns,
                        pageData: pageResult,
                        pageQuery: pageQuery,
                        setPageQuery: setPageQuery,
                        rowSelection: rowSelection,
                        tableComponents: [
                            <>
                                <Flex justify={'center'}>
                                    <Button className={'back-button'}
                                            onClick={breadcrumbItems && breadcrumbItems[breadcrumbItems.length - 2]?.onClick}
                                    ><LeftOutlined/></Button>
                                    <Breadcrumb className={'space-bucket-breadcrumb'} items={breadcrumbItems}/>


                                </Flex>
                                {/*<PermissionButton buttonPermissions={buttonPermissions}*/}
                                {/*                  permissionStr={TenantPermissionConstant.ADD}>*/}
                                {/*    <AddButton onClick={() => navigate('/tenant-form')}/>*/}
                                {/*</PermissionButton>*/}
                                {/*<PermissionButton buttonPermissions={buttonPermissions}*/}
                                {/*                  permissionStr={TenantPermissionConstant.DELETE}>*/}
                                {/*    <Popconfirm*/}
                                {/*        title={t('Button.delete')}*/}
                                {/*        description={t('Button.deleteConfirm')}*/}
                                {/*        okText={t('Common.yes')}*/}
                                {/*        cancelText={t('Common.no')}*/}
                                {/*        onConfirm={async () => {*/}
                                {/*            await tenantApi.deleteInfoApi(rowKeys as string[]);*/}
                                {/*            await pageRequest();*/}
                                {/*        }}*/}
                                {/*    >*/}
                                {/*        <DeleteButton disabled={rowKeys === undefined || rowKeys.length === 0}/>*/}
                                {/*    </Popconfirm>*/}
                                {/*</PermissionButton>*/}
                            </>
                        ]
                    }}
                    // headerSearchProps={{
                    //     components: [
                    //         <><label htmlFor="tenantName">{t('Tenant.name')}</label>
                    //             <Input placeholder={t('Tenant.namePlaceholder')} id={'tenantName'} onChange={(e) => {
                    //                 setTenantQuery({tenantName: e.target.value})
                    //             }}/>
                    //         </>
                    //     ],
                    //     onSearchClick: () => setPageQuery({...pageQuery, ...tenantQuery})
                    // }}
                />
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