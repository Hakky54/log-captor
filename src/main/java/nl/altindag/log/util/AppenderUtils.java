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

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.Encoder;
import nl.altindag.log.appender.InMemoryAppender;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

/**
 * @author Hakan Altindag
 */
public final class AppenderUtils {

    public static final String CONSOLE_APPENDER_NAME = "console";
    public static final String IN_MEMORY_APPENDER_NAME = "logcaptor-in-memory-appender";
    private static final String DEFAULT_LOG_PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSSXXX} %-5level [%thread] %logger{36} - %msg%n";
    private static final List<String> CONSOLE_APPENDER_NAMES = Arrays.asList("console", "CONSOLE");

    private AppenderUtils() {}

    public static ConsoleAppender<ILoggingEvent> createConsoleAppender(LoggerContext loggerContext) {
        Encoder<ILoggingEvent> encoder = createEncoder(loggerContext);
        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(loggerContext);
        consoleAppender.setName(CONSOLE_APPENDER_NAME);
        consoleAppender.setImmediateFlush(true);
        consoleAppender.setEncoder(encoder);
        consoleAppender.start();
        return consoleAppender;
    }

    public static Encoder<ILoggingEvent> createEncoder(LoggerContext loggerContext) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern(DEFAULT_LOG_PATTERN);
        encoder.start();
        return encoder;
    }

    public static Optional<ConsoleAppender<ILoggingEvent>> getConsoleAppender(Logger logger) {
        return CONSOLE_APPENDER_NAMES.stream()
                .map(logger::getAppender)
                .filter(Objects::nonNull)
                .filter(ConsoleAppender.class::isInstance)
                .map(consoleAppender -> (ConsoleAppender<ILoggingEvent>) consoleAppender)
                .findFirst();
    }

    public static InMemoryAppender<ILoggingEvent> configureInMemoryAppender(Logger logger, List<ILoggingEvent> eventsCollector) {
        InMemoryAppender<ILoggingEvent> inMemoryAppender = new InMemoryAppender<>(AppenderUtils.IN_MEMORY_APPENDER_NAME, eventsCollector);
        inMemoryAppender.setContext(logger.getLoggerContext());
        inMemoryAppender.start();
        logger.addAppender(inMemoryAppender);
        return inMemoryAppender;
    }

    public static ConsoleAppender<ILoggingEvent> configureConsoleAppender(Logger logger) {
        return configureConsoleAppender(logger, null);
    }

    public static ConsoleAppender<ILoggingEvent> configureConsoleAppender(Logger logger, ConsoleAppender<ILoggingEvent> appender) {
        String loggerName = logger.getName();
        if (!ROOT_LOGGER_NAME.equals(loggerName)) {
            logger.setAdditive(false);
        }

        ConsoleAppender<ILoggingEvent> consoleAppender = createConsoleAppender(logger, appender);
        if (!ROOT_LOGGER_NAME.equals(loggerName)) {
            boolean containsRootConsoleAppender = false;
            Iterator<Appender<ILoggingEvent>> rootAppenders = getRootLogger(logger).iteratorForAppenders();
            while (rootAppenders.hasNext()) {
                if (rootAppenders.next() instanceof ConsoleAppender) {
                    containsRootConsoleAppender = true;
                }
            }

            if (containsRootConsoleAppender) {
                logger.addAppender(consoleAppender);
            }
        }
        return consoleAppender;
    }

    private static ConsoleAppender<ILoggingEvent> createConsoleAppender(Logger logger, ConsoleAppender<ILoggingEvent> consoleAppender) {
        return Optional.ofNullable(consoleAppender)
                .orElseGet(() -> AppenderUtils.getConsoleAppender(getRootLogger(logger))
                .orElseGet(() -> AppenderUtils.getConsoleAppender(logger)
                .orElseGet(() -> AppenderUtils.createConsoleAppender(logger.getLoggerContext()))));
    }

    private static Logger getRootLogger(Logger logger) {
        return logger.getLoggerContext().getLogger(ROOT_LOGGER_NAME);
    }

}
