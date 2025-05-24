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

import {
  IMatomoCountryVisit,
  IMatomoVisitQuery,
  IMatomoVisitSummary
} from "@/types/matomoVisitSummary";
import {request} from "@/utils";
import {BaseApiUrlConstant} from "@/constants/baseUrlConstant.ts";

class MatomoApi {

    private readonly _baseUrl: string;


    constructor(baseUrl: string) {
        this._baseUrl = baseUrl;
    }

    /**
     * 获取访问统计
     * @param query 查询对象
     */
    getVisitSummary: (query: IMatomoVisitQuery) => Promise<IMatomoVisitSummary[]> = (query: IMatomoVisitQuery): Promise<IMatomoVisitSummary[]> =>
        request.get(`${this._baseUrl}/visit-summary`, {
            params: query
        });

    /**
     * 获取国家访问统计
     * @param query 查询对象
     */
    getCountryVisit: (query: IMatomoVisitQuery) => Promise<IMatomoCountryVisit[]> = (query: IMatomoVisitQuery): Promise<IMatomoCountryVisit[]> =>
        request.get(`${this._baseUrl}/country-visit`, {
            params: query
        });
}

export const matomoApi: MatomoApi = new MatomoApi(BaseApiUrlConstant.MATOMO_API_URL);
