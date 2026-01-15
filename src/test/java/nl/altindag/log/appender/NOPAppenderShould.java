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
