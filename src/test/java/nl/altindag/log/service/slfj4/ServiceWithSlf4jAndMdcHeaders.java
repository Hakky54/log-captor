/*
 * Copyright 2019-2020 the original author or authors.
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

package nl.altindag.log.service.slfj4;

import nl.altindag.log.service.LogMessage;
import nl.altindag.log.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * @author Vasiliy Sobolev
 */
public class ServiceWithSlf4jAndMdcHeaders implements Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceWithSlf4jAndMdcHeaders.class);

    @Override
    public void sayHello() {
        try {
            MDC.put("test-slf4j-mdc", "hello-slf4j");
            LOGGER.info(LogMessage.INFO.getMessage());
        } finally {
            MDC.clear();
        }

        LOGGER.info(LogMessage.INFO.getMessage());
    }

}
