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

import nl.altindag.log.exception.LogCaptorException;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.SubstituteLogger;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * <strong>NOTE:</strong>
 * Please don't use this class directly as it is part of the internal API. Class name and methods can be changed any time.
 *
 * @author Hakan Altindag
 */
public final class LogbackUtils {

    private static final int DEFAULT_POLL_COUNTER_LIMIT = 10;
    private static final int DEFAULT_POLL_DELAY_MILLISECONDS = 100;
    public static final String IS_NUMBER_REGEX = ".*\\d.*";

    private static final Supplier<Integer> POLL_COUNTER_LIMIT = () -> Optional.ofNullable(System.getProperty("logcaptor.poll-counter-limit"))
            .map(String::trim)
            .filter(value -> !value.isEmpty())
            .filter(value -> value.matches(IS_NUMBER_REGEX))
            .map(Integer::parseInt)
            .orElse(DEFAULT_POLL_COUNTER_LIMIT);
    private static final Supplier<Integer> POLL_DELAY_MILLISECONDS = () -> Optional.ofNullable(System.getProperty("logcaptor.poll-delay-milliseconds"))
            .map(String::trim)
            .filter(value -> !value.isEmpty())
            .filter(value -> value.matches(IS_NUMBER_REGEX))
            .map(Integer::parseInt)
            .orElse(DEFAULT_POLL_DELAY_MILLISECONDS);

    private LogbackUtils() {}

    public static ch.qos.logback.classic.Logger getLogger(String loggerName) {
        org.slf4j.Logger slf4jLogger = getSlf4jLogger(loggerName);
        ValidationUtils.requireLoggerOfType(slf4jLogger, ch.qos.logback.classic.Logger.class);
        return (ch.qos.logback.classic.Logger) slf4jLogger;
    }

    /**
     * Attempts to get the {@link org.slf4j.Logger}.
     * It might occur the {@link LoggerFactory} returns a {@link SubstituteLogger}. In that
     * case it will retry to get the correct logger within the given poll limit and poll delay.
     * SLF4J will provide the {@link SubstituteLogger} temporally when the underlying logger is not ready yet.
     * This will most likely happen when running the tests in parallel.
     */
    private static org.slf4j.Logger getSlf4jLogger(String loggerName) {
        int retryCounter = 0;
        org.slf4j.Logger slf4jLogger = LoggerFactory.getLogger(loggerName);

        int pollCounterLimit = POLL_COUNTER_LIMIT.get();
        int pollDelayMilliseconds = POLL_DELAY_MILLISECONDS.get();

        while (slf4jLogger instanceof SubstituteLogger && retryCounter++ < pollCounterLimit) {
            try {
                TimeUnit.MILLISECONDS.sleep(pollDelayMilliseconds);
                slf4jLogger = LoggerFactory.getLogger(loggerName);;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new LogCaptorException(e);
            }
        }

        return slf4jLogger;
    }

}
