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
package nl.altindag.log.service.apache;

import nl.altindag.log.service.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

/**
 * @author Hakan Altindag
 */
public class ServiceWithApacheLog4jAndMarkers implements Service {

    private static final Logger LOGGER = LogManager.getLogger(ServiceWithApacheLog4jAndMarkers.class);

    @Override
    public void sayHello() {
        Marker marriage = MarkerManager.getMarker("marriage");
        Marker husband = MarkerManager.getMarker("James");
        Marker wife = MarkerManager.getMarker("Mary");
        Marker michael = MarkerManager.getMarker("Michael");
        Marker jennifer = MarkerManager.getMarker("Jennifer");
        Marker elizabeth = MarkerManager.getMarker("Elizabeth");

        husband.addParents(marriage);
        wife.addParents(marriage);
        michael.addParents(wife);
        jennifer.addParents(wife);
        elizabeth.addParents(wife);

        LOGGER.debug(michael, "I haven't spoken to my wife in years. I didn't want to interrupt her.");
    }

}
