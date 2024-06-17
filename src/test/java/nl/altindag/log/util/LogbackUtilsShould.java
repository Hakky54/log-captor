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
import nl.altindag.log.exception.LogCaptorException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.SubstituteLogger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

class LogbackUtilsShould {

    @Test
    void getLoggerEvenWhenItHasInitializedLogger() {
        SubstituteLogger substituteLogger = mock(SubstituteLogger.class);
        Logger logbackLogger = mock(Logger.class);

        try (MockedStatic<LoggerFactory> mockedStatic = mockStatic(LoggerFactory.class)) {
            mockedStatic.when(() -> LoggerFactory.getLogger("magic-logger"))
                    .thenReturn(substituteLogger)
                    .thenReturn(logbackLogger);

            Logger logger = LogbackUtils.getLogger("magic-logger");
            assertThat(logger).isEqualTo(logbackLogger);
        }
    }

    @Test
    void failToGetLoggerWhenTheUnderlyingLoggerIsNotInitializedYet() {
        SubstituteLogger substituteLogger = mock(SubstituteLogger.class);
        System.setProperty("logcaptor.poll-counter-limit", "1");

        try (MockedStatic<LoggerFactory> mockedStatic = mockStatic(LoggerFactory.class)) {
            mockedStatic.when(() -> LoggerFactory.getLogger("magic-logger"))
                    .thenReturn(substituteLogger);

            assertThatThrownBy(() -> LogbackUtils.getLogger("magic-logger"))
                    .isInstanceOf(LogCaptorException.class)
                    .hasMessage("SLF4J Logger implementation should be of the type [ch.qos.logback.classic.Logger] but found [org.slf4j.helpers.SubstituteLogger].");
        } finally {
            System.clearProperty("logcaptor.poll-counter-limit");
        }
    }

    @Test
    void failToGetLoggerWhenTheUnderlyingLoggerIsNotInitializedAndExceedsRetryMechanism() {
        System.setProperty("logcaptor.poll-counter-limit", "5");
        System.setProperty("logcaptor.poll-delay-milliseconds", "10");

        SubstituteLogger substituteLogger = mock(SubstituteLogger.class);

        try (MockedStatic<LoggerFactory> mockedStatic = mockStatic(LoggerFactory.class)) {
            mockedStatic.when(() -> LoggerFactory.getLogger("magic-logger"))
                    .thenReturn(substituteLogger);

            assertThatThrownBy(() -> LogbackUtils.getLogger("magic-logger"))
                    .isInstanceOf(LogCaptorException.class)
                    .hasMessage("SLF4J Logger implementation should be of the type [ch.qos.logback.classic.Logger] but found [org.slf4j.helpers.SubstituteLogger].");

            mockedStatic.verify(() -> LoggerFactory.getLogger("magic-logger"), times(6));
        } finally {
            System.clearProperty("logcaptor.poll-counter-limit");
            System.clearProperty("logcaptor.poll-delay-milliseconds");
        }
    }

}
