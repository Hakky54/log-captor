/*
 * Copyright 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.altindag.log.model;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Hakan Altindag
 */
public final class LogEvent {

    private final String message;
    private final String formattedMessage;
    private final String level;
    private final ZonedDateTime timeStamp;
    private final List<Object> arguments;
    private final Throwable throwable;

    public LogEvent(String message, String formattedMessage, String level, ZonedDateTime timeStamp, List<Object> arguments, Throwable throwable) {
        this.message = Objects.requireNonNull(message);
        this.formattedMessage = Objects.requireNonNull(formattedMessage);
        this.level = Objects.requireNonNull(level);
        this.timeStamp = Objects.requireNonNull(timeStamp);
        this.throwable = throwable;
        this.arguments = arguments;
    }

    public String getMessage() {
        return message;
    }

    public String getFormattedMessage() {
        return formattedMessage;
    }

    public String getLevel() {
        return level;
    }

    public ZonedDateTime getTimeStamp() {
        return timeStamp;
    }

    public List<Object> getArguments() {
        return arguments;
    }

    public Optional<Throwable> getThrowable() {
        return Optional.ofNullable(throwable);
    }

}
