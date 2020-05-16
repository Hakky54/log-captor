package nl.altindag.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class LogCaptor<T> {

    private static final Map<String, Level> LOG_LEVEL_CONTAINER = new HashMap<>();

    private final Logger logger;
    private final ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

    private LogCaptor(Class<T> clazz) {
        logger = (Logger) LoggerFactory.getLogger(clazz);
        if (!LOG_LEVEL_CONTAINER.containsKey(logger.getName())) {
            LOG_LEVEL_CONTAINER.put(logger.getName(), logger.getEffectiveLevel());
        }

        listAppender.start();
        logger.addAppender(listAppender);
    }

    public static <T> LogCaptor<T> forClass(Class<T> clazz) {
        return new LogCaptor<>(clazz);
    }

    public List<String> getLogs() {
        return listAppender.list.stream()
                .map(ILoggingEvent::getFormattedMessage)
                .collect(toList());
    }

    public List<String> getInfoLogs() {
        return getLogs(Level.INFO);
    }

    public List<String> getDebugLogs() {
        return getLogs(Level.DEBUG);
    }

    public List<String> getWarnLogs() {
        return getLogs(Level.WARN);
    }

    public List<String> getErrorLogs() {
        return getLogs(Level.ERROR);
    }

    public List<String> getTraceLogs() {
        return getLogs(Level.TRACE);
    }

    private List<String> getLogs(Level level) {
        return listAppender.list.stream()
                .filter(logEvent -> logEvent.getLevel() == level)
                .map(ILoggingEvent::getFormattedMessage)
                .collect(toList());
    }

    /**
     * Overrides the log level property of the target class. This may result that the overridden property
     * of the target class is still active even though a new instance of {@link LogCaptor} has been created.
     * To roll-back to the initial state use: {@link LogCaptor#resetLogLevel()}
     *
     * This option will implicitly include the following log levels: WARN and ERROR
     */
    public void setLogLevelToInfo() {
        logger.setLevel(Level.INFO);
    }

    /**
     * Overrides the log level property of the target class. This may result that the overridden property
     * of the target class is still active even though a new instance of {@link LogCaptor} has been created.
     * To roll-back to the initial state use: {@link LogCaptor#resetLogLevel()}
     *
     * This option will implicitly include the following log levels: INFO, WARN and ERROR
     */
    public void setLogLevelToDebug() {
        logger.setLevel(Level.DEBUG);
    }

    /**
     * Overrides the log level property of the target class. This may result that the overridden property
     * of the target class is still active even though a new instance of {@link LogCaptor} has been created.
     * To roll-back to the initial state use: {@link LogCaptor#resetLogLevel()}
     *
     * This option will implicitly include the following log levels: INFO, DEBUG, WARN and ERROR
     */
    public void setLogLevelToTrace() {
        logger.setLevel(Level.TRACE);
    }

    /**
     * Resets the log level of the target class to the initial value which was available before
     * changing it with {@link LogCaptor#setLogLevelToInfo()}, {@link LogCaptor#setLogLevelToDebug()} or with {@link LogCaptor#setLogLevelToTrace()}
     */
    public void resetLogLevel() {
        Optional.ofNullable(LOG_LEVEL_CONTAINER.get(logger.getName()))
                .ifPresent(logger::setLevel);
    }

    public void clearLogs() {
        listAppender.list.clear();
    }

}
