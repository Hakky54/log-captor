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

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.helpers.NOPAppender;

public final class AppenderUtils {

    public static final String NOP_APPENDER_NAME = "logcaptor-nop-appender";
    public static final String CONSOLE_APPENDER_NAME = "console";
    private static final String DEFAULT_LOG_PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSSXXX} %-5level [%thread] %logger{36} - %msg%n";

    private AppenderUtils() {}

    public static ConsoleAppender<ILoggingEvent> createConsoleAppender(LoggerContext loggerContext) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern(DEFAULT_LOG_PATTERN);
        encoder.start();

        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(loggerContext);
        consoleAppender.setName(CONSOLE_APPENDER_NAME);
        consoleAppender.setImmediateFlush(true);
        consoleAppender.setEncoder(encoder);
        consoleAppender.start();
        return consoleAppender;
    }

    public static NOPAppender<ILoggingEvent> createNopAppender(Context context) {
        NOPAppender<ILoggingEvent> nopAppender = new NOPAppender<>();
        nopAppender.setName(NOP_APPENDER_NAME);
        nopAppender.setContext(context);
        nopAppender.start();
        return nopAppender;
    }

}
