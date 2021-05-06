[![Actions Status](https://github.com/Hakky54/log-captor/workflows/Build/badge.svg)](https://github.com/Hakky54/log-captor/actions)
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

# Install with [maven](https://mvnrepository.com/artifact/io.github.hakky54/logcaptor)
```xml
<dependency>
    <groupId>io.github.hakky54</groupId>
    <artifactId>logcaptor</artifactId>
    <version>2.5.0</version>
    <scope>test</scope>
</dependency>
```

# Introduction
LogCaptor is a library which will enable you to easily capture logging entries for unit testing purposes.

### Tested Logging libraries
 - SLFJ4
 - Logback
 - Java Util Logging  
 - Apache Log4j
 - Log4j with Lombok
 - Log4j2 with Lombok
 - SLFJ4 with Lombok
 - Java Util Logging with Lombok

See the unit test [LogCaptorShould](src/test/java/nl/altindag/log/LogCaptorShould.java) for all the scenario's or checkout this project [Java Tutorials](https://github.com/Hakky54/java-tutorials) which contains more isolated examples of the individual logging frameworks
 
### Supported Java versions
 - Java 8
 - Java 11+

### Advantages
 - No mocking required
 - No custom Junit extension required
 - Plug & play
 
# Usage
##### Basic example class
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
        String expectedInfoMessage = "Keyboard not responding. Press any key to continue...";
        String expectedWarnMessage = "Congratulations, you are pregnant!";

        LogCaptor logCaptor = LogCaptor.forClass(FooService.class);

        FooService fooService = new FooService();
        fooService.sayHello();

        // Option 1 to assert logging entries
        assertThat(logCaptor.getInfoLogs()).containsExactly(expectedInfoMessage);
        assertThat(logCaptor.getWarnLogs()).containsExactly(expectedWarnMessage);

        // Option 2 to assert logging entries
        assertThat(logCaptor.getLogs())
                .hasSize(2)
                .containsExactly(expectedInfoMessage, expectedWarnMessage);
    }
}
```

##### Initialize LogCaptor once and reuse it during multiple tests with clearLogs method within the afterEach method:
```java
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

public class FooServiceShould {

    private LogCaptor logCaptor = LogCaptor.forClass(FooService.class);
    private static final String EXPECTED_INFO_MESSAGE = "Keyboard not responding. Press any key to continue...";
    private static final String EXPECTED_WARN_MESSAGE = "Congratulations, you are pregnant!";

    @AfterEach
    public void clearLogs() {
        logCaptor.clearLogs();
    }

    @Test
    public void logInfoAndWarnMessagesAndGetWithEnum() {
        Service service = new FooService();
        service.sayHello();

        assertThat(logCaptor.getInfoLogs()).containsExactly(EXPECTED_INFO_MESSAGE);
        assertThat(logCaptor.getWarnLogs()).containsExactly(EXPECTED_WARN_MESSAGE);

        assertThat(logCaptor.getLogs()).hasSize(2);
    }

    @Test
    public void logInfoAndWarnMessagesAndGetWithString() {
        Service service = new FooService();
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
        String expectedInfoMessage = "Congratulations, you are pregnant!";
        String expectedDebugMessage = "Keyboard not responding. Press any key to continue...";

        LogCaptor logCaptor = LogCaptor.forClass(FooService.class);
        logCaptor.setLogLevelToInfo();

        FooService fooService = new FooService();
        fooService.sayHello();

        assertThat(logCaptor.getInfoLogs()).containsExactly(expectedInfoMessage);
        assertThat(logCaptor.getDebugLogs())
            .doesNotContain(expectedDebugMessage)
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

        Fervice service = new FooService();
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
    void captureLoggingEventsContainingException() {
        String expectedInfoMessage = "Keyboard not responding. Press any key to continue...";
        String expectedWarnMessage = "Congratulations, you are pregnant!";

        LogCaptor logCaptor = LogCaptor.forClass(FooService.class);

        Fervice service = new FooService();
        service.sayHello();

        assertThat(logCaptor.getLogs())
                .hasSize(2)
                .containsExactly(expectedInfoMessage, expectedWarnMessage);
    }
}
```

## Using Log Captor alongside with other logging libraries
When building your maven or gradle project it can complain that you are using multiple SLF4J implementations. Log Captor is using logback as SLF4J implementation and SLF4J doesn't allow you to use multiple implementations, therefor you need to explicitly specify which to use during which build phase.
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

## Contributing

There are plenty of ways to contribute to this project:

* Give it a star
* Share it with a [![Tweet](https://img.shields.io/twitter/url/http/shields.io.svg?style=social)](https://twitter.com/intent/tweet?text=With%20LogCaptor%20it%20is%20now%20very%20easy%20to%20captor%20and%20test%20your%20log%20message&url=https://github.com/Hakky54/log-captor/&via=hakky541&hashtags=logging,testing,log4j,slf4j,log4j2,jul,lombok,developer,java,scala,kotlin,logcaptor)
* Join the [Gitter room](https://gitter.im/hakky54/logcaptor) and leave a feedback or help with answering users questions
* Submit a PR

## License
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2FHakky54%2Flog-captor.svg?type=large)](https://app.fossa.io/projects/git%2Bgithub.com%2FHakky54%2Flog-captor?ref=badge_large)
