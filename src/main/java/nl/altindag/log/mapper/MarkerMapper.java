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
package nl.altindag.log.mapper;

import nl.altindag.log.model.LogMarker;
import org.slf4j.Marker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * <strong>NOTE:</strong>
 * Please don't use this class directly as it is part of the internal API. Class name and methods can be changed any time.
 *
 * @author Hakan Altindag
 */
public final class MarkerMapper implements Function<Marker, LogMarker> {

    private static final MarkerMapper INSTANCE = new MarkerMapper();

    private MarkerMapper() {}

    @Override
    public LogMarker apply(Marker marker) {
        String name = marker.getName();
        List<LogMarker> innerLogMarkers = new ArrayList<>();

        if (marker.hasReferences()) {
            Iterator<Marker> iterator = marker.iterator();
            while (iterator.hasNext()) {
                LogMarker innerLogMarker = apply(iterator.next());
                innerLogMarkers.add(innerLogMarker);
            }
        }

        return new LogMarker(name, Collections.unmodifiableList(innerLogMarkers));
    }

    public static MarkerMapper getInstance() {
        return INSTANCE;
    }
}
