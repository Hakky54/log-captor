package nl.altindag.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class LogCaptor<T> {

    private static final Map<String, Level> LOG_LEVEL_CONTAINER = new HashMap<>();

    private Logger logger;
    private ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

    private LogCaptor(Class<T> clazz) {
        logger = (Logger) LoggerFactory.getLogger(clazz);
        if (!LOG_LEVEL_CONTAINER.containsKey(clazz.getName())) {
            LOG_LEVEL_CONTAINER.put(logger.getName(), logger.getEffectiveLevel());
        }

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

    public void setLogLevel(Level newLevel) {
        logger.setLevel(newLevel);
    }

    public void resetLogLevel() {
        Level initialLogLevelOfTargetClazz = LOG_LEVEL_CONTAINER.get(logger.getName());
        logger.setLevel(initialLogLevelOfTargetClazz);
    }

}
