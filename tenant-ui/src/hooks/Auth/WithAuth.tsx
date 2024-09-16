/*
 * Copyright 2024-2034 the original author or authors.
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


import React, {useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import {message} from "antd";

// 使用具名函数组件来创建高阶组件
const withAuth = <P extends object>(WrappedComponent: React.ComponentType<P>) => {
    // 具名函数组件
    const AuthenticatedComponent: React.FC<P> = (props) => {
        const navigate = useNavigate();
        useEffect(() => {
            const token = localStorage.getItem('token');
            if (!token) {
                message.warning("当前用户登录状态已失效");
                navigate('/login'); // 未登录则重定向
            }
        }, [navigate]);

        // 渲染传入的组件
        return <WrappedComponent {...props} />;
    };

    // 为 AuthenticatedComponent 设置 displayName
    AuthenticatedComponent.displayName = `Authenticated(${WrappedComponent.displayName || WrappedComponent.name || 'Component'})`;

    return React.memo(AuthenticatedComponent); // 使用 React.memo 包裹以优化性能
};

export default withAuth;
