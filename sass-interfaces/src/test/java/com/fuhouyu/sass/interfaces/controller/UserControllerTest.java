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
package com.fuhouyu.sass.interfaces.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fuhouyu.framework.common.response.BaseResponse;
import com.fuhouyu.framework.common.utils.JacksonUtil;
import com.fuhouyu.framework.web.response.SuccessResponse;
import com.fuhouyu.sass.domain.service.UserAccountService;
import com.fuhouyu.sass.interfaces.controller.constants.WebConstant;
import com.fuhouyu.sass.interfaces.controller.dto.user.UserLoginCommand;
import com.fuhouyu.sass.interfaces.controller.dto.user.UserTokenDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * 用户控制层测试类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/5 12:32
 */
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserAccountService userAccountService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockmvc;

    @BeforeEach
    void setUp() {
        mockmvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void userLogin() throws Exception {
        UserLoginCommand userLoginCommand = new UserLoginCommand();
        userLoginCommand.setUsername("admin");
        userLoginCommand.setPassword("password");
        userLoginCommand.setLoginType("password");
        MockHttpServletRequestBuilder builder
                = MockMvcRequestBuilders.post(WebConstant.USER_CONTROLLER_PATH + "/login")
                .content(JacksonUtil.writeValueAsBytes(userLoginCommand))
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockHttpServletResponse response = mockmvc.perform(builder).andReturn().getResponse();
        assertEquals(200, response.getStatus());
        BaseResponse<UserTokenDTO> restResult = JacksonUtil.readValue(response.getContentAsByteArray(), new TypeReference<SuccessResponse<UserTokenDTO>>() {
        });
        Assertions.assertTrue(restResult.getIsSuccess());
    }
}
