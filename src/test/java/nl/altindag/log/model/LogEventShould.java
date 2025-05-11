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
package nl.altindag.log.model;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Johnny Nilsson
 */
public class LogEventShould {

    @Test
    void beEqualWhereInstancesHaveSameValues() {
        ZonedDateTime timestamp = ZonedDateTime.now();
        List<Object> arguments = new ArrayList<>();
        arguments.add("param");
        RuntimeException throwable = new RuntimeException();
        Map<String, String> diagnosticContext = new HashMap<>();
        diagnosticContext.put("trace.id", "value");
        Map<String, Object> keyValuePair = new HashMap<>();
        keyValuePair.put("key", "value");
        List<Map.Entry<String, Object>> keyValuePairs = new ArrayList<>();
        keyValuePairs.add(new ArrayList<>(keyValuePair.entrySet()).get(0));
        LogMarker refMarker = new LogMarker("REF_MARKER", null);
        List<LogMarker> references = new ArrayList<>();
        references.add(refMarker);
        LogMarker marker = new LogMarker("MARKER", references);
        List<LogMarker> markers = new ArrayList<>();
        markers.add(marker);

        LogEvent logEventA = new LogEvent(
                "Message",
                "FormattedMessage",
                "INFO",
                "nl.altindag.log.Service",
                "main",
                timestamp,
                arguments,
                throwable,
                diagnosticContext,
                keyValuePairs,
                markers);

        LogEvent logEventB = new LogEvent(
                "Message",
                "FormattedMessage",
                "INFO",
                "nl.altindag.log.Service",
                "main",
                timestamp,
                arguments,
                throwable,
                diagnosticContext,
                keyValuePairs,
                markers);

        assertThat(logEventA).isEqualTo(logEventB);
    }

    @Test
    void haveSameHashCodeWhenEqual() {
        ZonedDateTime timestamp = ZonedDateTime.now();
        LogEvent logEventA = new LogEvent("Message", "Message", "INFO", null, null, timestamp, null, null, null, null, null);
        LogEvent logEventB = new LogEvent("Message", "Message", "INFO", null, null, timestamp, null, null, null, null, null);

        assertThat(logEventA).isEqualTo(logEventB);
        assertThat(logEventA.hashCode()).isEqualTo(logEventB.hashCode());
    }

    @Test
    void haveDifferentHashCodeWhenNotEqual() {
        ZonedDateTime timestamp = ZonedDateTime.now();
        LogEvent logEventA = new LogEvent("Message", "Message", "INFO", null, null, timestamp, null, null, null, null, null);
        LogEvent logEventB = new LogEvent("Error", "Error", "ERROR", null, null, timestamp, null, null, null, null, null);

        assertThat(logEventA).isNotEqualTo(logEventB);
        assertThat(logEventA.hashCode()).isNotEqualTo(logEventB.hashCode());
    }

}
