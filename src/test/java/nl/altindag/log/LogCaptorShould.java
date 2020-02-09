package nl.altindag.log;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;

import ch.qos.logback.classic.Level;
import nl.altindag.log.service.LogMessage;
import nl.altindag.log.service.Service;
import nl.altindag.log.service.apache.FooService;
import nl.altindag.log.service.lombok.BooService;
import nl.altindag.log.service.lombok.QooService;
import nl.altindag.log.service.lombok.RooService;
import nl.altindag.log.service.lombok.WooService;

public class LogCaptorShould {

    @Test
    public void captureLoggingEventsWhereWithLogLevel() {
        LogCaptor<FooService> logCaptor = LogCaptor.forClass(FooService.class);

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
        LogCaptor<FooService> logCaptor = LogCaptor.forClass(FooService.class);

        Service service = new FooService();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN, LogMessage.DEBUG);
    }

    @Test
    public void captureLoggingEventsWhereLombokLog4j2IsUsed() {
        LogCaptor<BooService> logCaptor = LogCaptor.forClass(BooService.class);

        Service service = new BooService();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN, LogMessage.DEBUG);
    }

    @Test
    public void captureLoggingEventsWhereLombokSlf4jIsUsed() {
        LogCaptor<QooService> logCaptor = LogCaptor.forClass(QooService.class);

        Service service = new QooService();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN, LogMessage.DEBUG);
    }

    @Test
    public void captureLoggingEventsWhereLombokLog4jIsUsed() {
        LogCaptor<WooService> logCaptor = LogCaptor.forClass(WooService.class);

        Service service = new WooService();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN, LogMessage.DEBUG);
    }

    @Test
    public void captureLoggingEventsWhereLombokJavaUtilLoggingIsUsed() {
        LogCaptor<RooService> logCaptor = LogCaptor.forClass(RooService.class);

        Service service = new RooService();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN);
    }

    private void assertLogMessages(LogCaptor<?> logCaptor, LogMessage... logMessages) {
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

}
