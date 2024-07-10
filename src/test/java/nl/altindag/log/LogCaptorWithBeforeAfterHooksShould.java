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
package nl.altindag.log;

import nl.altindag.log.service.LogMessage;
import nl.altindag.log.service.Service;
import nl.altindag.log.service.apache.ServiceWithApacheLog4j;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hakan Altindag
 */
class LogCaptorWithBeforeAfterHooksShould {

    private static LogCaptor logCaptor;

    @BeforeAll
    public static void setupLogCaptor() {
        logCaptor = LogCaptor.forClass(ServiceWithApacheLog4j.class);
    }

    @BeforeEach
    public void enableDetailedLogging() {
        logCaptor.setLogLevelToTrace();
    }

    @AfterEach
    public void reset() {
        logCaptor.clearLogs();
        logCaptor.resetLogLevel();
    }

    @AfterAll
    public static void tearDown() {
        logCaptor.close();
    }

    @Test
    public void containsCapturedLogs() {
        Service service = new ServiceWithApacheLog4j();
        service.sayHello();

        assertThat(logCaptor.getLogs()).hasSize(5);
    }

    @Test
    public void containsExpectedMessages() {
        Service service = new ServiceWithApacheLog4j();
        service.sayHello();

        assertThat(logCaptor.getInfoLogs()).containsExactly(LogMessage.INFO.getMessage());
        assertThat(logCaptor.getDebugLogs()).containsExactly(LogMessage.DEBUG.getMessage());
        assertThat(logCaptor.getWarnLogs()).containsExactly(LogMessage.WARN.getMessage());
        assertThat(logCaptor.getErrorLogs()).containsExactly(LogMessage.ERROR.getMessage());
        assertThat(logCaptor.getTraceLogs()).containsExactly(LogMessage.TRACE.getMessage());
    }

}
