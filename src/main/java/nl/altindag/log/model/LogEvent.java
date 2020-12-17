package nl.altindag.log.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class LogEvent {

    private final String message;
    private final String formattedMessage;
    private final String level;
    private final List<Object> arguments;
    private final Throwable throwable;

    public LogEvent(String message, String formattedMessage, String level, List<Object> arguments, Throwable throwable) {
        this.message = Objects.requireNonNull(message);
        this.formattedMessage = Objects.requireNonNull(formattedMessage);
        this.level = Objects.requireNonNull(level);
        this.throwable = throwable;
        this.arguments = arguments;
    }

    public String getMessage() {
        return message;
    }

    public String getLevel() {
        return level;
    }

    public Optional<Throwable> getThrowable() {
        return Optional.ofNullable(throwable);
    }

    public String getFormattedMessage() {
        return formattedMessage;
    }

    public List<Object> getArguments() {
        return arguments;
    }

}
