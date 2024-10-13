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


import React, {useEffect} from 'react';
import {useLocation, useNavigate} from 'react-router-dom';
import {message} from "antd";
import {getToken} from "@/utils";
import Login from "@/pages/Login";

// 使用具名函数组件来创建高阶组件
const withAuth = (WrappedComponent: React.FC): React.FC => {
    // 具名函数组件
    const AuthenticatedComponent: React.FC = () => {
        const navigate = useNavigate();
        const location = useLocation();
        const pathname = location.pathname;
        const token = getToken();
        useEffect(() => {
            if (!token) {
                message.warning("当前用户登录状态已失效");
                navigate('/login', {state: {from: pathname}});
            }
        }, [navigate])
        if (token) {
            return <WrappedComponent/>;
        } else {
            return <Login/>;
        }
    };

// 使用 React.memo 包裹以优化性能
    return React.memo(AuthenticatedComponent);
};

export default withAuth;
