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


import React, {useEffect, useState} from "react";
import {Button, Card, Space, TableColumnsType} from "antd";
import {IconFont, PageList} from "@/components";
import {useTranslation} from "react-i18next";
import {PageQuery, PageResult} from "@/model/pageQuery";
import {Resource} from "@/model/resource.tsx";
import {resourceApi} from "@/apis/resource.tsx";
import type {TableRowSelection} from "antd/es/table/interface";
import {Userinfo} from "@/model/user.tsx";
import {useResourcePreview} from "@/hooks/useResourcePreview.tsx";
import './index.scss'
import {UploadOutlined} from "@ant-design/icons";

const TenantSpace: React.FC = () => {

    const {t} = useTranslation();

    const [pageResult, setPageResult] = useState<PageResult<Resource>>();
    const [rowKeys, setRowKeys] = useState<React.Key[]>([])
    const {previewUrl} = useResourcePreview();


    const columns: TableColumnsType = [
        {
            title: t('Resource.name'),
            dataIndex: 'name',
            align: 'center',
            render: (_, record) => {
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
            align: "center"
        },
        {
            title: t('Common.updateAt'),
            dataIndex: 'updateAt',
            align: "center",
        },
        {
            title: t('Common.updateBy'),
            dataIndex: 'updateBy',
            align: "center",
        },
        {
            title: t('Common.action'),
            dataIndex: 'action',
            align: "center",
            render: (_, record) => {
                return <Button
                    onClick={() => setPicViewUrl(previewUrl(record.id))}
                    icon={<IconFont type="i-yulan"/>}>
                    {t('Resource.preview')}
                </Button>
            }
        }
    ];

    /**
     * 分页查询
     */
    const [pageQuery, setPageQuery] = useState<PageQuery>({
        pageNum: 1,
        pageSize: 10,
        tenantId: 1,
    });

    /**
     * 分页查询结果
     */
    useEffect(() => {
        resourceApi.pageInfoListApi(pageQuery)
            .then((res: PageResult<Resource>) => {
                setPageResult({...res});
            });
    }, [pageQuery]);

    /**
     * table列选择
     */
    const rowSelection: TableRowSelection<Userinfo> = {
        onChange: (selectedRowKeys: React.Key[]) => setRowKeys(selectedRowKeys),
    };

    const [picViewUrl, setPicViewUrl] = useState<string | undefined>(undefined);

    return (<>
        <Card>
            <div className={'tenant-space-header'}>
                <div>
                    <IconFont type={'i-cunchu'}/>
                </div>
                <div className={'space-button'}>
                    <Button type="primary" icon={<UploadOutlined/>}> 上传文件</Button>
                </div>
            </div>
            <PageList
                tableProps={{
                    tableName: t('Resource.list'),
                    columns: columns,
                    pageData: pageResult,
                    pageQuery: pageQuery,
                    setPageQuery: setPageQuery,
                    rowSelection: rowSelection,
                    tableComponents: [
                        <>
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