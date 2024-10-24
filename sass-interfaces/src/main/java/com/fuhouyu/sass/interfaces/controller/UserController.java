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

import com.fuhouyu.framework.common.response.BaseResponse;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.web.enums.ResponseCodeEnum;
import com.fuhouyu.framework.web.exception.WebServiceException;
import com.fuhouyu.framework.web.response.ResponseHelper;
import com.fuhouyu.sass.domain.model.account.AccountEntity;
import com.fuhouyu.sass.domain.model.page.PageQueryValue;
import com.fuhouyu.sass.domain.model.page.PageResultEntity;
import com.fuhouyu.sass.domain.model.token.TokenValueEntity;
import com.fuhouyu.sass.domain.model.user.UserEntity;
import com.fuhouyu.sass.domain.service.UserAccountService;
import com.fuhouyu.sass.domain.service.UserService;
import com.fuhouyu.sass.interfaces.controller.assembler.PageQueryAssembler;
import com.fuhouyu.sass.interfaces.controller.assembler.UserLoginAssembler;
import com.fuhouyu.sass.interfaces.controller.assembler.UserinfoAssembler;
import com.fuhouyu.sass.interfaces.controller.constants.WebConstant;
import com.fuhouyu.sass.interfaces.controller.dto.BasePageQueryDTO;
import com.fuhouyu.sass.interfaces.controller.dto.PageQueryResultDTO;
import com.fuhouyu.sass.interfaces.controller.dto.user.UserLoginCommand;
import com.fuhouyu.sass.interfaces.controller.dto.user.UserTokenDTO;
import com.fuhouyu.sass.interfaces.controller.dto.user.UserinfoDTO;
import com.fuhouyu.sass.interfaces.controller.dto.user.UserinfoEditCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户控制层
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/4 21:50
 */
@RestController
@RequestMapping(WebConstant.USER_CONTROLLER_PATH)
@Validated
@Tag(name = "用户前端控制层")
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final UserLoginAssembler USER_LOGIN_ASSEMBLER
            = UserLoginAssembler.INSTANCE;

    private static final UserinfoAssembler USER_INFO_ASSEMBLER = UserinfoAssembler.INSTANCE;

    private static final PageQueryAssembler PAGE_QUERY_ASSEMBLER = PageQueryAssembler.INSTANCE;

    private final UserAccountService userAccountService;

    private final UserService userService;


    /**
     * 用户登录
     *
     * @param userLoginCommand 用户登录操作
     * @return 响应
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录接口")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", required = true,
            example = "Basic dGVzdDE6cGFzc3dvcmQ=")
    public BaseResponse<UserTokenDTO> login(@RequestBody @Validated UserLoginCommand userLoginCommand) {
        AccountEntity accountEntity = USER_LOGIN_ASSEMBLER.toAccountEntity(userLoginCommand);
        try {
            TokenValueEntity tokenValueEntity = this.userAccountService.login(accountEntity);
            UserTokenDTO userTokenDTO = USER_LOGIN_ASSEMBLER.toUserTokenDTO(tokenValueEntity);
            return ResponseHelper.success(userTokenDTO);
        } catch (Exception e) {
            LoggerUtil.error(logger, "用户: {} 使用 {} 方式登录失败: {} ",
                    userLoginCommand.getUsername(), userLoginCommand.getLoginType(), e.getMessage(), e);
            throw new WebServiceException(
                    ResponseCodeEnum.INVALID_PARAM,
                    "用户名或密码错误");
        }
    }


    /**
     * 退出登录
     *
     * @return 响应
     */
    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public BaseResponse<Void> logout() {
        this.userAccountService.logout();
        return ResponseHelper.success();
    }

    /**
     * 登录用户的用户详情
     *
     * @return 用户详情
     */
    @Operation(summary = "用户详情")
    @GetMapping("/info")
    public BaseResponse<UserinfoDTO> userinfo() {
        Long userId = ContextHolderStrategy.getContext().getUser().getId();
        UserEntity userEntity = this.userService.findByUserId(userId);
        UserinfoDTO userinfo = USER_INFO_ASSEMBLER.toUserInfo(userEntity);
        return ResponseHelper.success(userinfo);
    }

    /**
     * 修改当前用户的详情
     *
     * @param userinfoEditCommand 用户详情操作
     * @return restResult
     */
    @PutMapping("/info")
    @Operation(summary = "修改当前的用户详情")
    public BaseResponse<Void> editUserinfo(@Validated @RequestBody UserinfoEditCommand userinfoEditCommand) {
        UserEntity userEntity = USER_INFO_ASSEMBLER.toUserEntity(userinfoEditCommand);
        userEntity.setId(ContextHolderStrategy.getContext().getUser().getId());
        this.userService.editUser(userEntity);
        return ResponseHelper.success();
    }

    /**
     * 分页查询用户列表
     *
     * @param basePageQuery 颁
     * @return 用户列表集合
     */
    @GetMapping("/list")
    @Operation(summary = "获取用户列表")
    public BaseResponse<PageQueryResultDTO<UserinfoDTO>> pageUserinfo(BasePageQueryDTO basePageQuery) {
        PageQueryValue pageQueryValue = PAGE_QUERY_ASSEMBLER.toPageQuery(basePageQuery);
        PageResultEntity<UserEntity> pageUserEntityResult = this.userService.pageUserList(pageQueryValue);
        PageQueryResultDTO<UserinfoDTO> pageQueryResultDTO = new PageQueryResultDTO<>(pageUserEntityResult.getPageNum(),
                pageUserEntityResult.getPageSize(), pageUserEntityResult.getTotal(),
                USER_INFO_ASSEMBLER.toUserInfoList(pageUserEntityResult.getList()));
        return ResponseHelper.success(pageQueryResultDTO);
    }

    /**
     * 通过用户id删除用户
     *
     * @param ids 用户id集合
     * @return 成功响应
     */
    @Operation(summary = "通过用户id删除用户")
    @DeleteMapping
    public BaseResponse<Void> removeUserList(
            @RequestBody
            @Size(min = 1, message = "需要删除的用户不能为空")
            @NotNull(message = "需要删除的用户不能为空") List<Long> ids) {
        this.userService.removeUserListByIds(ids);
        return ResponseHelper.success();
    }
}
