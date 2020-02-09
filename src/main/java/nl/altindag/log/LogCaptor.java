package nl.altindag.log;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

public class LogCaptor<T> {

    private ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

    private LogCaptor(Class<T> clazz) {
        Logger logger = (Logger) LoggerFactory.getLogger(clazz);

        listAppender.start();
        logger.addAppender(listAppender);
    }

    public static <T> LogCaptor<T> forClass(Class<T> clazz) {
        return new LogCaptor<>(clazz);
    }

    public List<String> getLogs(Level level) {
        return listAppender.list.stream()
                .filter(logEvent -> logEvent.getLevel() == level)
                .map(ILoggingEvent::getFormattedMessage)
                .collect(toList());
    }

    public List<String> getLogs(String level) {
        return listAppender.list.stream()
                                .filter(logEvent -> logEvent.getLevel().toString().equalsIgnoreCase(level))
                                .map(ILoggingEvent::getFormattedMessage)
                                .collect(toList());
    }

    public List<String> getLogs() {
        return listAppender.list.stream()
                .map(ILoggingEvent::getFormattedMessage)
                .collect(toList());
    }

}
