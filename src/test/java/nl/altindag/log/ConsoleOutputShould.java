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
package nl.altindag.log;

import nl.altindag.console.ConsoleCaptor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

class ConsoleOutputShould {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleOutputShould.class);

    private static final ConsoleCaptor consoleCaptor = new ConsoleCaptor();
    private static final LogCaptor rootLogCaptor = LogCaptor.forRoot();
    private static final LogCaptor fooLogCaptor = LogCaptor.forClass(Foo.class);
    private static final LogCaptor barLogCaptor = LogCaptor.forClass(Bar.class);

    @BeforeAll
    static void setUp() {
        fooLogCaptor.disableConsoleOutput();
    }

    @AfterAll
    static void tearDown() {
        consoleCaptor.close();
        rootLogCaptor.close();
        fooLogCaptor.close();
        barLogCaptor.close();
    }

    @Test
    void properlyLogToConsoleEvenWhenTogglingDisableFunction() {
        Foo foo = new Foo();
        Bar bar = new Bar();

        {
            LOGGER.info("Calling foo");
            foo.foo();

            assertThat(rootLogCaptor.getLogs()).contains("Calling foo");
            assertThat(fooLogCaptor.getLogs()).contains("Inside foo");
            assertThat(consoleCaptor.getStandardOutput().stream().anyMatch(line -> line.contains("Calling foo"))).isTrue();
            assertThat(consoleCaptor.getStandardOutput().stream().anyMatch(line -> line.contains("Inside foo"))).isFalse();

            consoleCaptor.clearOutput();
            rootLogCaptor.clearLogs();
            fooLogCaptor.clearLogs();
        }

        {
            LOGGER.info("Calling bar");
            bar.bar();

            assertThat(rootLogCaptor.getLogs()).contains("Calling bar");
            assertThat(barLogCaptor.getLogs()).contains("Inside bar");
            assertThat(consoleCaptor.getStandardOutput().stream().anyMatch(line -> line.contains("Calling bar"))).isTrue();
            assertThat(consoleCaptor.getStandardOutput().stream().anyMatch(line -> line.contains("Inside bar"))).isTrue();

            consoleCaptor.clearOutput();
            rootLogCaptor.clearLogs();
            barLogCaptor.clearLogs();
        }

        {
            rootLogCaptor.disableConsoleOutput();
            fooLogCaptor.enableConsoleOutput();

            LOGGER.info("Calling foo");
            foo.foo();

            assertThat(rootLogCaptor.getLogs()).contains("Calling foo");
            assertThat(fooLogCaptor.getLogs()).contains("Inside foo");
            assertThat(consoleCaptor.getStandardOutput().stream().anyMatch(line -> line.contains("Calling foo"))).isFalse();
            assertThat(consoleCaptor.getStandardOutput().stream().anyMatch(line -> line.contains("Inside foo"))).isTrue();
            rootLogCaptor.enableConsoleOutput();
        }
    }

    static class Foo {

        private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Foo.class);

        void foo() {
            LOGGER.info("Inside foo");
        }

    }

    static class Bar {

        private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Bar.class);

        void bar() {
            LOGGER.info("Inside bar");
        }

    }

}
