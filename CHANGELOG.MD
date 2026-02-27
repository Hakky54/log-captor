<h2 class="github">Changelog</h2>

This list is not intended to be all-encompassing - it will document major and breaking API 
changes with their rationale when appropriate:

### v2.12.5
- Prevent duplicate console output when using SpringBootTest with re-enabled ConsoleAppender
### v2.12.4
- Fixed capturing MDC context
### v2.12.3
- Fixed No appenders present in context while using SpringBootTest with disabled ConsoleAppender 
- Bumped dependencies
### v2.12.2
- Improve disabling console output
- Renamed module name
- Bumped dependencies
### v2.12.1
- Corrected module-info file
### v2.12.0
- Added support for Java Modules
- Added helper methods for checking individual messages
- Bumped dependencies
### v2.11.0
- Added option to capture Markers
- Bumped dependencies
### v2.10.2
- Added toString method to LogCaptor
- Bumped dependencies
### v2.10.1
- Bumped dependencies
- Resolved CVE-2024-12798
- Resolved CVE-2024-12801
### v2.10.0
- Added option to capture key value pairs
- Bumped dependencies
### v2.9.3
- Corrected copyright in license header
- Bug-fix Handle not initialized logger
- Bumped dependencies
### v2.9.2
- Bumped transitive dependencies for resolving CVE-2023-6378 from logback
### v2.9.1
- Bumped transitive dependencies
- Made disabling console output NPE safe
### v2.9.0
- Bumped transitive dependencies
- Added option to disable console output
### v2.8.0
- Bumped SLF4J to version 2.x.x
- Bumped transitive dependencies
### v2.7.11
- Added option to disable console output (Backport)
### v2.7.10
- Bumped transitive dependencies
- Added toString method for LogEvent
### v2.7.9
- Bumped transitive dependencies
- Added validation for using different classloaders
### v2.7.8
- Bumped transitive dependencies (Logback and Log4J)
### v2.7.7
- Bumped transitive dependencies (Logback)
### v2.7.6
- Bumped transitive dependencies (Log4J)
### v2.7.5
- Bumped transitive dependencies (Logback)
### v2.7.4
- Bumped transitive dependencies (Log4J)
### v2.7.3
- Bumped transitive dependencies (Logback)
### v2.7.2
- Fixed concurrency issue
- Republished with JDK 1.8 compatibility
### v2.7.1
- Fixed concurrency issue
- Accidently published JDK 11 compiles classes
### v2.7.0
- Added thread name in LogEvent
### v2.6.1
- Added cleanup function to tear down the setup
### v2.6.0
- Added MDC (Mapped Diagnostic Context) support
### v2.5.0
- Added capturing logger name within LogEvents
- Added filtering option
- Bumped dependencies
### v2.4.0
- Preventing class cast exception when incorrect slf4j implementation is found
- Added jul support out-of-the-box
- Added timestamp option to LogEvent
### v2.3.2
- Added option to disable logs
### v2.3.1
- Added license header
- Added author
- Added an adapter for Apache Log4j logger
### v2.3.0
- Added option to capture formatted messages and arguments for un-formatted messages
### v2.2.0
- Added option to capture exceptions
### v2.1.1
- Added option to easily captor all logs with Root option
### v2.1.0
- Removed generic type of LogCaptor
- Added option to create LogCaptor with logger name
### v2.0.1
- Marked LogCaptor as final, so it is not extendable
### v2.0.0
- Removed deprecated methods
### v1.1.2
- Marked getLogs deprecated
- Marked setLogLevel deprecated
- Added methods for easily adjust log level
- Added methods for easily getting log messages for different levels
### v1.1.1
- Support for reusing LogCaptor by clearing the logs
### v1.1.0
- Added option to change log level
### v1.0.1
- Added generic type for target class to resolve warnings
### v1.0.0
- Initial release
