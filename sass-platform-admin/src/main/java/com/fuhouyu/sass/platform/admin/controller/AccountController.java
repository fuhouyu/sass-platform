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
package com.fuhouyu.sass.platform.admin.controller;

import com.fuhouyu.framework.common.response.BaseResponse;
import com.fuhouyu.framework.common.response.ResponseHelper;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.log.annotaions.LogModule;
import com.fuhouyu.framework.log.annotaions.LogRecord;
import com.fuhouyu.framework.log.enums.OperationTypeEnum;
import com.fuhouyu.framework.log.enums.RiskTypeEnum;
import com.fuhouyu.sass.platform.system.domain.dto.account.AccountDTO;
import com.fuhouyu.sass.platform.system.domain.dto.account.AccountIdDTO;
import com.fuhouyu.sass.platform.system.domain.dto.account.UpdatePasswordDTO;
import com.fuhouyu.sass.platform.system.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 账号web接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/24 21:24
 */
@RestController
@RequestMapping("/v1/account")
@Tag(name = "账号 web接口")
@RequiredArgsConstructor
@Slf4j
@Validated
@LogModule("用户账号模块")
public class AccountController {

    private final AccountService accountService;

    /**
     * 获取当前用户关联的第三方账号信息
     *
     * @return 第三方账号信息
     */
    @GetMapping("/me")
    @Operation(summary = "获取当前登录用户关联的账号信息")
    public BaseResponse<List<AccountDTO>> getAccountListForMe() {
        return ResponseHelper.success(this.accountService.findAccountListForMe(ContextHolderStrategy.getContext().getUser().getId()));
    }

    /**
     * 修改当前用户密码
     *
     * @param updatePasswordDTO 修改密码的dto
     * @return void
     */
    @PutMapping("/password")
    @Operation(summary = "修改当前用户密码")
    @LogRecord(operationType = OperationTypeEnum.UPDATE,
            riskType = RiskTypeEnum.HIGH_LEVEL)
    public BaseResponse<Void> updatePassword(@RequestBody @Valid UpdatePasswordDTO updatePasswordDTO) {
        this.accountService.updatePassword(updatePasswordDTO);
        return ResponseHelper.success();
    }

    /**
     * 绑定第三方账号信息
     *
     * @return void
     */
    @PostMapping("/bind")
    @Operation(summary = "第三方账号绑定")
    @LogRecord(operationType = OperationTypeEnum.UPDATE,
            riskType = RiskTypeEnum.HIGH_LEVEL,
            content = """
                    T(String).format('用户绑定第三方平台 [%s] 账号 [%s]', #accountIdDTO.getAccountType(), #accountIdDTO.getAccountId())
                    """,
            contentEn = """
                    T(String).format('User bind third-party platform [%s] account: [%s]', #accountIdDTO.getAccountType(), #accountIdDTO.getAccountId())
                    """
    )
    public BaseResponse<Void> bindThirdPartyAccount(@RequestBody AccountIdDTO accountIdDTO) {
        this.accountService.saveThirdPartyAccount(accountIdDTO);
        return ResponseHelper.success();
    }


    /**
     * 第三方账号取消绑定
     *
     * @param accountIdDTO 账号id
     * @return void
     */
    @DeleteMapping("/unbind")
    @Operation(summary = "第三方账号取消绑定")
    @LogRecord(operationType = OperationTypeEnum.UPDATE,
            riskType = RiskTypeEnum.HIGH_LEVEL,
            content = """
                    T(String).format('用户取消绑定第三方平台 [%s] 账号 [%s]', #accountIdDTO.getAccountType(), #accountIdDTO.getAccountId())
                    """,
            contentEn = """
                    T(String).format('User unbind third-party platform [%s] account: [%s]', #accountIdDTO.getAccountType(), #accountIdDTO.getAccountId())
                    """
    )
    public BaseResponse<Void> unbindThirdPartyAccount(@RequestBody AccountIdDTO accountIdDTO) {
        this.accountService.removeById(accountIdDTO);
        return ResponseHelper.success();
    }
}
