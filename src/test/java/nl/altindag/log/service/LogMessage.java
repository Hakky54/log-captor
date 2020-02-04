package nl.altindag.log.service;

public enum LogMessage {

    INFO("Hi there friend!"),
    WARN("Congratulations, you are pregnant!"),
    DEBUG("Keyboard not responding. Press any key to continue...");

    LogMessage(String message) {
        this.message = message;
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public String getLogLevel() {
        return this.name();
    }

}
