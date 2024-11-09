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
package com.fuhouyu.sass.platform.admin.controller;

import com.fuhouyu.framework.common.response.BaseResponse;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.security.entity.TokenEntity;
import com.fuhouyu.framework.web.enums.ResponseCodeEnum;
import com.fuhouyu.framework.web.exception.WebServiceException;
import com.fuhouyu.framework.web.response.ResponseHelper;
import com.fuhouyu.sass.platform.admin.assembler.PageQueryAssembler;
import com.fuhouyu.sass.platform.admin.assembler.UserAccountAssembler;
import com.fuhouyu.sass.platform.admin.assembler.UserAssembler;
import com.fuhouyu.sass.platform.admin.assembler.UserLoginAssembler;
import com.fuhouyu.sass.platform.admin.constants.WebConstant;
import com.fuhouyu.sass.platform.admin.vo.BasePageQueryVO;
import com.fuhouyu.sass.platform.admin.vo.PageQueryResultVO;
import com.fuhouyu.sass.platform.admin.vo.user.SaveUserinfoVO;
import com.fuhouyu.sass.platform.admin.vo.user.UserLoginVO;
import com.fuhouyu.sass.platform.admin.vo.user.UserTokenVO;
import com.fuhouyu.sass.platform.admin.vo.user.UserinfoVO;
import com.fuhouyu.sass.platform.system.dto.*;
import com.fuhouyu.sass.platform.system.enums.AccountTypeEnum;
import com.fuhouyu.sass.platform.system.service.UserAccountService;
import com.fuhouyu.sass.platform.system.service.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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
@Slf4j
public class UserController {

    private static final UserLoginAssembler USER_LOGIN_ASSEMBLER = UserLoginAssembler.INSTANCE;

    private static final UserAccountAssembler USER_ACCOUNT_ASSEMBLER = UserAccountAssembler.INSTANCE;

    private static final UserAssembler USER_ASSEMBLER = UserAssembler.INSTANCE;

    private static final PageQueryAssembler PAGE_QUERY_ASSEMBLER = PageQueryAssembler.INSTANCE;

    private final UserService userService;

    private final UserAccountService userAccountService;

    /**
     * 用户登录
     *
     * @param userLoginVO 用户登录vo对象
     * @return 响应
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录接口")
    public BaseResponse<UserTokenVO> login(@RequestBody @Validated UserLoginVO userLoginVO) {
        LoginAccountDTO loginAccountDTO = USER_LOGIN_ASSEMBLER.toLoginAccountDTO(userLoginVO);
        try {
            TokenEntity tokenEntity = this.userAccountService.login(loginAccountDTO);
            UserTokenVO userTokenVO = USER_LOGIN_ASSEMBLER.toUserTokenVO(tokenEntity);
            return ResponseHelper.success(userTokenVO);
        } catch (Exception e) {
            LoggerUtil.error(log, "用户: {} 使用 {} 方式登录失败: {} ",
                    userLoginVO.getUsername(), userLoginVO.getLoginType(), e.getMessage(), e);
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
    public BaseResponse<UserinfoVO> userinfo() {
        Long userId = ContextHolderStrategy.getContext().getUser().getId();
        UserinfoDTO userinfoDTO = this.userService.findById(userId);
        UserinfoVO userinfo = USER_ASSEMBLER.toUserVO(userinfoDTO);
        return ResponseHelper.success(userinfo);
    }


    /**
     * 通过id获取用户详情
     *
     * @return 用户详情
     */
    @Operation(summary = "用户详情")
    @GetMapping("/info/{id}")
    public BaseResponse<UserinfoVO> userinfo(@PathVariable("id") Long id) {
        UserinfoDTO userinfoDTO = this.userService.findById(id);
        UserinfoVO userinfo = USER_ASSEMBLER.toUserVO(userinfoDTO);
        return ResponseHelper.success(userinfo);
    }

    /**
     * 修改当前用户的详情
     *
     * @param userinfoVO 用户详情操作
     * @return restResult
     */
    @PutMapping("/info")
    @Operation(summary = "修改当前的用户详情")
    public BaseResponse<Void> editUserinfo(@Validated @RequestBody UserinfoVO userinfoVO) {
        UserinfoDTO userinfoDTO = USER_ASSEMBLER.toUserDTO(userinfoVO);
        userinfoDTO.setId(ContextHolderStrategy.getContext().getUser().getId());
        this.userService.edit(userinfoDTO);
        return ResponseHelper.success();
    }

    /**
     * 修改当前用户的详情
     *
     * @param userinfoVO 用户详情操作
     * @param id     主键id
     * @return restResult
     */
    @PutMapping("/info/{id}")
    @Operation(summary = "修改当前的用户详情")
    public BaseResponse<Void> editUserinfo(
            @PathVariable("id") Long id,
            @Validated @RequestBody UserinfoVO userinfoVO) {
        UserinfoDTO userinfoDTO = USER_ASSEMBLER.toUserDTO(userinfoVO);
        userinfoDTO.setId(id);
        this.userService.edit(userinfoDTO);
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
    public BaseResponse<PageQueryResultVO<UserinfoVO>> pageUserinfo(BasePageQueryVO basePageQuery) {
        PageQueryDTO pageQuery = PAGE_QUERY_ASSEMBLER.toPageQuery(basePageQuery);
        PageInfo<UserinfoDTO> pageUserEntityResult = this.userService.pageList(pageQuery);
        PageQueryResultVO<UserinfoVO> pageQueryResultVO = new PageQueryResultVO<>(pageUserEntityResult.getPageNum(),
                pageUserEntityResult.getPageSize(), pageUserEntityResult.getTotal(),
                USER_ASSEMBLER.toUserInfoList(pageUserEntityResult.getList()));
        return ResponseHelper.success(pageQueryResultVO);
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
        this.userService.removeByIds(ids);
        return ResponseHelper.success();
    }


    /**
     * 校验用户名是否存在
     *
     * @param username 用户名
     * @return true 已存在，false不存在
     */
    @GetMapping("/exists")
    @Operation(summary = "校验用户名是否存在，如果存在，则返回true")
    @Parameter(name = "username", description = "用户名称")
    public BaseResponse<Boolean> validUsernameExists(@RequestParam("username") String username) {
        UserinfoDTO userinfoDTO = this.userService.findByUsername(username);
        return ResponseHelper.success(Objects.nonNull(userinfoDTO));
    }

    /**
     * 保存用户信息
     *
     * @param userinfoVO 用户的vo对象
     * @return 响应
     */
    @Operation(summary = "保存用户信息")
    @PostMapping("/info")
    public BaseResponse<Void> saveUser(@RequestBody SaveUserinfoVO userinfoVO) {
        UserinfoAccountDTO userinfoAccountDTO = USER_ACCOUNT_ASSEMBLER.toUserinfoAccountDTO(userinfoVO);
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccount(userinfoVO.getUsername());
        accountDTO.setAccountType(AccountTypeEnum.PASSWORD.name());
        accountDTO.setCredentials(userinfoVO.getPassword());
        userinfoAccountDTO.addAccount(accountDTO);
        this.userAccountService.register(userinfoAccountDTO);
        return ResponseHelper.success();
    }
}
