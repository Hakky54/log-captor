package nl.altindag.log;

import ch.qos.logback.classic.Level;
import nl.altindag.log.service.LogMessage;
import nl.altindag.log.service.Service;
import nl.altindag.log.service.apache.FooService;
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

public class LogCaptorShould {

    private LogCaptor<?> logCaptor;

    @AfterEach
    public void resetProperties() {
        Optional.ofNullable(logCaptor)
                .ifPresent(LogCaptor::resetLogLevel);
    }

    @Test
    public void captureLoggingEventsWithLogLevelEnum() {
        logCaptor = LogCaptor.forClass(FooService.class);

        Service service = new FooService();
        service.sayHello();

        assertThat(logCaptor.getLogs(Level.INFO)).containsExactly(LogMessage.INFO.getMessage());
        assertThat(logCaptor.getLogs(Level.DEBUG)).containsExactly(LogMessage.DEBUG.getMessage());
        assertThat(logCaptor.getLogs(Level.WARN)).containsExactly(LogMessage.WARN.getMessage());

        assertThat(logCaptor.getLogs())
                .hasSize(3)
                .containsExactly(LogMessage.INFO.getMessage(), LogMessage.WARN.getMessage(), LogMessage.DEBUG.getMessage());
    }

    @Test
    public void captureLoggingEventsWhereApacheLogManagerIsUsed() {
        logCaptor = LogCaptor.forClass(FooService.class);

        Service service = new FooService();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN, LogMessage.DEBUG);
    }

    @Test
    public void captureLoggingEventsWithLogLevelInfoWhereApacheLogManagerIsUsed() {
        logCaptor = LogCaptor.forClass(FooService.class);
        logCaptor.setLogLevel(Level.INFO);

        Service service = new FooService();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN);
    }

    @Test
    public void captureLoggingEventsWhereLombokLog4j2IsUsed() {
        logCaptor = LogCaptor.forClass(BooService.class);

        Service service = new BooService();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN, LogMessage.DEBUG);
    }

    @Test
    public void captureLoggingEventsWithLogLevelInfoWhereLombokLog4j2IsUsed() {
        logCaptor = LogCaptor.forClass(BooService.class);
        logCaptor.setLogLevel(Level.INFO);

        Service service = new BooService();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN);
    }

    @Test
    public void captureLoggingEventsWhereLombokSlf4jIsUsed() {
        logCaptor = LogCaptor.forClass(QooService.class);

        Service service = new QooService();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN, LogMessage.DEBUG);
    }

    @Test
    public void captureLoggingEventsWithLogLevelInfoWhereLombokSlf4jIsUsed() {
        logCaptor = LogCaptor.forClass(QooService.class);
        logCaptor.setLogLevel(Level.INFO);

        Service service = new QooService();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN);
    }

    @Test
    public void captureLoggingEventsWhereLombokLog4jIsUsed() {
        logCaptor = LogCaptor.forClass(WooService.class);

        Service service = new WooService();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN, LogMessage.DEBUG);
    }

    @Test
    public void captureLoggingEventsWithLogLevelInfoWhereLombokLog4jIsUsed() {
        logCaptor = LogCaptor.forClass(WooService.class);
        logCaptor.setLogLevel(Level.INFO);

        Service service = new WooService();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN);
    }

    @Test
    public void captureLoggingEventsWhereLombokJavaUtilLoggingIsUsed() {
        logCaptor = LogCaptor.forClass(RooService.class);

        Service service = new RooService();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN);
    }

    private static void assertLogMessages(LogCaptor<?> logCaptor, LogMessage... logMessages) {
        for (LogMessage logMessage : logMessages) {
            assertThat(logCaptor.getLogs(logMessage.getLogLevel())).containsExactly(logMessage.getMessage());
        }

        String[] expectedLogMessages = Arrays.stream(logMessages)
                .map(LogMessage::getMessage)
                .toArray(String[]::new);

        assertThat(logCaptor.getLogs())
                .hasSize(expectedLogMessages.length)
                .containsExactly(expectedLogMessages);
    }

    @Nested
    public class ClearLogsShould {

        private LogCaptor<FooService> logCaptor = LogCaptor.forClass(FooService.class);

        @AfterEach
        public void clearLogs() {
            logCaptor.clearLogs();
        }

        @Test
        public void captureLoggingEventsWithLogLevelEnum() {
            Service service = new FooService();
            service.sayHello();

            assertThat(logCaptor.getLogs(Level.INFO)).containsExactly(LogMessage.INFO.getMessage());
            assertThat(logCaptor.getLogs(Level.DEBUG)).containsExactly(LogMessage.DEBUG.getMessage());
            assertThat(logCaptor.getLogs(Level.WARN)).containsExactly(LogMessage.WARN.getMessage());

            assertThat(logCaptor.getLogs())
                    .hasSize(3)
                    .containsExactly(LogMessage.INFO.getMessage(), LogMessage.WARN.getMessage(), LogMessage.DEBUG.getMessage());
        }

        @Test
        public void captureLoggingEventsWhereApacheLogManagerIsUsed() {
            Service service = new FooService();
            service.sayHello();

            assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN, LogMessage.DEBUG);
        }

    }

}
