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
package nl.altindag.log.mapper;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import nl.altindag.log.model.LogEvent;
import nl.altindag.log.model.LogMarker;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <strong>NOTE:</strong>
 * Please don't use this class directly as it is part of the internal API. Class name and methods can be changed any time.
 *
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
        List<Map.Entry<String, Object>> keyValuePairs = iLoggingEvent.getKeyValuePairs() == null ? Collections.emptyList() : iLoggingEvent.getKeyValuePairs().stream()
                .map(keyValuePair -> new SimpleImmutableEntry<>(keyValuePair.key, keyValuePair.value))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

        List<Object> arguments = Optional.ofNullable(iLoggingEvent.getArgumentArray())
                .map(Arrays::asList)
                .map(Collections::unmodifiableList)
                .orElseGet(Collections::emptyList);

        Throwable throwable = Optional.ofNullable(iLoggingEvent.getThrowableProxy())
                .filter(ThrowableProxy.class::isInstance)
                .map(ThrowableProxy.class::cast)
                .map(ThrowableProxy::getThrowable)
                .orElse(null);

        List<LogMarker> logMarkers = Collections.emptyList();
        if (iLoggingEvent.getMarkerList() != null) {
            logMarkers = iLoggingEvent.getMarkerList().stream()
                    .map(LogMarkerMapper.getInstance())
                    .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
        }

        return new LogEvent(
                message,
                formattedMessage,
                level,
                loggerName,
                threadName,
                timeStamp,
                arguments,
                throwable,
                diagnosticContext,
                keyValuePairs,
                logMarkers
        );
    }

    public static LogEventMapper getInstance() {
        return INSTANCE;
    }

}
