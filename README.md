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
    <version>2.7.9</version>
    <scope>test</scope>
</dependency>
```
### Install with Gradle
```groovy
testImplementation 'io.github.hakky54:logcaptor:2.7.9'
```
### Install with Scala SBT
```
libraryDependencies += "io.github.hakky54" % "logcaptor" % "2.7.9" % Test
```
### Install with Apache Ivy
```xml
<dependency org="io.github.hakky54" name="logcaptor" rev="2.7.9" />
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
   - [Mixing up different classloaders](#mixing-up-different-classloaders)
4. [Contributing](#contributing)
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
    public static void setupLogCaptor() {
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
When building your maven or gradle project it can complain that you are using multiple SLF4J implementations. Log Captor is using logback as SLF4J implementation and SLF4J doesn't allow you to use multiple implementations, therefore you need to explicitly specify which to use during which build phase if you are using multiple SLF4J implementations.

During the test execution it can give you the following warning:
```
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:~/.m2/repository/org/apache/logging/log4j/log4j-slf4j-impl/2.17.0/log4j-slf4j-impl-2.17.0.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:~/.m2/repository/ch/qos/logback/logback-classic/1.2.10/logback-classic-1.2.10.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.apache.logging.slf4j.Log4jLoggerFactory]
```

Because of this dependency issue the test may fail to capture the logs. You can fix that by excluding your main logging framework during the unit/integration test phase.
Below is an example for Maven Failsafe and Maven Surefire. You can discover which dependency you need to exclude by anaylising the SLF4J warning displayed above.
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
LogCaptor successfully captures logs of static inner classes with `LogCaptor.forClass(MyStaticInnerClass.class)` construction 
when SLF4J, Log4J, JUL is used. This doesn't apply to static inner classes when Log4J2 is used, because it uses different method to for initializing the logger. 
It used `Class.getCanonicalName()` under the covers instead of `Class.getName()`. You should use `LogCaptor.forName(StaticInnerClass.class.getCanonicalName())` or `LogCaptor.forRoot()`for to be able to capture logs for static inner classes while using Log4J2.

## Mixing up different classloaders
It may occur that different classloaders are being used during your tests. LogCaptor works with different classloaders, but it will fail to set up if part of it has been created with a different classloader.
This may occur for example while using `@QuarkusTest` annotation. The logger will be setup by the default JDK classloader `[jdk.internal.loader.ClassLoaders$AppClassLoader]` while LogCaptor will be setup by the other classloader, in this case Quarkus `[io.quarkus.bootstrap.classloading.QuarkusClassLoader]`. 
LogCaptor will try to cast an object during the preparation, but it will fail as it is not possible to cast an object created by a different classloader. You need to make sure the logger is using the same classloader as LogCaptor or the other way around.

There is also an easier alternative solution by sending all of your logs to the console and capture that with [ConsoleCaptor](https://github.com/Hakky54/console-captor).
Add the following two dependencies to your project:
```xml
<dependencies>
   <dependency>
      <groupId>io.github.hakky54</groupId>
      <artifactId>consolecaptor</artifactId>
      <scope>test</scope>
   </dependency>

   <dependency>
      <groupId>ch.qos.logback</groupId>
      <artifactId>logback-classic</artifactId>
      <scope>test</scope>
   </dependency>

</dependencies>
```

Add the `logback-test.xml` configuration below to your test resources:
```xml
<configuration>

    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="TRACE">
        <appender-ref ref="STDOUT" />
    </root>

</configuration>
```

Your target class:
```java
@Path("/hello")
public class HelloResource {
    
    Logger logger = LoggerFactory.getLogger(GreetingResource.class);

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        logger.info("Hello");
        return "Hello";
    }
}
```
Your test class:
```java
import io.quarkus.test.junit.QuarkusTest;
import nl.altindag.console.ConsoleCaptor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class QuarkusTestTest {

    @Test
    void captureLogs() {
        try(ConsoleCaptor consoleCaptor = new ConsoleCaptor()) {
           HelloResource resource = new HelloResource();
           resource.hello();

           List<String> standardOutput = consoleCaptor.getStandardOutput();

           assertThat(standardOutput)
                   .hasSize(1)
                   .contains("Hello");
        }
    }

}
```

# Contributing

There are plenty of ways to contribute to this project:

* Give it a star
* Share it with a [![Tweet](https://img.shields.io/twitter/url/http/shields.io.svg?style=social)](https://twitter.com/intent/tweet?text=With%20LogCaptor%20it%20is%20now%20very%20easy%20to%20captor%20and%20test%20your%20log%20message&url=https://github.com/Hakky54/log-captor/&via=hakky541&hashtags=logging,testing,log4j,slf4j,log4j2,jul,lombok,developer,java,scala,kotlin,logcaptor)
* Join the [Gitter room](https://gitter.im/hakky54/logcaptor) and leave a feedback or help with answering users questions
* Submit a PR

## License
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2FHakky54%2Flog-captor.svg?type=large)](https://app.fossa.io/projects/git%2Bgithub.com%2FHakky54%2Flog-captor?ref=badge_large)

