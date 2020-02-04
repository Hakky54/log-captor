# Install with [maven](https://mvnrepository.com/artifact/io.github.hakky54/logcaptor)
```xml
<dependency>
    <groupId>io.github.hakky54</groupId>
    <artifactId>logcaptor</artifactId>
    <version>1.0.0</version>
</dependency>
```

# Introduction
LogCaptor is a library which will enable you to easily capture logging entries for unit testing purposes.

### Tested Logging libraries
 - Log4j with Apache
 - Log4j with Lombok
 - Log4j2 with Lombok
 - Slf4j with Lombok
 - Java Util Logging with Lombok
 
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
import org.junit.Test;
import ch.qos.logback.classic.Level;

public class FooServiceShould {

    @Test
    public void logInfoAndWarnMessages() {
        String expectedInfoMessage = "Keyboard not responding. Press any key to continue...";
        String expectedWarnMessage = "Congratulations, you are pregnant!";

        LogCaptor logCaptor = LogCaptor.forClass(FooService.class);

        FooService fooService = new FooService();
        fooService.sayHello();

        // Option 1 to assert logging entries
        assertThat(logCaptor.getLogs(Level.INFO)).containsExactly(expectedInfoMessage);
        assertThat(logCaptor.getLogs(Level.WARN)).containsExactly(expectedWarnMessage);

        // Option 2 to assert logging entries
        assertThat(logCaptor.getLogs("INFO").containsExactly(expectedInfoMessage);
        assertThat(logCaptor.getLogs("WARN").containsExactly(expectedWarnMessage);

        // Option 3 to assert logging entries
        assertThat(logCaptor.getLogs())
                .hasSize(2)
                .containsExactly(expectedInfoMessage, expectedWarnMessage);
    }
}
```

