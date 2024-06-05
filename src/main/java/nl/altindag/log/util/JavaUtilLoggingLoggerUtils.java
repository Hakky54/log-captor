/*
 * Copyright 2019 Thunderberry.
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
package nl.altindag.log.util;

import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.Logger;

/**
 * @author Hakan Altindag
 */
public final class JavaUtilLoggingLoggerUtils {

    private static final String JAVA_UTIL_LOGGING_ROOT_LOGGER_NAME = "";
    private static final String SLF4J_ROOT_LOGGER_NAME = org.slf4j.Logger.ROOT_LOGGER_NAME;

    private JavaUtilLoggingLoggerUtils() {}

    public static void redirectToSlf4j(String loggerName) {
        if (!SLF4JBridgeHandler.isInstalled()) {
            SLF4JBridgeHandler.removeHandlersForRootLogger();
            SLF4JBridgeHandler.install();
        }

        String loggerNameForJul = SLF4J_ROOT_LOGGER_NAME.equals(loggerName) ? JAVA_UTIL_LOGGING_ROOT_LOGGER_NAME : loggerName;
        Logger.getLogger(loggerNameForJul).setLevel(java.util.logging.Level.ALL);
    }

}
