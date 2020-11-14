package nl.altindag.log.model;

import java.util.Objects;
import java.util.Optional;

public final class LogEvent {

    private final String message;
    private final String level;
    private final Throwable throwable;

    public LogEvent(String message, String level, Throwable throwable) {
        this.message = Objects.requireNonNull(message);
        this.level = Objects.requireNonNull(level);
        this.throwable = throwable;
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

}
