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

import React, {FC, Suspense, useCallback, useEffect, useState} from "react";
import {useParams, useSearchParams} from "react-router-dom";
import {useLocaleStore, useUserStore} from "@/store";
import {message} from "antd";
import {OnlyOffice} from "@/model/office.tsx";
import {onlyOfficeApi} from "@/apis/onlyOffice.tsx";
import {usePageTitle} from "@/hooks/usePageTitle.tsx";
import {Userinfo} from "@/model/user.tsx";
import {PageLoading} from "@/components";
import {useTranslation} from "react-i18next";
import {useResourceAction} from "@/hooks/useResourceAction.tsx";


const DocumentEditor = React.lazy(() =>
    import('@onlyoffice/document-editor-react').then(module => ({
        default: module.DocumentEditor
    }))
);


export const Office: FC = () => {
    usePageTitle('Menu.office');
    const {id} = useParams();
    const {t} = useTranslation();
    const [params] = useSearchParams();
    const [officeView, setOfficeView] = React.useState<OnlyOffice>({} as OnlyOffice)
    const {fetchUserinfo} = useUserStore(state => state);
    const {preview} = useResourceAction();
    const [userinfo, setUserinfo] = useState<Userinfo | undefined>(undefined);
    const language = useLocaleStore(state => state.language);

    const initOfficeView = useCallback(async () => {
        if (!id) {
            message.error("没有找到该资源").then();
            return <div></div>;
        }

        const onlyOffice: OnlyOffice = params.get('mode') === 'VIEW' ?
            await onlyOfficeApi.view(id) : await onlyOfficeApi.edit(id);
        setOfficeView(onlyOffice);
    }, [id, params]);

    useEffect(() => {
        fetchUserinfo().then(res => {
            setUserinfo(res);
        });
    }, [fetchUserinfo]);

    useEffect(() => {
        initOfficeView().then();
    }, [initOfficeView]);

    if (userinfo === undefined) {
        return <PageLoading/>
    }


    return (

        <Suspense fallback={<PageLoading title={t('Common.resourceLoading')}/>}>
            <DocumentEditor
                id="documentEditor"
                documentServerUrl={officeView?.documentServerUrl}
                config={{
                    ...officeView?.config,
                    editorConfig: {
                        user: {
                            id: userinfo?.id,
                            name: userinfo?.realName,
                            image: preview(userinfo.avatar)
                        },
                        lang: language
                    },
                }}
                onLoadComponentError={(_, errorDescription) => console.log(errorDescription)}
            />
        </Suspense>
    );
}