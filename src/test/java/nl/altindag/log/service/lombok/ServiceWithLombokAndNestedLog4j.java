/*
 * Copyright 2019-2021 the original author or authors.
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

package nl.altindag.log.service.lombok;

import lombok.extern.log4j.Log4j;
import nl.altindag.log.service.LogMessage;
import nl.altindag.log.service.Service;

/**
 * @author Hakan Altindag
 */
public class ServiceWithLombokAndNestedLog4j {

    @Log4j
    public static class NestedService implements Service {

        @Override
        public void sayHello() {
            log.info(LogMessage.INFO.getMessage());
        }
    }

}
