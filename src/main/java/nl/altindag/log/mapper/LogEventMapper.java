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

package nl.altindag.log.mapper;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import nl.altindag.log.model.LogEvent;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Hakan Altindag
 */
public final class LogEventMapper implements Function<ILoggingEvent, LogEvent> {

    private static final LogEventMapper INSTANCE = new LogEventMapper();

    private LogEventMapper() {}

    @Override
    public LogEvent apply(ILoggingEvent iLoggingEvent) {
        String message = iLoggingEvent.getMessage();
        String formattedMessage = iLoggingEvent.getFormattedMessage();
        String level = iLoggingEvent.getLevel().toString();
        String loggerName = iLoggingEvent.getLoggerName();
        String threadName = iLoggingEvent.getThreadName();
        ZonedDateTime timeStamp = ZonedDateTime.ofInstant(Instant.ofEpochMilli(iLoggingEvent.getTimeStamp()), ZoneOffset.UTC);
        Map<String, String> diagnosticContext = Collections.unmodifiableMap(iLoggingEvent.getMDCPropertyMap());

        List<Object> arguments = Optional.ofNullable(iLoggingEvent.getArgumentArray())
                .map(Arrays::asList)
                .map(Collections::unmodifiableList)
                .orElseGet(Collections::emptyList);

        Throwable throwable = Optional.ofNullable(iLoggingEvent.getThrowableProxy())
                .filter(ThrowableProxy.class::isInstance)
                .map(ThrowableProxy.class::cast)
                .map(ThrowableProxy::getThrowable)
                .orElse(null);

        return new LogEvent(
                message,
                formattedMessage,
                level,
                loggerName,
                threadName,
                timeStamp,
                arguments,
                throwable,
                diagnosticContext
        );
    }

    public static LogEventMapper getInstance() {
        return INSTANCE;
    }

}
