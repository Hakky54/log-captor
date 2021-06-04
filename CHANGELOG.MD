<h2 class="github">Changelog</h2>

This list is not intended to be all-encompassing - it will document major and breaking API 
changes with their rationale when appropriate:

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
