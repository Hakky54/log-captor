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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.spi.FilterReply;
import nl.altindag.console.ConsoleCaptor;
import nl.altindag.log.appender.InMemoryAppender;
import nl.altindag.log.exception.LogCaptorException;
import nl.altindag.log.model.LogEvent;
import nl.altindag.log.model.Marker;
import nl.altindag.log.service.LogMessage;
import nl.altindag.log.service.Service;
import nl.altindag.log.service.apache.ServiceWithApacheLog4j;
import nl.altindag.log.service.apache.ServiceWithApacheLog4jAndMarkers;
import nl.altindag.log.service.apache.ServiceWithApacheLog4jAndMdcHeaders;
import nl.altindag.log.service.apache.ServiceWithNestedApacheLog4j;
import nl.altindag.log.service.jdk.ServiceWithJavaUtilLogging;
import nl.altindag.log.service.jdk.ServiceWithNestedJavaUtilLogging;
import nl.altindag.log.service.lombok.ServiceWithLombokAndJavaUtilLogging;
import nl.altindag.log.service.lombok.ServiceWithLombokAndLog4j;
import nl.altindag.log.service.lombok.ServiceWithLombokAndLog4j2;
import nl.altindag.log.service.lombok.ServiceWithLombokAndNestedJavaUtilLogging;
import nl.altindag.log.service.lombok.ServiceWithLombokAndNestedLog4j;
import nl.altindag.log.service.lombok.ServiceWithLombokAndNestedLog4j2;
import nl.altindag.log.service.lombok.ServiceWithLombokAndSlf4j;
import nl.altindag.log.service.slfj4.ServiceWithNestedSlf4j;
import nl.altindag.log.service.slfj4.ServiceWithSlf4j;
import nl.altindag.log.service.slfj4.ServiceWithSlf4jAndCustomException;
import nl.altindag.log.service.slfj4.ServiceWithSlf4jAndMarkers;
import nl.altindag.log.service.slfj4.ServiceWithSlf4jAndMdcHeaders;
import nl.altindag.log.service.slfj4.ServiceWithSlf4jWhileUsingKeyValuePairs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.slf4j.simple.SimpleLogger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

/**
 * @author Hakan Altindag
 */
@ExtendWith(MockitoExtension.class)
class LogCaptorShould {

    private LogCaptor logCaptor;

    @AfterEach
    void resetProperties() {
        Optional.ofNullable(logCaptor)
                .ifPresent(LogCaptor::resetLogLevel);
    }

    @Test
    void captureLoggingEventsWhereApacheLogManagerIsUsed() {
        logCaptor = LogCaptor.forClass(ServiceWithApacheLog4j.class);
        logCaptor.setLogLevelToTrace();

        Service service = new ServiceWithApacheLog4j();
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
    void captureNestedLogsWhereApacheLogManagerIsUsed() {
        logCaptor = LogCaptor.forName(ServiceWithNestedApacheLog4j.NestedService.class.getCanonicalName());

        Service service = new ServiceWithNestedApacheLog4j.NestedService();
        service.sayHello();

        assertThat(logCaptor.getLogs()).containsExactly(LogMessage.INFO.getMessage());
    }

    @Test
    void captureNestedLogsWhereSlf4jIsUsedWith() {
        logCaptor = LogCaptor.forClass(ServiceWithNestedSlf4j.NestedService.class);

        Service service = new ServiceWithNestedSlf4j.NestedService();
        service.sayHello();

        assertThat(logCaptor.getLogs()).containsExactly(LogMessage.INFO.getMessage());
    }

    @Test
    void captureNestedLogsWhereJulIsUsedWith() {
        logCaptor = LogCaptor.forClass(ServiceWithNestedJavaUtilLogging.NestedService.class);

        Service service = new ServiceWithNestedJavaUtilLogging.NestedService();
        service.sayHello();

        assertThat(logCaptor.getLogs()).containsExactly(LogMessage.INFO.getMessage());
    }

    @Test
    void captureNestedLogsWhereLombokWithJulIsUsedWith() {
        logCaptor = LogCaptor.forClass(ServiceWithLombokAndNestedJavaUtilLogging.NestedService.class);

        Service service = new ServiceWithLombokAndNestedJavaUtilLogging.NestedService();
        service.sayHello();

        assertThat(logCaptor.getLogs()).containsExactly(LogMessage.INFO.getMessage());
    }

    @Test
    void captureNestedLogsWhereLombokWithLog4j2IsUsedWith() {
        logCaptor = LogCaptor.forName(ServiceWithLombokAndNestedLog4j2.NestedService.class.getCanonicalName());

        Service service = new ServiceWithLombokAndNestedLog4j2.NestedService();
        service.sayHello();

        assertThat(logCaptor.getLogs()).containsExactly(LogMessage.INFO.getMessage());
    }

    @Test
    void captureNestedLogsWhereLombokWithLog4jIsUsedWith() {
        logCaptor = LogCaptor.forClass(ServiceWithLombokAndNestedLog4j.NestedService.class);

        Service service = new ServiceWithLombokAndNestedLog4j.NestedService();
        service.sayHello();

        assertThat(logCaptor.getLogs()).containsExactly(LogMessage.INFO.getMessage());
    }

    @Test
    void captureNestedLogsWhereLombokWithSlf4jIsUsedWith() {
        logCaptor = LogCaptor.forClass(ServiceWithLombokAndNestedLog4j.NestedService.class);

        Service service = new ServiceWithLombokAndNestedLog4j.NestedService();
        service.sayHello();

        assertThat(logCaptor.getLogs()).containsExactly(LogMessage.INFO.getMessage());
    }

    @Test
    void captureLoggingEventsWithoutSpecifyingClass() {
        logCaptor = LogCaptor.forRoot();
        logCaptor.setLogLevelToTrace();

        Service service = new ServiceWithApacheLog4j();
        service.sayHello();

        assertThat(logCaptor.getInfoLogs()).containsExactly(LogMessage.INFO.getMessage());
        assertThat(logCaptor.getDebugLogs()).containsExactly(LogMessage.DEBUG.getMessage());
        assertThat(logCaptor.getWarnLogs()).containsExactly(LogMessage.WARN.getMessage());
        assertThat(logCaptor.getErrorLogs()).containsExactly(LogMessage.ERROR.getMessage());
        assertThat(logCaptor.getTraceLogs()).containsExactly(LogMessage.TRACE.getMessage());
    }

    @Test
    void captureLoggingEventsContainingException() {
        logCaptor = LogCaptor.forClass(ServiceWithSlf4jAndCustomException.class);

        Service service = new ServiceWithSlf4jAndCustomException();
        service.sayHello();

        List<LogEvent> logEvents = logCaptor.getLogEvents();
        assertThat(logEvents).hasSize(1);

        LogEvent logEvent = logEvents.get(0);
        assertThat(logEvent.getFormattedMessage()).isEqualTo("Caught unexpected exception");
        assertThat(logEvent.getLevel()).isEqualTo("ERROR");
        assertThat(logEvent.getThrowable()).isPresent();

        assertThat(logEvent.getThrowable().get())
                .hasMessage("KABOOM!")
                .isInstanceOf(IOException.class);
    }

    @Test
    void captureLoggingEventsContainingArguments() {
        logCaptor = LogCaptor.forClass(ServiceWithSlf4j.class);

        Service service = new ServiceWithSlf4j();
        service.sayHello();

        List<LogEvent> logEvents = logCaptor.getLogEvents();
        assertThat(logEvents).hasSize(1);

        LogEvent logEvent = logEvents.get(0);
        assertThat(logEvent.getArguments()).contains("Enter");
        assertThat(logEvent.getMessage()).isEqualTo("Keyboard not responding. Press {} key to continue...");
        assertThat(logEvent.getFormattedMessage()).isEqualTo("Keyboard not responding. Press Enter key to continue...");
    }

    @Test
    void captureLoggingEventsContainingThreadName() {
        logCaptor = LogCaptor.forClass(ServiceWithSlf4j.class);

        Service service = new ServiceWithSlf4j();
        service.sayHello();

        List<LogEvent> logEvents = logCaptor.getLogEvents();
        assertThat(logEvents).hasSize(1);

        LogEvent logEvent = logEvents.get(0);
        assertThat(logEvent.getThreadName()).isEqualTo("main");
    }

    @Test
    void captureLoggingEventsByUsingForNameMethodWithLogCaptor() {
        logCaptor = LogCaptor.forName("nl.altindag.log.service.apache.ServiceWithApacheLog4j");
        logCaptor.setLogLevelToTrace();

        Service service = new ServiceWithApacheLog4j();
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
        logCaptor = LogCaptor.forClass(ServiceWithApacheLog4j.class);
        logCaptor.setLogLevelToInfo();

        Service service = new ServiceWithApacheLog4j();
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
        logCaptor = LogCaptor.forClass(ServiceWithLombokAndLog4j2.class);

        Service service = new ServiceWithLombokAndLog4j2();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN, LogMessage.DEBUG);
    }

    @Test
    void captureLoggingEventsWithLogLevelInfoWhereLombokLog4j2IsUsed() {
        logCaptor = LogCaptor.forClass(ServiceWithLombokAndLog4j2.class);
        logCaptor.setLogLevelToInfo();

        Service service = new ServiceWithLombokAndLog4j2();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN);
    }

    @Test
    void captureLoggingEventsWhereLombokSlf4jIsUsed() {
        logCaptor = LogCaptor.forClass(ServiceWithLombokAndSlf4j.class);

        Service service = new ServiceWithLombokAndSlf4j();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN, LogMessage.DEBUG);
    }

    @Test
    void captureLoggingEventsWithLogLevelInfoWhereLombokSlf4jIsUsed() {
        logCaptor = LogCaptor.forClass(ServiceWithLombokAndSlf4j.class);
        logCaptor.setLogLevelToInfo();

        Service service = new ServiceWithLombokAndSlf4j();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN);
    }

    @Test
    void captureLoggingEventsWhereLombokLog4jIsUsed() {
        logCaptor = LogCaptor.forClass(ServiceWithLombokAndLog4j.class);

        Service service = new ServiceWithLombokAndLog4j();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN, LogMessage.DEBUG);
    }

    @Test
    void captureLoggingEventsWithLogLevelInfoWhereLombokLog4jIsUsed() {
        logCaptor = LogCaptor.forClass(ServiceWithLombokAndLog4j.class);
        logCaptor.setLogLevelToInfo();

        Service service = new ServiceWithLombokAndLog4j();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN);
    }

    @Test
    void captureLoggingEventsWhereLombokJavaUtilLoggingIsUsed() {
        logCaptor = LogCaptor.forClass(ServiceWithLombokAndJavaUtilLogging.class);

        Service service = new ServiceWithLombokAndJavaUtilLogging();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN);
    }

    @Test
    void captureLoggingEventsWhereJavaUtilLoggingIsUsed() {
        logCaptor = LogCaptor.forClass(ServiceWithJavaUtilLogging.class);
        logCaptor.setLogLevelToTrace();

        Service service = new ServiceWithJavaUtilLogging();
        service.sayHello();

        assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.ERROR, LogMessage.DEBUG, LogMessage.TRACE);
    }

    @Test
    void doNotCaptureLogMessagesWhenItIsDisabled() {
        logCaptor = LogCaptor.forClass(ServiceWithApacheLog4j.class);
        logCaptor.disableLogs();

        Service service = new ServiceWithApacheLog4j();
        service.sayHello();

        assertThat(logCaptor.getLogs()).isEmpty();
    }

    @Test
    void captureTimeStampOfLogsAndRetainOrderOfOccurrence() {
        logCaptor = LogCaptor.forClass(ServiceWithApacheLog4j.class);
        logCaptor.setLogLevelToTrace();

        Service service = new ServiceWithApacheLog4j();
        service.sayHello();

        List<LogEvent> logEvents = logCaptor.getLogEvents();

        Optional<LogEvent> infoLog = logEvents.stream().filter(logEvent -> logEvent.getLevel().equalsIgnoreCase("info")).findFirst();
        Optional<LogEvent> traceLog = logEvents.stream().filter(logEvent -> logEvent.getLevel().equalsIgnoreCase("trace")).findFirst();

        assertThat(infoLog).isPresent();
        assertThat(traceLog).isPresent();

        assertThat(infoLog.get().getTimeStamp()).isBeforeOrEqualTo(traceLog.get().getTimeStamp());
    }

    @Test
    void captureLoggerName() {
        logCaptor = LogCaptor.forRoot();

        Service service = new ServiceWithApacheLog4j();
        service.sayHello();

        List<LogEvent> logEvents = logCaptor.getLogEvents();

        assertThat(logEvents).hasSize(4);
        assertThat(logEvents.get(0).getLoggerName()).isEqualTo(ServiceWithApacheLog4j.class.getName());
    }

    @Test
    void captureLoggingEventsContainingMarkersWithSlf4j() {
        logCaptor = LogCaptor.forClass(ServiceWithSlf4jAndMarkers.class);

        Service service = new ServiceWithSlf4jAndMarkers();
        service.sayHello();

        List<LogEvent> logEvents = logCaptor.getLogEvents();
        assertThat(logEvents).hasSize(1);

        LogEvent logEvent = logEvents.get(0);
        assertThat(logEvent.getFormattedMessage()).isEqualTo("I haven't spoken to my wife in years. I didn't want to interrupt her.");

        List<Marker> markers = logEvent.getMarkers();
        assertThat(markers).hasSize(1);

        Marker marker = markers.get(0);
        assertThat(marker.getName()).isEqualTo("marriage");
        assertThat(marker.getReferences()).hasSize(2);

        List<Marker> references = marker.getReferences();
        Marker husband = references.get(0);
        Marker wife = references.get(1);

        assertThat(husband.getName()).isEqualTo("James");
        assertThat(husband.getReferences()).isEmpty();

        assertThat(wife.getName()).isEqualTo("Mary");
        assertThat(wife.getReferences()).hasSize(3);

        List<Marker> innerReferences = wife.getReferences();
        Marker childOne = innerReferences.get(0);
        Marker childTwo = innerReferences.get(1);
        Marker childThree = innerReferences.get(2);

        assertThat(childOne.getName()).isEqualTo("Michael");
        assertThat(childTwo.getName()).isEqualTo("Jennifer");
        assertThat(childThree.getName()).isEqualTo("Elizabeth");
    }

    @Test
    void captureLoggingEventsContainingMarkersWithLog4j() {
        logCaptor = LogCaptor.forClass(ServiceWithApacheLog4jAndMarkers.class);

        Service service = new ServiceWithApacheLog4jAndMarkers();
        service.sayHello();

        List<LogEvent> logEvents = logCaptor.getLogEvents();
        assertThat(logEvents).hasSize(1);

        LogEvent logEvent = logEvents.get(0);
        assertThat(logEvent.getFormattedMessage()).isEqualTo("I haven't spoken to my wife in years. I didn't want to interrupt her.");

        List<Marker> markers = logEvent.getMarkers();
        assertThat(markers).hasSize(1);

        Marker marker = markers.get(0);
        assertThat(marker.getName()).isEqualTo("Michael");
        assertThat(marker.getReferences()).hasSize(1);

        List<Marker> references = marker.getReferences();
        Marker mother = references.get(0);

        assertThat(mother.getName()).isEqualTo("Mary");
        assertThat(mother.getReferences()).hasSize(1);

        List<Marker> innerReferences = mother.getReferences();
        Marker innerMarker = innerReferences.get(0);

        assertThat(innerMarker.getName()).isEqualTo("marriage");
        assertThat(innerMarker.getReferences()).isEmpty();
    }

    @Test
    void throwExceptionWhenLoggerImplementationIsNull() {
        try (MockedStatic<LoggerFactory> loggerFactoryMockedStatic = mockStatic(LoggerFactory.class)) {

            loggerFactoryMockedStatic.when(() -> LoggerFactory.getLogger(anyString())).thenReturn(null);

            assertThatThrownBy(LogCaptor::forRoot)
                    .isInstanceOf(LogCaptorException.class)
                    .hasMessage("SLF4J Logger implementation should be of the type [ch.qos.logback.classic.Logger] but found [nothing].");
        }
    }

    @Test
    void throwExceptionWhenLoggerImplementationIsNotLogback() {
        try (MockedStatic<LoggerFactory> loggerFactoryMockedStatic = mockStatic(LoggerFactory.class)) {

            org.slf4j.Logger logger = mock(SimpleLogger.class);
            loggerFactoryMockedStatic.when(() -> LoggerFactory.getLogger(anyString())).thenReturn(logger);

            assertThatThrownBy(LogCaptor::forRoot)
                    .isInstanceOf(LogCaptorException.class)
                    .hasMessage("SLF4J Logger implementation should be of the type [ch.qos.logback.classic.Logger] but found [org.slf4j.simple.SimpleLogger].");
        }
    }

    @Test
    void throwExceptionWhenLoggerImplementationIsFromAnotherClassloader() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        CustomClassLoader classLoader = new CustomClassLoader();
        Class<?> loggerClass = classLoader.findClass("ch.qos.logback.classic.Logger");
        Constructor<?> loggerConstructor = loggerClass.getDeclaredConstructors()[0];
        loggerConstructor.setAccessible(true);

        Object logger = loggerConstructor.newInstance(null, null, null);

        try (MockedStatic<LoggerFactory> loggerFactoryMockedStatic = mockStatic(LoggerFactory.class)) {

            loggerFactoryMockedStatic.when(() -> LoggerFactory.getLogger(anyString())).thenReturn(logger);

            assertThatThrownBy(LogCaptor::forRoot)
                    .isInstanceOf(LogCaptorException.class)
                    .hasMessage(String.format("Multiple classloaders are being used. The Logging API is created by the following classloader: [nl.altindag.log.LogCaptorShould$CustomClassLoader], " +
                            "while it should have been created by the following classloader: [%s].", this.getClass().getClassLoader().getClass().getName()
                    ));
        }
    }

    @Test
    void filterInfoMessages() {
        LevelFilter levelFilter = new LevelFilter();
        levelFilter.setOnMismatch(FilterReply.DENY);
        levelFilter.setLevel(Level.INFO);

        logCaptor = LogCaptor.forClass(ServiceWithApacheLog4j.class);
        logCaptor.addFilter(levelFilter);
        logCaptor.setLogLevelToTrace();

        Service service = new ServiceWithApacheLog4j();
        service.sayHello();

        assertThat(logCaptor.getLogEvents())
                .extracting(LogEvent::getLevel)
                .map(Level::toLevel)
                .allMatch(Level.INFO::equals, "INFO");
    }

    @Test
    void captureMdcHeadersWhereLog4jIsUsed() {
        logCaptor = LogCaptor.forClass(ServiceWithApacheLog4jAndMdcHeaders.class);

        Service service = new ServiceWithApacheLog4jAndMdcHeaders();
        service.sayHello();

        assertDiagnosticContext(logCaptor, "test-log4j-mdc", "hello-log4j");
    }

    @Test
    void captureMdcHeadersWhereSlf4jIsUsed() {
        logCaptor = LogCaptor.forClass(ServiceWithSlf4jAndMdcHeaders.class);

        Service service = new ServiceWithSlf4jAndMdcHeaders();
        service.sayHello();

        assertDiagnosticContext(logCaptor, "test-slf4j-mdc", "hello-slf4j");
    }

    @Test
    void captureLoggingEventsContainingKeyValuePairs() {
        logCaptor = LogCaptor.forClass(ServiceWithSlf4jWhileUsingKeyValuePairs.class);

        Service service = new ServiceWithSlf4jWhileUsingKeyValuePairs();
        service.sayHello();

        List<LogEvent> logEvents = logCaptor.getLogEvents();
        assertThat(logEvents).hasSize(1);

        LogEvent logEvent = logEvents.get(0);
        assertThat(logEvent.getMessage()).isEqualTo("My grocery list");

        List<Map.Entry<String, Object>> keyValuePairs = logEvent.getKeyValuePairs();
        assertThat(keyValuePairs)
                .hasSize(2)
                .contains(new SimpleImmutableEntry<>("fruit", "apple"))
                .contains(new SimpleImmutableEntry<>("vegetable", "tomato"));
    }

    @Test
    void detachAppenderWithCloseMethod() {
        Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
        assertThat(fetchAppenders(logger)).isEmpty();

        logCaptor = LogCaptor.forClass(this.getClass());
        assertListAppender(logger);

        logCaptor.close();
        assertThat(fetchAppenders(logger)).isEmpty();
    }

    @Test
    void detachAppenderWithAutoClosable() {
        Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
        assertThat(fetchAppenders(logger)).isEmpty();

        try (LogCaptor ignored = LogCaptor.forClass(this.getClass())) {
            assertListAppender(logger);
        }

        assertThat(fetchAppenders(logger)).isEmpty();
    }

    /**
     * NopStatusListener disables the output of logs to the console. If the listener is present in a configuration file
     * LogCaptor won't be able to provide the console appender as it does not exist at all. Getting the console appender should also not
     * throw an exception when the NopStatusListener is present. Having the listener and calling the method {@link LogCaptor#disableConsoleOutput()}
     * does not make sense as it is already disabled by the listener.
     */
    @Test
    void notProvideConsoleAppenderWhenNopStatusListenerIsPresentAsLogBackConfiguration() throws IOException, JoranException {
        try (InputStream inputStream = this.getClass().getResourceAsStream("/logback-config-examples/logback-test.xml")) {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            loggerContext.reset();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(loggerContext);
            configurator.doConfigure(inputStream);
        }

        logCaptor = LogCaptor.forClass(ServiceWithApacheLog4j.class);
        assertThat(logCaptor.getConsoleAppender()).isEmpty();
    }

    @Test
    void provideConsoleAppenderWhenNoNopStatusListenerIsPresentAsLogBackConfiguration() {
        logCaptor = LogCaptor.forClass(ServiceWithApacheLog4j.class);
        assertThat(logCaptor.getConsoleAppender()).isPresent();
    }

    @Test
    void disableConsoleOutput() {
        logCaptor = LogCaptor.forClass(ServiceWithApacheLog4j.class);
        logCaptor.disableConsoleOutput();

        Service service = new ServiceWithApacheLog4j();
        try (ConsoleCaptor consoleCaptor = new ConsoleCaptor()) {
            service.sayHello();
            assertThat(logCaptor.getLogs()).hasSizeGreaterThan(0);
            assertThat(consoleCaptor.getStandardOutput()).isEmpty();

            logCaptor.enableConsoleOutput();
            logCaptor.clearLogs();

            service.sayHello();
            assertThat(logCaptor.getLogs()).hasSizeGreaterThan(0);
            assertThat(consoleCaptor.getStandardOutput()).hasSizeGreaterThan(0);
        }
    }

    private static void assertListAppender(Logger logger) {
        assertThat(fetchAppenders(logger))
                .hasSize(1)
                .first()
                .isInstanceOf(InMemoryAppender.class)
                .extracting(Appender::getName)
                .isEqualTo("log-captor");
    }

    private static List<Appender<?>> fetchAppenders(Logger logger) {
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(logger.iteratorForAppenders(), Spliterator.ORDERED), false)
                .collect(Collectors.toList());
    }

    private static void assertDiagnosticContext(LogCaptor logCaptor, String mdcKey, String mdcValue) {
        List<LogEvent> logEvents = logCaptor.getLogEvents();

        assertThat(logEvents).hasSize(2);

        assertThat(logEvents.get(0).getDiagnosticContext())
                .hasSize(1)
                .extractingByKey(mdcKey)
                .isEqualTo(mdcValue);

        assertThat(logEvents.get(1).getDiagnosticContext()).isEmpty();
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

        private final LogCaptor logCaptor = LogCaptor.forClass(ServiceWithApacheLog4j.class);

        @AfterEach
        void clearLogs() {
            logCaptor.clearLogs();
        }

        @Test
        void captureLogging() {
            Service service = new ServiceWithApacheLog4j();
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
            Service service = new ServiceWithApacheLog4j();
            service.sayHello();

            assertLogMessages(logCaptor, LogMessage.INFO, LogMessage.WARN, LogMessage.ERROR, LogMessage.DEBUG);
        }

    }

    private static class CustomClassLoader extends ClassLoader {

        @Override
        public Class<?> findClass(String name) {
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(name.replace(".", "/") + ".class");
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                Objects.requireNonNull(inputStream);

                int len = 0;
                while ((len = inputStream.read()) != -1) {
                    outputStream.write(len);
                }

                byte[] bytes = outputStream.toByteArray();
                return defineClass(name, bytes, 0, bytes.length);
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }

    }

}
