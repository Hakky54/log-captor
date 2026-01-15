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
package nl.altindag.log.appender;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class NOPAppenderShould {

    @Test
    void appendDoesNothing() {
        Logger logger = (Logger) LoggerFactory.getLogger(NOPAppenderShould.class);
        NOPAppender<ILoggingEvent> nopAppender = new NOPAppender<>(logger.getLoggerContext());

        ILoggingEvent loggingEvent = mock(ILoggingEvent.class);
        nopAppender.append(loggingEvent);
        verify(loggingEvent, never()).getMessage();
    }

}
