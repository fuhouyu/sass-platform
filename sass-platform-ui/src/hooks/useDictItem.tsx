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

import {useAppDispatch, useAppSelector} from "@/store";
import {dictStore} from "@/store/modules/dict.tsx";
import {dictItemApi} from "@/apis/dictItem.tsx";
import {useCallback, useEffect} from "react";

/**
 * 获取字典项
 * @param dictCodes 字典编码，以,分隔
 */
export function useDictItem(dictCodes: string) {

    const dispatch = useAppDispatch();
    const dictTypeItemMapping = useAppSelector(state => state.dict.dictTypeItemMapping);

    const initDictItemType = useCallback(async (dictCodes: string) => {
        const dictItemMapping = await dictItemApi.getDictItemTypeMappingList(dictCodes);
        dispatch(dictStore.actions.storeDictItem(dictItemMapping));
    }, [dispatch]);

    /**
     * 通过字典编码获取字典项
     */
    const getDictItemByDictCode = useCallback((dictCode: string) => {
        return dictTypeItemMapping[dictCode] ?? [];
    }, [dictTypeItemMapping])

    useEffect(() => {
        initDictItemType(dictCodes).then();
    }, [dictCodes, initDictItemType]);

    return {
        getDictItemByDictCode
    };
}