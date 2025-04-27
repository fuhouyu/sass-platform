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
package com.fuhouyu.sass.platform.system.domain.dto.office;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * onlyOffice回调dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/26 21:36
 */
@Data
@Schema(name = "OnlyOfficeCallbackDTO", description = "onlyOffice 回调dto对象", hidden = true)
public class OnlyOfficeCallbackDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1541235754123548572L;

    @Schema(name = "actions", description = "用户操作")
    private List<Action> actions;

    @Schema(name = "changesUrl", description = """
            使用文档编辑数据定义文件的链接，用于跟踪和显示文档更改历史记录。
            仅当 status 等于 2, 3, 6 或 7 时，链接才存在。
            必须保存文件，并且必须使用 setHistoryData 方法将其地址作为 changesUrl 参数发送，以显示与特定文档版本对应的更改。
            """)
    @JsonAlias({"changesurl"})
    private String changesUrl;

    @Schema(name = "history", description = "历史记录")
    private History history;

    @Schema(name = "fileType", description = "文件类型")
    @JsonAlias({"filetype"})
    private String fileType;

    @Schema(name = "key", description = "文档key")
    private String key;

    @Schema(name = "status", description = """
            状态1: 每次用户连接或断开文档共同编辑时都会收到它。他们的 callbackUrl 被使用。
            状态2: 它在编辑文档关闭后 10 秒收到，该用户的标识符是最后一个将更改发送到文档编辑服务的用户。对文件进行最后更改的用户的 callbackUrl 被使用。
            状态4: 它是在最后一个用户关闭所编辑的没有更改的文档情况下收到的。他们的 callbackUrl 被使用。
            状态6: 在执行强制保存请求时接收到。callbackUrl 依赖于 forcesavetype 参数
            """)
    private Integer status;

    @Schema(name = "url", description = """
            定义已编辑的要由文档存储服务保存的文档的链接。仅当 status 值等于 2, 3, 6 或 7 时，链接才存在。
            """)
    private String url;

    @Schema(name = "users", description = """
            定义打开文档进行编辑的用户的标识符列表；当文档被更改时，用户将返回最后编辑文档的用户的标识符（对于 status 2 和 status 6 的应答）。
            """)
    private Set<String> users;


    /**
     * 用户操作
     */
    @Data
    @Schema(name = "Action", description = "用户操作", hidden = true)
    public static class Action {
        @Schema(name = "type", description = """
                0 - 用户断开与文档共同编辑的连接，
                1 - 新用户连接到文档共同编辑，
                2 - 用户单击强制保存按钮。
                """)
        private Integer type;

        @Schema(name = "userId", description = "用户ID")
        private String userId;
    }


    @Data
    @Schema(name = "history", description = "历史记录", hidden = true)
    public static class History {

        @Schema(name = "changes", description = "更改历史")
        private List<Change> changes;

        @Schema(name = "serverVersion", description = "服务器版本")
        private String serverVersion;
    }

    @Data
    @Schema(name = "change", description = "表示 OnlyOffice 回调中 history.changes 里的单个变更记录", hidden = true)
    public static class Change {

        @Schema(name = "documentSha256", description = "文档的 SHA256 哈希值")
        private String documentSha256;

        @Schema(name = "created", description = "创建时间")
        private String created;

        @Schema(name = "user", description = "用户信息")
        private ChangeUser user;

        @Data
        @Schema(name = "ChangeUser", description = "用户信息", hidden = true)
        public static class ChangeUser {

            @Schema(name = "id", description = "用户ID")
            private String id;

            @Schema(name = "name", description = "用户名")
            private String name;
        }
    }
}
