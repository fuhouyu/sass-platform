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


import {Breadcrumb} from "antd";
import {useMemo} from "react";
import {useAppSelector} from "@/store";
import {Menu} from "@/model/menu";
import './index.scss'

const getBreadcrumbName = (path: string, routers: Menu[]) => {
    for (const item of routers) {
        if (matchPath(path, item.routePath ?? '')) {
            return item.permissionName;
        }
        const children = item.children;
        if (children) {
            const childName: string = getBreadcrumbName(path, children);
            if (childName) return childName;
        }
    }
    return '';
};

export const Bread = () => {
    const userMenus = useAppSelector(state => state.user.userMenus);
    const pathSnippets = location.pathname.split('/').filter(i => i);

    const breadcrumb = useMemo(() => {
        return pathSnippets.map((_, index) => {
            const url = `/${pathSnippets.slice(0, index + 1).join('/')}`;
            const breadcrumbName = getBreadcrumbName(url, userMenus);
            return {
                title: breadcrumbName,
                key: url,
                href: url,
            };
        });
    }, [pathSnippets, userMenus]);

    return (
        <>
            <Breadcrumb className="breadcrumb"
                        items={breadcrumb}
            ></Breadcrumb>
        </>
    );
};

const matchPath = (currentPath: string, routePath: string) => {
    const pathRegex = new RegExp(`^${routePath.replace(/:\w+/g, '\\w+')}$`);
    return pathRegex.test(currentPath);
};