package nl.altindag.log.service;

public enum LogMessage {

    INFO("Hi there friend!"),
    WARN("Congratulations, you are pregnant!"),
    DEBUG("Keyboard not responding. Press any key to continue..."),
    ERROR("An error occurred while displaying the previous error"),
    TRACE("Something happened");

    private String message;

    LogMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getLogLevel() {
        return this.name();
    }

}
