package nl.altindag.log;

import nl.altindag.log.service.LogMessage;
import nl.altindag.log.service.Service;
import nl.altindag.log.service.apache.FooService;
import nl.altindag.log.service.exception.ZooService;
import nl.altindag.log.service.lombok.BooService;
import nl.altindag.log.service.lombok.QooService;
import nl.altindag.log.service.lombok.RooService;
import nl.altindag.log.service.lombok.WooService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class LogCaptorShould {

    private LogCaptor logCaptor;

    @AfterEach
    void resetProperties() {
        Optional.ofNullable(logCaptor)
                .ifPresent(LogCaptor::resetLogLevel);
    }

    @Test
    void captureLoggingEventsWhereApacheLogManagerIsUsed() {
        logCaptor = LogCaptor.forClass(FooService.class);
        logCaptor.setLogLevelToTrace();

        Service service = new FooService();
        service.sayHello();

        assertThat(logCaptor.getInfoLogs()).containsExactly(LogMessage.INFO.getMessage());
        assertThat(logCaptor.getDebugLogs()).containsExactly(LogMessage.DEBUG.getMessage());
        assertThat(logCaptor.getWarnLogs()).containsExactly(LogMessage.WARN.getMessage());
        assertThat(logCaptor.getErrorLogs()).containsExactly(LogMessage.ERROR.getMessage());
        assertThat(logCaptor.getTraceLogs()).containsExactly(LogMessage.TRACE.getMessage());

        assertThat(logCaptor.getLogs())
                .hasSize(5)
                .containsExactly(
                        LogMessage.INFO.getMessage(),
                        LogMessage.WARN.getMessage(),
                        LogMessage.ERROR.getMessage(),
                        LogMessage.TRACE.getMessage(),
                        LogMessage.DEBUG.getMessage()
                );
    }

    @Test
    void captureLoggingEventsWithoutSpecifyingClass() {
        logCaptor = LogCaptor.forRoot();
        logCaptor.setLogLevelToTrace();

        Service service = new FooService();
        service.sayHello();

        assertThat(logCaptor.getInfoLogs()).containsExactly(LogMessage.INFO.getMessage());
        assertThat(logCaptor.getDebugLogs()).containsExactly(LogMessage.DEBUG.getMessage());
        assertThat(logCaptor.getWarnLogs()).containsExactly(LogMessage.WARN.getMessage());
        assertThat(logCaptor.getErrorLogs()).containsExactly(LogMessage.ERROR.getMessage());
        assertThat(logCaptor.getTraceLogs()).containsExactly(LogMessage.TRACE.getMessage());
    }

    @Test
    void captureLoggingEventsContainingException() {
        logCaptor = LogCaptor.forClass(ZooService.class);

        Service service = new ZooService();
        service.sayHello();

        assertThat(logCaptor.getErrorLogs())
                .containsExactly("Caught unexpected exception\n" +
                        "java.io.IOException: KABOOM!");
    }

    @Test
    void captureLoggingEventsByUsingForNameMethodWithLogCaptor() {
        logCaptor = LogCaptor.forName("nl.altindag.log.service.apache.FooService");
        logCaptor.setLogLevelToTrace();

        Service service = new FooService();
        service.sayHello();

        assertThat(logCaptor.getInfoLogs()).containsExactly(LogMessage.INFO.getMessage());
        assertThat(logCaptor.getDebugLogs()).containsExactly(LogMessage.DEBUG.getMessage());
        assertThat(logCaptor.getWarnLogs()).containsExactly(LogMessage.WARN.getMessage());
        assertThat(logCaptor.getErrorLogs()).containsExactly(LogMessage.ERROR.getMessage());
        assertThat(logCaptor.getTraceLogs()).containsExactly(LogMessage.TRACE.getMessage());

        assertThat(logCaptor.getLogs())
                .hasSize(5)
                .containsExactly(
                        LogMessage.INFO.getMessage(),
                        LogMessage.WARN.getMessage(),
                        LogMessage.ERROR.getMessage(),
                        LogMessage.TRACE.getMessage(),
                        LogMessage.DEBUG.getMessage()
                );
    }

    @Test
    void captureLoggingEventsWithDebugEnabled() {
        logCaptor = LogCaptor.forClass(FooService.class);
        logCaptor.setLogLevelToInfo();

        Service service = new FooService();
        service.sayHello();

        assertThat(logCaptor.getLogs())
                .hasSize(3)
                .containsExactly(
                        LogMessage.INFO.getMessage(),
                        LogMessage.WARN.getMessage(),
                        LogMessage.ERROR.getMessage()
                );

        logCaptor.clearLogs();
        logCaptor.setLogLevelToDebug();

        service.sayHello();

        assertThat(logCaptor.getLogs())
                .hasSize(4)
                .containsExactly(
                        LogMessage.INFO.getMessage(),
                        LogMessage.WARN.getMessage(),
                        LogMessage.ERROR.getMessage(),
                        LogMessage.DEBUG.getMessage()
                );
    }

    @Test
    void captureLoggingEventsWhereLombokLog4j2IsUsed() {
        logCaptor = LogCaptor.forClass(BooService.class);

        Service service = new BooService();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN, LogMessage.DEBUG);
    }

    @Test
    void captureLoggingEventsWithLogLevelInfoWhereLombokLog4j2IsUsed() {
        logCaptor = LogCaptor.forClass(BooService.class);
        logCaptor.setLogLevelToInfo();

        Service service = new BooService();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN);
    }

    @Test
    void captureLoggingEventsWhereLombokSlf4jIsUsed() {
        logCaptor = LogCaptor.forClass(QooService.class);

        Service service = new QooService();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN, LogMessage.DEBUG);
    }

    @Test
    void captureLoggingEventsWithLogLevelInfoWhereLombokSlf4jIsUsed() {
        logCaptor = LogCaptor.forClass(QooService.class);
        logCaptor.setLogLevelToInfo();

        Service service = new QooService();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN);
    }

    @Test
    void captureLoggingEventsWhereLombokLog4jIsUsed() {
        logCaptor = LogCaptor.forClass(WooService.class);

        Service service = new WooService();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN, LogMessage.DEBUG);
    }

    @Test
    void captureLoggingEventsWithLogLevelInfoWhereLombokLog4jIsUsed() {
        logCaptor = LogCaptor.forClass(WooService.class);
        logCaptor.setLogLevelToInfo();

        Service service = new WooService();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN);
    }

    @Test
    void captureLoggingEventsWhereLombokJavaUtilLoggingIsUsed() {
        logCaptor = LogCaptor.forClass(RooService.class);

        Service service = new RooService();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN);
    }

    private static void assertLogMessages(LogCaptor logCaptor, LogMessage... logMessages) {
        for (LogMessage logMessage : logMessages) {
            switch (logMessage) {
                case INFO:
                    assertThat(logCaptor.getInfoLogs()).containsExactly(logMessage.getMessage());
                    break;
                case DEBUG:
                    assertThat(logCaptor.getDebugLogs()).containsExactly(logMessage.getMessage());
                    break;
                case WARN:
                    assertThat(logCaptor.getWarnLogs()).containsExactly(logMessage.getMessage());
                    break;
                case ERROR:
                    assertThat(logCaptor.getErrorLogs()).containsExactly(logMessage.getMessage());
                    break;
                case TRACE:
                    assertThat(logCaptor.getTraceLogs()).containsExactly(logMessage.getMessage());
                    break;
                default:
                    throw new IllegalArgumentException(logMessage.getLogLevel() + " level is not supported yet");
            }
        }

        String[] expectedLogMessages = Arrays.stream(logMessages)
                .map(LogMessage::getMessage)
                .toArray(String[]::new);

        assertThat(logCaptor.getLogs())
                .hasSize(expectedLogMessages.length)
                .containsExactly(expectedLogMessages);
    }

    @Nested
    class ClearLogsShould {

        private final LogCaptor logCaptor = LogCaptor.forClass(FooService.class);

        @AfterEach
        void clearLogs() {
            logCaptor.clearLogs();
        }

        @Test
        void captureLogging() {
            Service service = new FooService();
            service.sayHello();

            assertThat(logCaptor.getInfoLogs()).containsExactly(LogMessage.INFO.getMessage());
            assertThat(logCaptor.getDebugLogs()).containsExactly(LogMessage.DEBUG.getMessage());
            assertThat(logCaptor.getErrorLogs()).containsExactly(LogMessage.ERROR.getMessage());
            assertThat(logCaptor.getWarnLogs()).containsExactly(LogMessage.WARN.getMessage());

            assertThat(logCaptor.getLogs())
                    .hasSize(4)
                    .containsExactly(
                            LogMessage.INFO.getMessage(),
                            LogMessage.WARN.getMessage(),
                            LogMessage.ERROR.getMessage(),
                            LogMessage.DEBUG.getMessage()
                    );
        }

        @Test
        void captureLoggingWithTheSameLogCaptureInstance() {
            Service service = new FooService();
            service.sayHello();

            assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN, LogMessage.ERROR, LogMessage.DEBUG);
        }

    }

}
