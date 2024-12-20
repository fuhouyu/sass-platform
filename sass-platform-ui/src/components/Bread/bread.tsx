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


import {Breadcrumb, BreadcrumbProps} from "antd";
import {useMemo} from "react";
import {useAppSelector} from "@/store";
import {Menu} from "@/model/menu";
import './index.scss'
import {Link} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {BASE_HOME_URL} from "@/constants/commonConstant";


const getBreadcrumbName = (path: string, routers: Menu[]) => {
    for (const item of routers) {
        if (matchPath(path, item.routePath ?? '')) {
            return item.permissionName;
        }
        const children = item.children;
        if (children) {
            const childName: string = getBreadcrumbName(path, children) ?? '';
            if (childName) return childName;
        }
    }
    return '';
};

const itemRender: BreadcrumbProps<object>['itemRender'] = (currentRoute, _params, items, paths) => {
    const isLast = currentRoute?.path === items[items.length - 1]?.path;
    return isLast ? (
        <span>{currentRoute.title}</span>
    ) : (
        <Link to={`/${paths.join("/")}`}>{currentRoute.title}</Link>
    );
}


export const Bread = () => {
    const userMenus = useAppSelector(state => state.user.userMenus);
    const pathname = location.pathname;
    const {t} = useTranslation();

    const breadcrumb = useMemo(() => {
        if (pathname === '/') {
            return [];
        }
        if (pathname === BASE_HOME_URL) {
            return [{
                title: t(`Menu.home`),
                key: pathname,
                path: pathname,
            }];
        }

        const pathSnippets = pathname.split('/').filter(i => i);
        return pathSnippets.map((path) => {
            const breadcrumbName = getBreadcrumbName(path, userMenus);
            if (breadcrumbName === '') {
                return undefined
            }
            return {
                title: t(`Menu.${breadcrumbName}`),
                key: path,
                path: path,
            };
        });
    }, [pathname, t, userMenus])
        .filter(item => item !== undefined);
    return (
        <>
            <Breadcrumb className="breadcrumb"
                        items={breadcrumb}
                        itemRender={itemRender}
            ></Breadcrumb>
        </>
    );
};

const matchPath = (currentPath: string, routePath: string) => {
    const normalizedCurrentPath = currentPath.replace(/^\//, '');   // 移除开头的斜杠
    const normalizedRoutePath = routePath.replace(/^\//, '');      // 移除开头的斜杠

    const pathSegments = normalizedCurrentPath.split('/');
    const routeSegments = normalizedRoutePath.split('/');

    // 逐段匹配
    return routeSegments.every((segment, index) => {
        if (segment.startsWith(':')) {
            // 动态参数匹配
            return true;
        }
        // 精确匹配路径段
        return pathSegments[index] === segment;
    });
};
