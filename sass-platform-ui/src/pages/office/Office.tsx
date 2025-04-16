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

import React, {FC, useCallback, useEffect} from "react";
import {useParams, useSearchParams} from "react-router-dom";
import {useLocaleStore, useUserStore} from "@/store";
import {message} from "antd";
import {OnlyOffice} from "@/model/office.tsx";
import {useResourceAction} from "@/hooks/useResourceAction.tsx";
import {onlyOfficeApi} from "@/apis/onlyOffice.tsx";
import {DocumentEditor} from "@onlyoffice/document-editor-react";
import {usePageTitle} from "@/hooks/usePageTitle.tsx";

function onLoadComponentError(errorCode: number, errorDescription: string) {
    switch (errorCode) {
        case -1: // Unknown error loading component
            console.log(errorDescription)
            break

        case -2: // Error load DocsAPI from http://documentserver/
            console.log(errorDescription)
            break

        case -3: // DocsAPI is not defined
            console.log(errorDescription)
            break
    }
}


export const Office: FC = () => {
    usePageTitle('Menu.office');
    const {id} = useParams();
    const [params] = useSearchParams();
    const {userinfo, fetchUserinfo} = useUserStore(state => state);
    const [officeView, setOfficeView] = React.useState<OnlyOffice>({} as OnlyOffice)
    const {preview} = useResourceAction();
    const language = useLocaleStore(state => state.language);

    const initOfficeView = useCallback(async () => {
        if (!id) {
            message.error("没有找到该资源").then();
            return <div></div>;
        }
        const onlyOffice: OnlyOffice = await onlyOfficeApi.view({id: id, mode: params.get('mode') || 'VIEW'});
        setOfficeView(onlyOffice);
    }, [id, params])

    useEffect(() => {
        fetchUserinfo().then();
        initOfficeView().then();
    }, [fetchUserinfo, initOfficeView])

    return (
        officeView.config &&
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
            onLoadComponentError={onLoadComponentError}
        />
    )
}