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

import useAuth from "@/hooks/useAuth.tsx";
import {Outlet, useLocation, useNavigate} from "react-router-dom";
import React, {useEffect, useState} from "react";
import Layout, {Content} from "antd/es/layout/layout";
import {LayoutHeader} from "@/pages/Layout/header";
import {LayoutMenu} from "@/pages/Layout/menu";
import './index.scss'
import {Bread} from "@/components";
import {BaseUrlConstant} from "@/constants/baseUrlConstant.tsx";
import {QuestionCircleFilled, UnorderedListOutlined} from "@ant-design/icons";
import {Affix, Badge, Button, Drawer, Progress, Space, Table, TableColumnsType, Tooltip} from "antd";
import {UploadFile, useUploadStore} from "@/store/modules/upload.tsx";
import {useTranslation} from "react-i18next";
import {FileUtils} from "@/utils/fileUtil.tsx";

export const LayoutMain = () => {
    const accessToken = useAuth();
    const location = useLocation();
    const {uploadFiles, removeUploadFile} = useUploadStore(state => state);
    const {t} = useTranslation();
    const [visible, setVisible] = useState<boolean>(false);

    const showDrawer = () => {
        setVisible(true);
    };

    const onClose = () => {
        setVisible(false);
    };
    const navigate = useNavigate();

    useEffect(() => {
        if (!accessToken) {
            navigate(BaseUrlConstant.LOGIN_URL, {state: {from: location.pathname}});
        }
    }, [accessToken, navigate, location]);

    const uploadFileColumns: TableColumnsType<UploadFile> = [
        {
            title: t('Resource.name'),
            dataIndex: 'name',
            key: 'name',
            align: 'center',
        },
        {
            title: t('Resource.size'),
            dataIndex: 'size',
            key: 'size',
            align: 'center',
            render: (size: number) => FileUtils.formatSize(size),
        },
        {
            title: t('Resource.upload.status'),
            dataIndex: 'status',
            key: 'status',
            align: 'center',
            render: (status: string, uploadFile) => {
                if (status === 'error') {
                    return <Space>
                        <span>{t(`Resource.upload.${status}`)}</span>
                        <Tooltip title={uploadFile.errorMessage} color={'red'}>
                            <QuestionCircleFilled style={{color: 'red'}}/>
                        </Tooltip>
                    </Space>
                }
                return <span>{t(`Resource.upload.${status}`)}</span>


            }
        },
        {
            title: t('Resource.upload.progress'),
            dataIndex: 'progress',
            key: 'progress',
            align: 'center',
            render: (progress: number, uploadFile) => {
                if (uploadFile.status === 'canceled') {
                    return <Progress percent={0} status={'exception'}/>;
                }
                if (uploadFile.status === 'success') {
                    return <Progress percent={100} status={'success'}/>;
                }
                return <Progress percent={progress} status={'active'}/>;
            }
        },
        {
            title: t('Common.action'),
            dataIndex: 'action',
            key: 'action',
            align: 'center',
            render: (_, uploadFile: UploadFile) => {
                if (uploadFile.status === 'canceled' || uploadFile.status === 'success' || uploadFile.status === 'error') {
                    return <Button
                        type={'link'}
                        onClick={() => removeUploadFile(uploadFile.id)}
                    >
                        {t('Resource.upload.remove')}
                    </Button>
                }
                return (
                    <Button
                        type={'link'}
                        onClick={() => {
                            uploadFile.abortController?.abort();
                        }}
                    >
                        {t('Resource.upload.cancel')}
                    </Button>
                );
            }
        },

    ]


    return (
        <Layout className={'app-container'}
        >
            <LayoutHeader/>
            <Content className={'layout-content'}>
                <Layout>
                    <LayoutMenu/>
                    {uploadFiles.length > 0 && <Affix className={'task-affix-container'}>
                        <Badge count={uploadFiles.length}
                               offset={[-30, 0]}
                        >
                            <Button
                                className={'task-button'}
                                variant={'filled'}
                                icon={<UnorderedListOutlined/>}
                                size="small"
                                onClick={showDrawer}
                            >

                                <span className="task-button-text">{t('Resource.upload.list')}</span>

                            </Button>
                        </Badge>
                    </Affix>}
                    <Drawer
                        className={'task-table-container'}
                        title={t('Resource.upload.list')}
                        placement="right"
                        closable={true}
                        onClose={onClose}
                        open={visible}
                        width={'40%'}

                    >
                        <Table<UploadFile>
                            rowKey={'id'}
                            pagination={false}
                            columns={uploadFileColumns} dataSource={uploadFiles}/>
                    </Drawer>

                    <Content className="layout-content">
                        <Bread/>
                        <Outlet/>
                    </Content>
                </Layout>
            </Content>
        </Layout>
    );
}