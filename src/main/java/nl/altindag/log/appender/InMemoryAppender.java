/*
 * Copyright 2019-2021 the original author or authors.
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

package nl.altindag.log.appender;

import ch.qos.logback.core.AppenderBase;

import java.util.List;

/**
 * <strong>NOTE:</strong>
 * Please don't use this class directly as it is part of the internal API. Class name and methods can be changed any time.
 *
 * @author Hakan Altindag
 */
public final class InMemoryAppender<T> extends AppenderBase<T> {

    private final List<T> eventsCollector;

    public InMemoryAppender(String appenderName, List<T> eventsCollector) {
        this.eventsCollector = eventsCollector;
        this.setName(appenderName);
    }

    @Override
    protected void append(T t) {
        eventsCollector.add(t);
    }

}
