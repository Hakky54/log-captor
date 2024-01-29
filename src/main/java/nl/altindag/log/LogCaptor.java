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

package nl.altindag.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.filter.Filter;
import nl.altindag.log.appender.InMemoryAppender;
import nl.altindag.log.model.LogEvent;
import nl.altindag.log.util.JavaUtilLoggingLoggerUtils;
import nl.altindag.log.util.Mappers;
import nl.altindag.log.util.ValidationUtils;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

/**
 * @author Hakan Altindag
 */
public final class LogCaptor implements AutoCloseable {

    private static final Map<String, Level> LOG_LEVEL_CONTAINER = new HashMap<>();
    private static final String CONSOLE_APPENDER_NAME = "console";

    private final Logger logger;
    private final Appender<ILoggingEvent> appender;
    private final List<ILoggingEvent> eventsCollector = Collections.synchronizedList(new ArrayList<>());

    private LogCaptor(String loggerName) {
        org.slf4j.Logger slf4jLogger = LoggerFactory.getLogger(loggerName);
        ValidationUtils.requireLoggerOfType(slf4jLogger, ch.qos.logback.classic.Logger.class);

        logger = (Logger) slf4jLogger;
        appender = new InMemoryAppender<>("log-captor", eventsCollector);
        appender.start();
        logger.addAppender(appender);

        JavaUtilLoggingLoggerUtils.redirectToSlf4j(loggerName);
        LOG_LEVEL_CONTAINER.putIfAbsent(logger.getName(), logger.getEffectiveLevel());
    }

    /**
     * Captures all log messages
     *
     * @return LogCaptor instance for the root logger
     */
    public static LogCaptor forRoot() {
        return new LogCaptor(ROOT_LOGGER_NAME);
    }

    /**
     * Captures log messages for the provided class
     *
     * @param clazz Class for capturing
     * @return LogCaptor instance for the provided class
     */
    public static LogCaptor forClass(Class<?> clazz) {
        return new LogCaptor(clazz.getName());
    }

    /**
     * Captures log messages for the provided logger name
     *
     * @param name Logger name for capturing
     * @return LogCaptor instance for the provided logger name
     */
    public static LogCaptor forName(String name) {
        return new LogCaptor(name);
    }

    public List<String> getLogs() {
        return getLogs(logEvent -> true, ILoggingEvent::getFormattedMessage);
    }

    public List<String> getInfoLogs() {
        return getLogs(Level.INFO);
    }

    public List<String> getDebugLogs() {
        return getLogs(Level.DEBUG);
    }

    public List<String> getWarnLogs() {
        return getLogs(Level.WARN);
    }

    public List<String> getErrorLogs() {
        return getLogs(Level.ERROR);
    }

    public List<String> getTraceLogs() {
        return getLogs(Level.TRACE);
    }

    private List<String> getLogs(Level level) {
        return getLogs(logEvent -> logEvent.getLevel() == level, ILoggingEvent::getFormattedMessage);
    }

    public List<LogEvent> getLogEvents() {
        return getLogs(logEvent -> true, Mappers.toLogEvent());
    }

    private <T> List<T> getLogs(Predicate<ILoggingEvent> logEventPredicate, Function<ILoggingEvent, T> logEventMapper) {
        synchronized (eventsCollector) {
            return eventsCollector.stream()
                    .filter(logEventPredicate)
                    .map(logEventMapper)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
        }
    }

    public void addFilter(Filter<ILoggingEvent> filter) {
        appender.addFilter(filter);
        filter.start();
    }

    /**
     * Overrides the log level property of the target logger. This may result that the overridden property
     * of the target logger is still active even though a new instance of {@link LogCaptor} has been created.
     * To roll-back to the initial state use: {@link LogCaptor#resetLogLevel()}
     *
     * This option will implicitly include the following log levels: WARN and ERROR
     */
    public void setLogLevelToInfo() {
        logger.setLevel(Level.INFO);
    }

    /**
     * Overrides the log level property of the target logger. This may result that the overridden property
     * of the target logger is still active even though a new instance of {@link LogCaptor} has been created.
     * To roll-back to the initial state use: {@link LogCaptor#resetLogLevel()}
     *
     * This option will implicitly include the following log levels: INFO, WARN and ERROR
     */
    public void setLogLevelToDebug() {
        logger.setLevel(Level.DEBUG);
    }

    /**
     * Overrides the log level property of the target logger. This may result that the overridden property
     * of the target logger is still active even though a new instance of {@link LogCaptor} has been created.
     * To roll-back to the initial state use: {@link LogCaptor#resetLogLevel()}
     *
     * This option will implicitly include the following log levels: INFO, DEBUG, WARN and ERROR
     */
    public void setLogLevelToTrace() {
        logger.setLevel(Level.TRACE);
    }

    /**
     * Overrides the log level property of the target logger. This may result that the overridden property
     * of the target logger is still active even though a new instance of {@link LogCaptor} has been created.
     * To roll-back to the initial state use: {@link LogCaptor#resetLogLevel()}
     */
    public void disableLogs() {
        logger.setLevel(Level.OFF);
    }

    /**
     * Disables the output of the log entries to the console. To revert this option use {@link LogCaptor#enableConsoleOutput()}.
     * LogCaptor will still be capturing the log entries.
     */
    public void disableConsoleOutput() {
        getConsoleAppender().stop();
    }

    /**
     * The output of the log entries to the console are enabled by default but can be re-enabled if
     * they are disabled earlier by {@link LogCaptor#disableConsoleOutput()}
     */
    public void enableConsoleOutput() {
        getConsoleAppender().start();
    }

    private Appender<ILoggingEvent> getConsoleAppender() {
        return logger.getLoggerContext()
                .getLogger(ROOT_LOGGER_NAME)
                .getAppender(CONSOLE_APPENDER_NAME);
    }

    /**
     * Resets the log level of the target logger to the initial value which was available before
     * changing it with {@link LogCaptor#setLogLevelToInfo()}, {@link LogCaptor#setLogLevelToDebug()} or with {@link LogCaptor#setLogLevelToTrace()}
     */
    public void resetLogLevel() {
        Optional.ofNullable(LOG_LEVEL_CONTAINER.get(logger.getName()))
                .ifPresent(logger::setLevel);
    }

    public void clearLogs() {
        eventsCollector.clear();
    }

    @Override
    public void close() {
        logger.detachAppender(appender);
        appender.stop();
    }

}
