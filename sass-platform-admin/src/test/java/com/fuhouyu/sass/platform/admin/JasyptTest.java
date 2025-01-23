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
package com.fuhouyu.sass.platform.admin;

import com.ulisesbocchio.jasyptspringbootstarter.JasyptSpringBootAutoConfiguration;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>
 *
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/24 20:33
 */
@SpringBootTest(classes = JasyptSpringBootAutoConfiguration.class)
@Disabled
class JasyptTest {

    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    void testEncrypt() {
        String originDataStrings = System.getenv("encrypt.strings");
        Assertions.assertNotNull(originDataStrings);
        for (String originData : originDataStrings.split(",")) {
            System.out.printf("origin data: %s, encrypt data: %s%n", originData, stringEncryptor.encrypt(originData));
        }
    }
}
