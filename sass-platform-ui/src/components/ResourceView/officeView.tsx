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

import {DocumentEditor} from "@onlyoffice/document-editor-react";
import {ResourceViewProps} from "@components/ResourceView/interface.tsx";
import React, {useCallback, useEffect} from "react";
import {onlyOfficeApi} from "@/apis/onlyOffice.tsx";
import {OnlyOffice} from "@/model/office.tsx";
import {useUserStore} from "@/store";

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

export const OfficeView = (resourceView: ResourceViewProps) => {
    const userinfo = useUserStore(state => state.userinfo);
    const [officeView, setOfficeView] = React.useState<OnlyOffice>({} as OnlyOffice)

    const initOfficeView = useCallback(async () => {
        const onlyOffice: OnlyOffice = await onlyOfficeApi.view({...resourceView});
        setOfficeView(onlyOffice);
    }, [resourceView]);


    useEffect(() => {
        initOfficeView().then();
    }, [initOfficeView])


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
                    }
                }
            }}
            onLoadComponentError={onLoadComponentError}
        />
    )
}