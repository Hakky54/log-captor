# Copilot Instructions

## Build & Test

```bash
# Build and run all tests
mvn clean verify

# Run a single test class
mvn test -Dtest=LogCaptorShould

# Skip tests
mvn clean install -DskipTests

# Check license headers only
mvn validate
```

Test classes must match the regex `.*Should.*` — Maven Surefire is configured to only pick up classes with `Should` in the name.

## Architecture

LogCaptor is a single-module Java library (`nl.altindag.logcaptor`) for capturing log output during tests. The public API surface is small and intentionally narrow.

**Core flow:**
1. `LogCaptor` (the only public entry point) accepts a logger name, class, or `forRoot()`.
2. On construction, it fetches the Logback `Logger` via `LogbackUtils`, attaches an `InMemoryAppender` that drains into a `CopyOnWriteArrayList<ILoggingEvent>`, and registers JUL→SLF4J bridging via `JavaUtilLoggingLoggerUtils`.
3. Log4j1 and Log4j2 are bridged to SLF4J via runtime dependencies (`log4j-over-slf4j`, `log4j-to-slf4j`), so LogCaptor captures them automatically without code changes.
4. `LogCaptor` implements `AutoCloseable` — it detaches the appender on `close()`. Use it in try-with-resources or call `close()` explicitly in `@AfterEach`.
5. `LogEvent` is the rich model for individual captured events (level, message, formatted message, MDC, markers, key-value pairs, throwable, timestamp).

**Module exports** (`module-info.java`):  
Only `nl.altindag.log`, `nl.altindag.log.exception`, and `nl.altindag.log.model` are exported. Util and appender packages are internal API — do not reference them from outside the library.

**Dual compilation:**  
The Maven build compiles twice. First pass targets Java 11 (includes `module-info.java`). Second pass recompiles everything except `module-info.java` targeting Java 8. This keeps class files Java 8 compatible while providing a proper JPMS descriptor.

## Key Conventions

### License header required on every `.java` file
The `license-maven-plugin` runs at the `validate` phase and fails the build if any `.java` file is missing the Apache 2.0 header. Always include it:

```java
/*
 * Copyright 2019 Thunderberry.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * ...
 */
```

### Test structure
- All test classes live under `nl.altindag.log` (root or sub-packages) and must have `Should` in the class name.
- `LogCaptorShould` is the main integration test covering all supported logging frameworks.
- `src/test/java/nl/altindag/log/service/` contains helper service classes (one per logging framework variant) used by tests — these are not test classes themselves and don't follow the `Should` naming convention.

### Static log-level map
`LogCaptor` maintains a static `Map<String, Level> logLevelContainer` to remember the original log level per logger name. Call `logCaptor.resetLogLevel()` in `@AfterEach` to avoid level bleed between tests. `clearLogs()` only clears captured messages, not the level override.

### Parallel test support
`LogbackUtils.getSlf4jLogger()` retries when it receives a `SubstituteLogger` (SLF4J's placeholder during initialization). Retry behavior is tunable via system properties:
- `logcaptor.poll-counter-limit` (default: 10)
- `logcaptor.poll-delay-milliseconds` (default: 100)

### No Lombok in main sources
Lombok is a **test-scope** dependency only. Main source classes are written with hand-crafted getters, no annotations.
