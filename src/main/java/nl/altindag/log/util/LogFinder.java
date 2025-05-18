/*
 * Copyright 2019 Thunderberry.
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
package nl.altindag.log.util;

import nl.altindag.log.LogCaptor;
import nl.altindag.log.model.LogEvent;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Johnny Nilsson
 */
public class LogFinder {

    private static final LogCaptor logCaptor = LogCaptor.forRoot();

    public enum MatchRule {EXACT, IGNORE_NULL}

    public static boolean hasLogEvent(LogEvent match) {
        return hasLogEvent(match, MatchRule.IGNORE_NULL);
    }

    public static boolean hasLogEvent(LogEvent match, MatchRule rule) {
        return !getLogEvents(match, rule).isEmpty();
    }

    public static Set<LogEvent> getLogEvents(LogEvent match) {
        return getLogEvents(match, MatchRule.IGNORE_NULL);
    }

    public static Set<LogEvent> getLogEvents(LogEvent match, MatchRule rule) {
        switch (rule) {
            case EXACT:
                return matchExact(match);
            case IGNORE_NULL:
                return matchIgnoreNull(match);
            default:
                return Collections.emptySet();
        }
    }

    private static Set<LogEvent> filterEvents(Predicate<LogEvent> predicate) {
        return logCaptor.getLogEvents().stream()
                .filter(predicate)
                .collect(Collectors.toSet());
    }

    private static Set<LogEvent> matchExact(LogEvent match) {
        return filterEvents(candidate -> candidate.equals(match));
    }

    private static Set<LogEvent> matchIgnoreNull(LogEvent match) {
        List<BiConsumer<LogEvent, List<FieldMatcher<?>>>> rules = Arrays.asList(
                // message
                (candidate, list) -> Optional.ofNullable(match.getMessage())
                        .ifPresent(matchValue -> list.add(new FieldMatcher<>(candidate.getMessage(), matchValue))),
                // formatted message
                (candidate, list) -> Optional.ofNullable(match.getFormattedMessage())
                        .ifPresent(matchValue -> list.add(new FieldMatcher<>(candidate.getFormattedMessage(), matchValue))),
                // level
                (candidate, list) -> Optional.ofNullable(match.getLevel())
                        .ifPresent(matchValue -> list.add(new FieldMatcher<>(candidate.getLevel(), matchValue))),
                // loggerName
                (candidate, list) -> Optional.ofNullable(match.getLoggerName())
                        .ifPresent(matchValue -> list.add(new FieldMatcher<>(candidate.getLoggerName(), matchValue))),
                // threadName
                (candidate, list) -> Optional.ofNullable(match.getThreadName())
                        .ifPresent(matchValue -> list.add(new FieldMatcher<>(candidate.getThreadName(), matchValue))),
                // timestamp
                (candidate, list) -> Optional.ofNullable(match.getTimeStamp())
                        .ifPresent(matchValue -> list.add(new FieldMatcher<>(candidate.getTimeStamp(), matchValue))),
                // arguments
                (candidate, list) -> Optional.ofNullable(match.getArguments())
                        .ifPresent(matchValue -> list.add(new FieldMatcher<>(candidate.getArguments(), matchValue))),
                // throwable
                (candidate, list) -> match.getThrowable()
                        .ifPresent(matchValue -> list.add(new ThrowableFieldMatcher(candidate.getThrowable().orElse(null), matchValue))),
                // diagnosticContext
                (candidate, list) -> Optional.ofNullable(match.getDiagnosticContext())
                        .ifPresent(matchValue -> list.add(new FieldMatcher<>(candidate.getDiagnosticContext(), matchValue))),
                // keyValuePairs
                (candidate, list) -> Optional.ofNullable(match.getKeyValuePairs())
                        .ifPresent(matchValue -> list.add(new FieldMatcher<>(candidate.getKeyValuePairs(), matchValue))),
                // markers
                (candidate, list) -> Optional.ofNullable(match.getMarkers())
                        .ifPresent(matchValue -> list.add(new FieldMatcher<>(candidate.getMarkers(), matchValue)))
        );

        return filterEvents(candidate -> {
            List<FieldMatcher<?>> matchers = new ArrayList<>();
            rules.forEach(rule -> rule.accept(candidate, matchers));
            return matchers.stream().allMatch(FieldMatcher::matches);
        });
    }

    private static class FieldMatcher<T> {
        final T candidate, match;

        FieldMatcher(T candidate, T match) {
            this.candidate = candidate;
            this.match = match;
        }

        boolean matches() {
            return Objects.equals(candidate, match);
        }
    }

    private static class ThrowableFieldMatcher extends FieldMatcher<Throwable> {
        ThrowableFieldMatcher(Throwable candidate, Throwable match) {
            super(candidate, match);
        }

        @Override
        boolean matches() {
            return candidate != null
                    && match.getClass().equals(candidate.getClass())
                    && Objects.equals(match.getMessage(), candidate.getMessage());
        }
    }
}
