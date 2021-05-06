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

package nl.altindag.log.service.jdk;

import nl.altindag.log.service.LogMessage;
import nl.altindag.log.service.Service;
import java.util.logging.Logger;

/**
 * @author Hakan Altindag
 */
public class ServiceWithJavaUtilLogging implements Service {

    private static final Logger LOGGER = Logger.getLogger(ServiceWithJavaUtilLogging.class.getName());

    @Override
    public void sayHello() {
        LOGGER.info(LogMessage.INFO.getMessage());
        LOGGER.severe(LogMessage.ERROR.getMessage());
        LOGGER.fine(LogMessage.DEBUG.getMessage());
        LOGGER.finest(LogMessage.TRACE.getMessage());
    }

}
