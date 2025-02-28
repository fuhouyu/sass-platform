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
import {Outlet, useLocation} from "react-router-dom";
import {useEffect} from "react";
import {router} from "@/routes/routers.tsx";
import Layout, {Content} from "antd/es/layout/layout";
import {LayoutHeader} from "@/pages/Layout/header";
import {LayoutMenu} from "@/pages/Layout/menu";
import './index.scss'
import {Bread} from "@/components";
import {BaseUrlConstant} from "@/constants/baseUrlConstant.tsx";
import FloatButtonGroup from "antd/es/float-button/FloatButtonGroup";
import {CloudUploadOutlined} from "@ant-design/icons";
import {Card, Progress} from "antd";
import {useUploadStore} from "@/store/modules/upload.tsx";

export const LayoutMain = () => {
    const accessToken = useAuth();
    const pathname = useLocation().pathname;
    const uploadFiles = useUploadStore(state => state.uploadFiles);
    useEffect(() => {
        if (!accessToken) {
            router.navigate(BaseUrlConstant.LOGIN_URL, {state: {from: pathname}}).then();
        }
    }, [accessToken, pathname]);
    return (
        <Layout className={'layout-container'}>
            <LayoutHeader/>
            <Content>
                <Layout>
                    <LayoutMenu/>
                    {uploadFiles.length > 0 ? <FloatButtonGroup
                        badge={{count: uploadFiles.length}}
                        className={'upload-container'}
                        trigger={'click'}
                        icon={<CloudUploadOutlined/>}>
                        <Card title={'上传文件'} className={'upload-container'}>
                            {uploadFiles.map(uploadFile => {
                                return (
                                    <>
                                        <div className={'upload-content'}>
                                            <span className={'upload-headItem'}>{uploadFile.name}</span>
                                            <Progress percent={uploadFile.progress}/>
                                        </div>
                                    </>
                                )
                            })}
                        </Card>
                    </FloatButtonGroup> : null}

                    <Content className="layout-content">
                        <Bread/>
                        <Outlet/>
                    </Content>
                </Layout>
            </Content>
        </Layout>
    )
}