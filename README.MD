[![Actions Status](https://github.com/Hakky54/log-captor/workflows/Build/badge.svg)](https://github.com/Hakky54/log-captor/actions)
[![Maintainability](https://api.codeclimate.com/v1/badges/b846d86b14658d39f104/maintainability)](https://codeclimate.com/github/Hakky54/log-captor/maintainability)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/7493e0fbb2da4288816ebe524420a928)](https://www.codacy.com/manual/Hakky54/log-captor?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Hakky54/log-captor&amp;utm_campaign=Badge_Grade)
[![codecov](https://codecov.io/gh/Hakky54/log-captor/branch/master/graph/badge.svg)](https://codecov.io/gh/Hakky54/log-captor)
[![Apache2 license](https://img.shields.io/badge/license-Aache2.0-blue.svg)](https://github.com/Hakky54/log-captor/blob/master/LICENSE)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.hakky54/logcaptor/badge.svg)](https://mvnrepository.com/artifact/io.github.hakky54/logcaptor)
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2FHakky54%2Flog-captor.svg?type=shield)](https://app.fossa.io/projects/git%2Bgithub.com%2FHakky54%2Flog-captor?ref=badge_shield)

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/dashboard?id=io.github.hakky54%3Alogcaptor)

# Install with [maven](https://mvnrepository.com/artifact/io.github.hakky54/logcaptor)
```xml
<dependency>
    <groupId>io.github.hakky54</groupId>
    <artifactId>logcaptor</artifactId>
    <version>2.0.1</version>
    <scope>test</scope>
</dependency>
```

# Introduction
LogCaptor is a library which will enable you to easily capture logging entries for unit testing purposes.

### Tested Logging libraries
 - SLFJ4
 - Log4j with Apache
 - Log4j with Lombok
 - Log4j2 with Lombok
 - SLFJ4 with Lombok
 - Java Util Logging with Lombok

See the unit test [LogCaptorShould](src/test/java/nl/altindag/log/LogCaptorShould.java) for all the scenario's 
 
### Supported Java versions
 - Java 8
 - Java 11+ 
 
# Usage
To be tested class
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
Unit test:
```java
import static org.assertj.core.api.Assertions.assertThat;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;

public class FooServiceShould {

    @Test
    public void logInfoAndWarnMessages() {
        String expectedInfoMessage = "Keyboard not responding. Press any key to continue...";
        String expectedWarnMessage = "Congratulations, you are pregnant!";

        LogCaptor<FooService> logCaptor = LogCaptor.forClass(FooService.class);

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

Initialize LogCaptor once and reuse it during multiple tests with clearLogs method within the afterEach method:
```java
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

public class FooServiceShould {

    private LogCaptor<FooService> logCaptor = LogCaptor.forClass(FooService.class);
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

Class which will log events if specific log level has been set
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
Unit test:
```java
import static org.assertj.core.api.Assertions.assertThat;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;

public class FooServiceShould {

    @Test
    public void logInfoAndWarnMessages() {
        String expectedInfoMessage = "Congratulations, you are pregnant!";
        String expectedDebugMessage = "Keyboard not responding. Press any key to continue...";

        LogCaptor<FooService> logCaptor = LogCaptor.forClass(FooService.class);
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

## License
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2FHakky54%2Flog-captor.svg?type=large)](https://app.fossa.io/projects/git%2Bgithub.com%2FHakky54%2Flog-captor?ref=badge_large)
