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
module nl.altindag.logcaptor {

    requires transitive org.slf4j;
    requires transitive ch.qos.logback.classic;
    requires transitive ch.qos.logback.core;
    requires transitive jul.to.slf4j;
    requires transitive java.logging;
    requires transitive org.apache.logging.log4j.to.slf4j;

    exports nl.altindag.log;
    exports nl.altindag.log.exception;
    exports nl.altindag.log.model;

}