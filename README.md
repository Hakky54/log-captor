[![Actions Status](https://github.com/Hakky54/log-captor/workflows/Build/badge.svg)](https://github.com/Hakky54/log-captor/actions)
[![Foresight Docs](https://foresight.service.thundra.io/public/api/v1/badge/test/3dcee99b-eef5-4f7c-a06b-5ca65ce9a45c)](https://foresight.docs.thundra.io/)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=io.github.hakky54%3Alogcaptor&metric=alert_status)](https://sonarcloud.io/dashboard?id=io.github.hakky54%3Alogcaptor)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=io.github.hakky54%3Alogcaptor&metric=coverage)](https://sonarcloud.io/dashboard?id=io.github.hakky54%3Alogcaptor)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=io.github.hakky54%3Alogcaptor&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=io.github.hakky54%3Alogcaptor)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=io.github.hakky54%3Alogcaptor&metric=security_rating)](https://sonarcloud.io/dashboard?id=io.github.hakky54%3Alogcaptor)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=io.github.hakky54%3Alogcaptor&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=io.github.hakky54%3Alogcaptor)
[![Apache2 license](https://img.shields.io/badge/license-Aache2.0-blue.svg)](https://github.com/Hakky54/log-captor/blob/master/LICENSE)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.hakky54/logcaptor/badge.svg)](https://mvnrepository.com/artifact/io.github.hakky54/logcaptor)
[![javadoc](https://javadoc.io/badge2/io.github.hakky54/logcaptor/javadoc.svg)](https://javadoc.io/doc/io.github.hakky54/logcaptor)
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2FHakky54%2Flog-captor.svg?type=shield)](https://app.fossa.io/projects/git%2Bgithub.com%2FHakky54%2Flog-captor?ref=badge_shield)
[![Join the chat at https://gitter.im/hakky54/logcaptor](https://badges.gitter.im/hakky54/logcaptor.svg)](https://gitter.im/hakky54/logcaptor?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/dashboard?id=io.github.hakky54%3Alogcaptor)

# LogCaptor [![Tweet](https://img.shields.io/twitter/url/http/shields.io.svg?style=social)](https://twitter.com/intent/tweet?text=With%20LogCaptor%20it%20is%20now%20very%20easy%20to%20captor%20and%20test%20your%20log%20message&url=https://github.com/Hakky54/log-captor/&via=hakky541&hashtags=logging,testing,log4j,slf4j,log4j2,jul,lombok,developer,java,scala,kotlin,logcaptor)

# Install library with:
### Install with [maven](https://mvnrepository.com/artifact/io.github.hakky54/logcaptor)
```xml
<dependency>
    <groupId>io.github.hakky54</groupId>
    <artifactId>logcaptor</artifactId>
    <version>2.7.5</version>
    <scope>test</scope>
</dependency>
```
### Install with Gradle
```groovy
testImplementation 'io.github.hakky54:logcaptor:2.7.5'
```
### Install with Scala SBT
```
libraryDependencies += "io.github.hakky54" % "logcaptor" % "2.7.5" % Test
```
### Install with Apache Ivy
```xml
<dependency org="io.github.hakky54" name="logcaptor" rev="2.7.5" />
```

## Table of contents
1. [Introduction](#introduction)
   - [Advantages](#advantages)
   - [Supported Java versions](#supported-java-versions)
   - [Tested Logging libraries](#tested-logging-libraries)
2. [Usage](#usage)
   - [Capture logs](#capture-logs)
   - [Reuse LogCaptor for multiple tests](#initialize-logcaptor-once-and-reuse-it-during-multiple-tests-with-clearlogs-method-within-the-aftereach-method)
   - [Capture logs for enabled logs only](#class-which-will-log-events-if-specific-log-level-has-been-set)
   - [Capture exceptions within logs](#class-which-will-also-log-an-exception)
   - [Capture Managed Diagnostic Context (MDC)](#capture-managed-diagnostic-context-mdc)  
   - [Disable any logs for specific class](#disable-any-logs-for-a-specific-class)   
   - [Disable all logs](#disable-all-logs)
   - [Returnable values from LogCaptor](#returnable-values-from-logcaptor)
3. [Known issues](#known-issues)
   - [Using Log Captor alongside with other logging libraries](#using-log-captor-alongside-with-other-logging-libraries)
   - [Capturing logs of static inner classes](#capturing-logs-of-static-inner-classes)
4. [Contributing](#contributing)
5. [Contributors](#contributors-)
5. [License](#license)
   

# Introduction
Hey, hello there 👋 Welcome, you are ![visitors](https://visitor-badge.glitch.me/badge?page_id=https://github.com/Hakky54/log-captor) I hope you will like this library ❤️

LogCaptor is a library which will enable you to easily capture logging entries for unit testing purposes. 

Do you want to capture the console output? Please have a look at [ConsoleCaptor](https://github.com/Hakky54/console-captor).

### Advantages
- No mocking required
- No custom JUnit extension required
- Plug & play

### Supported Java versions
- Java 8
- Java 11+

### Tested Logging libraries
 - SLFJ4
 - Logback
 - Java Util Logging  
 - Apache Log4j
 - Apache Log4j2  
 - Log4j with Lombok
 - Log4j2 with Lombok
 - SLFJ4 with Lombok
 - Java Util Logging with Lombok

See the unit test [LogCaptorShould](src/test/java/nl/altindag/log/LogCaptorShould.java) for all the scenario's or checkout this project [Java Tutorials](https://github.com/Hakky54/java-tutorials) which contains more isolated examples of the individual logging frameworks
 
# Usage
##### Capture logs
```java
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FooService {

    private static final Logger LOGGER = LogManager.getLogger(FooService.class);

    public void sayHello() {
        LOGGER.info("Keyboard not responding. Press any key to continue...");
        LOGGER.warn("Congratulations, you are pregnant!");
    }

}
```
###### Unit test:
```java
import static org.assertj.core.api.Assertions.assertThat;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;

public class FooServiceShould {

    @Test
    public void logInfoAndWarnMessages() {
        LogCaptor logCaptor = LogCaptor.forClass(FooService.class);

        FooService fooService = new FooService();
        fooService.sayHello();

        // Get logs based on level
        assertThat(logCaptor.getInfoLogs()).containsExactly("Keyboard not responding. Press any key to continue...");
        assertThat(logCaptor.getWarnLogs()).containsExactly("Congratulations, you are pregnant!");

        // Get all logs
        assertThat(logCaptor.getLogs())
                .hasSize(2)
                .contains(
                    "Keyboard not responding. Press any key to continue...",
                    "Congratulations, you are pregnant!"
                );
    }
}
```

##### Initialize LogCaptor once and reuse it during multiple tests with clearLogs method within the afterEach method:
```java
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

public class FooServiceShould {

    private static LogCaptor logCaptor;
    private static final String EXPECTED_INFO_MESSAGE = "Keyboard not responding. Press any key to continue...";
    private static final String EXPECTED_WARN_MESSAGE = "Congratulations, you are pregnant!";
    
    @BeforeAll
    public static setupLogCaptor() {
        logCaptor = LogCaptor.forClass(FooService.class);
    }

    @AfterEach
    public void clearLogs() {
        logCaptor.clearLogs();
    }
    
    @AfterAll
    public static void tearDown() {
        logCaptor.close();
    }

    @Test
    public void logInfoAndWarnMessagesAndGetWithEnum() {
        FooService service = new FooService();
        service.sayHello();

        assertThat(logCaptor.getInfoLogs()).containsExactly(EXPECTED_INFO_MESSAGE);
        assertThat(logCaptor.getWarnLogs()).containsExactly(EXPECTED_WARN_MESSAGE);

        assertThat(logCaptor.getLogs()).hasSize(2);
    }

    @Test
    public void logInfoAndWarnMessagesAndGetWithString() {
        FooService service = new FooService();
        service.sayHello();

        assertThat(logCaptor.getInfoLogs()).containsExactly(EXPECTED_INFO_MESSAGE);
        assertThat(logCaptor.getWarnLogs()).containsExactly(EXPECTED_WARN_MESSAGE);

        assertThat(logCaptor.getLogs()).hasSize(2);
    }

}
```

##### Class which will log events if specific log level has been set
```java
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FooService {

    private static final Logger LOGGER = LogManager.getLogger(FooService.class);

    public void sayHello() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Keyboard not responding. Press any key to continue...");
        }
        LOGGER.info("Congratulations, you are pregnant!");
    }

}
```
###### Unit test:
```java
import static org.assertj.core.api.Assertions.assertThat;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;

public class FooServiceShould {

    @Test
    public void logInfoAndWarnMessages() {
        LogCaptor logCaptor = LogCaptor.forClass(FooService.class);
        logCaptor.setLogLevelToInfo();

        FooService fooService = new FooService();
        fooService.sayHello();

        assertThat(logCaptor.getInfoLogs()).contains("Congratulations, you are pregnant!");
        assertThat(logCaptor.getDebugLogs())
            .doesNotContain("Keyboard not responding. Press any key to continue...")
            .isEmpty();
    }
}
```

##### Class which will also log an exception
```java
import nl.altindag.log.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FooService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZooService.class);

    @Override
    public void sayHello() {
        try {
            tryToSpeak();
        } catch (IOException e) {
            LOGGER.error("Caught unexpected exception", e);
        }
    }

    private void tryToSpeak() throws IOException {
        throw new IOException("KABOOM!");
    }
}
```
###### Unit test:
```java
import static org.assertj.core.api.Assertions.assertThat;

import nl.altindag.log.LogCaptor;
import nl.altindag.log.model.LogEvent;
import org.junit.jupiter.api.Test;

public class FooServiceShould {

    @Test
    void captureLoggingEventsContainingException() {
        LogCaptor logCaptor = LogCaptor.forClass(ZooService.class);

        FooService service = new FooService();
        service.sayHello();

        List<LogEvent> logEvents = logCaptor.getLogEvents();
        assertThat(logEvents).hasSize(1);

        LogEvent logEvent = logEvents.get(0);
        assertThat(logEvent.getMessage()).isEqualTo("Caught unexpected exception");
        assertThat(logEvent.getLevel()).isEqualTo("ERROR");
        assertThat(logEvent.getThrowable()).isPresent();

        assertThat(logEvent.getThrowable().get())
                .hasMessage("KABOOM!")
                .isInstanceOf(IOException.class);
    }
}
```
##### Capture Managed Diagnostic Context (MDC)
```java
import nl.altindag.log.service.LogMessage;
import nl.altindag.log.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class FooService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceWithSlf4jAndMdcHeaders.class);

    public void sayHello() {
        try {
            MDC.put("my-mdc-key", "my-mdc-value");
            LOGGER.info(LogMessage.INFO.getMessage());
        } finally {
            MDC.clear();
        }

        LOGGER.info("Hello there!");
    }

}
```
###### Unit test:
```java
import static org.assertj.core.api.Assertions.assertThat;

import nl.altindag.log.LogCaptor;
import nl.altindag.log.model.LogEvent;
import org.junit.jupiter.api.Test;

public class FooServiceShould {

   @Test
   void captureLoggingEventsContainingMdc() {
      LogCaptor logCaptor = LogCaptor.forClass(FooService.class);

      FooService service = new FooService();
      service.sayHello();

      List<LogEvent> logEvents = logCaptor.getLogEvents();

      assertThat(logEvents).hasSize(2);

      assertThat(logEvents.get(0).getDiagnosticContext())
              .hasSize(1)
              .extractingByKey("my-mdc-key")
              .isEqualTo("my-mdc-value");

      assertThat(logEvents.get(1).getDiagnosticContext()).isEmpty();
   }
}
```

##### Disable any logs for a specific class
In some use cases a unit test can generate too many logs by another class. This could be annoying as it will cause noise in your build logs. LogCaptor can disable those log messages with the following snippet:

```java
import static org.assertj.core.api.Assertions.assertThat;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FooServiceShould {

    private static LogCaptor logCaptorForSomeOtherService = LogCaptor.forClass(SomeService.class);

    @BeforeAll
    static void disableLogs() {
        logCaptorForSomeOtherService.disableLogs();
    }

    @AfterAll
    static void resetLogLevel() {
        logCaptorForSomeOtherService.resetLogLevel();
    }

    @Test
    void logInfoAndWarnMessages() {
        LogCaptor logCaptor = LogCaptor.forClass(FooService.class);

       FooService service = new FooService();
        service.sayHello();

        assertThat(logCaptor.getLogs())
                .hasSize(2)
                .contains(
                    "Keyboard not responding. Press any key to continue...",
                    "Congratulations, you are pregnant!"
                );
    }
}
```

##### Disable all logs
Add `logback-test.xml` to your test resources with the following content:
```xml
<configuration>
    <statusListener class="ch.qos.logback.core.status.NopStatusListener" />
</configuration>
```

##### Returnable values from LogCaptor
```java
import nl.altindag.log.LogCaptor;
import nl.altindag.log.model.LogEvent;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class FooServiceShould {

   @Test
   void showCaseAllReturnableValues() {
        LogCaptor logCaptor = LogCaptor.forClass(FooService.class);

        List<String> logs = logCaptor.getLogs();
        List<String> infoLogs = logCaptor.getInfoLogs();
        List<String> debugLogs = logCaptor.getDebugLogs();
        List<String> warnLogs = logCaptor.getWarnLogs();
        List<String> errorLogs = logCaptor.getErrorLogs();
        List<String> traceLogs = logCaptor.getTraceLogs();

        LogEvent logEvent = logCaptor.getLogEvents().get(0);
        String message = logEvent.getMessage();
        String formattedMessage = logEvent.getFormattedMessage();
        String level = logEvent.getLevel();
        List<Object> arguments = logEvent.getArguments();
        String loggerName = logEvent.getLoggerName();
        String threadName = logEvent.getThreadName();
        ZonedDateTime timeStamp = logEvent.getTimeStamp();
        Map<String, String> diagnosticContext = logEvent.getDiagnosticContext();
        Optional<Throwable> throwable = logEvent.getThrowable();
    }
    
}
```

# Known issues
## Using Log Captor alongside with other logging libraries
When building your maven or gradle project it can complain that you are using multiple SLF4J implementations. Log Captor is using logback as SLF4J implementation and SLF4J doesn't allow you to use multiple implementations, therefore you need to explicitly specify which to use during which build phase.
You can fix that by excluding your main logging framework during the unit/integration test phase. Below is an example for Maven Failsafe and Maven Surefire:
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <configuration>
                <classpathDependencyExcludes>
                    <classpathDependencyExclude>org.apache.logging.log4j:log4j-slf4j-impl</classpathDependencyExclude>
                </classpathDependencyExcludes>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-failsafe-plugin</artifactId>
            <configuration>
                <classpathDependencyExcludes>
                    <classpathDependencyExclude>org.apache.logging.log4j:log4j-slf4j-impl</classpathDependencyExclude>
                </classpathDependencyExcludes>
            </configuration>
        </plugin>
    </plugins>
</build>
``` 
And for gradle:
```groovy
configurations {
    testImplementation {
        exclude group: 'org.apache.logging.log4j', module: 'log4j-slf4j-impl'
    }
}
```

## Capturing logs of static inner classes
LogCaptor successfully catches logs of static inner classes with `LogCaptor.forClass(StaticInnerClass.class)` construction 
when SLF4J, Log4J, JUL is used. This doesn't apply to Log4J2 by default it uses different method to for initializing the logger. 
It used `Class.getCanonicalName()` under the covers instead of `Class.getName()`. You should use `LogCaptor.forName(StaticInnerClass.class.getCanonicalName())` for successful 
test execution with Log4j2.

# Contributing

There are plenty of ways to contribute to this project:

* Give it a star
* Share it with a [![Tweet](https://img.shields.io/twitter/url/http/shields.io.svg?style=social)](https://twitter.com/intent/tweet?text=With%20LogCaptor%20it%20is%20now%20very%20easy%20to%20captor%20and%20test%20your%20log%20message&url=https://github.com/Hakky54/log-captor/&via=hakky541&hashtags=logging,testing,log4j,slf4j,log4j2,jul,lombok,developer,java,scala,kotlin,logcaptor)
* Join the [Gitter room](https://gitter.im/hakky54/logcaptor) and leave a feedback or help with answering users questions
* Submit a PR

# Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="https://github.com/AkaZver"><img src="https://avatars.githubusercontent.com/u/40667664?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Vasiliy Sobolev</b></sub></a><br /><a href="#design-AkaZver" title="Design">🎨</a> <a href="#ideas-AkaZver" title="Ideas, Planning, & Feedback">🤔</a><a href="https://github.com/Hakky54/log-captor/commits?author=AkaZver" title="Code">💻</a></td>
    <td align="center"><a href="https://github.com/sleepo581"><img src="https://avatars.githubusercontent.com/u/30793892?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Alexei Brinza</b></sub></a><br /><a href="#design-sleepo581" title="Design">🎨</a><a href="https://github.com/Hakky54/log-captor/commits?author=sleepo581" title="Code">💻</a></td>
    <td align="center"><a href="https://dlsrb6342.github.io"><img src="https://avatars.githubusercontent.com/u/19386038?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Ingyu Hwang</b></sub></a><br /><a href="https://github.com/Hakky54/log-captor/pulls?q=is%3Apr+reviewed-by%3Adlsrb6342" title="Reviewed Pull Requests">👀</a> <a href="#ideas-dlsrb6342" title="Ideas, Planning, & Feedback">🤔</a></td>
    <td align="center"><a href="https://github.com/tjuchniewicz"><img src="https://avatars.githubusercontent.com/u/15428166?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Tomasz Juchniewicz</b></sub></a><br /><a href="#ideas-tjuchniewicz" title="Ideas, Planning, & Feedback">🤔</a></td>
  </tr>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!

## License
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2FHakky54%2Flog-captor.svg?type=large)](https://app.fossa.io/projects/git%2Bgithub.com%2FHakky54%2Flog-captor?ref=badge_large)

