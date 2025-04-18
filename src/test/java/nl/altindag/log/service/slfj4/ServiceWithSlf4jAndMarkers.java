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
package nl.altindag.log.service.slfj4;

import nl.altindag.log.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * @author Hakan Altindag
 */
public class ServiceWithSlf4jAndMarkers implements Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceWithSlf4jAndMarkers.class);

    @Override
    public void sayHello() {
        Marker marriage = MarkerFactory.getMarker("marriage");
        Marker husband = MarkerFactory.getMarker("James");
        Marker wife = MarkerFactory.getMarker("Mary");
        Marker michael = MarkerFactory.getMarker("Michael");
        Marker jennifer = MarkerFactory.getMarker("Jennifer");
        Marker elizabeth = MarkerFactory.getMarker("Elizabeth");

        marriage.add(husband);
        marriage.add(wife);
        wife.add(michael);
        wife.add(jennifer);
        wife.add(elizabeth);

        LOGGER.debug(marriage, "I haven't spoken to my wife in years. I didn't want to interrupt her.");
    }

}
