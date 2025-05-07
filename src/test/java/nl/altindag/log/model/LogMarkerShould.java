package nl.altindag.log.model;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LogMarkerShould {

    @Test
    void beEqualWhereInstancesHaveSameValues() {
        LogMarker refMarkerA = new LogMarker("REF_MARKER", null);
        ArrayList<LogMarker> referencesA = new ArrayList<>();
        referencesA.add(refMarkerA);
        LogMarker markerA = new LogMarker("MARKER", referencesA);

        LogMarker refMarkerB = new LogMarker("REF_MARKER", null);
        ArrayList<LogMarker> referencesB = new ArrayList<>();
        referencesB.add(refMarkerB);
        LogMarker markerB = new LogMarker("MARKER", referencesB);

        assertThat(markerA).isEqualTo(markerB);
    }

    @Test
    void haveSameHashCodeWhenEqual() {
        LogMarker markerA = new LogMarker("MARKER", null);
        LogMarker markerB = new LogMarker("MARKER", null);

        assertThat(markerA).isEqualTo(markerB);
        assertThat(markerA.hashCode()).isEqualTo(markerB.hashCode());
    }

    @Test
    void haveDifferentHashCodeWhenNotEqual() {
        LogMarker refMarkerA = new LogMarker("REF_MARKER", null);
        ArrayList<LogMarker> referencesA = new ArrayList<>();
        referencesA.add(refMarkerA);
        LogMarker markerA = new LogMarker("MARKER", referencesA);

        LogMarker markerB = new LogMarker("MARKER", null);

        assertThat(markerA).isNotEqualTo(markerB);
        assertThat(markerA.hashCode()).isNotEqualTo(markerB.hashCode());
    }

    @Test
    void whenInACollectionReturnTrueWhenFoundInCollection() {
        LogMarker markerA = new LogMarker("MARKER", null);
        LogMarker markerB = new LogMarker("MARKER", null);
        List<LogMarker> markers = new ArrayList<>();
        markers.add(markerA);

        assertThat(markers).containsExactly(markerB);
    }

    @Test
    void beFoundViaContainsWhenLogged() {
        LogCaptor logCaptor = LogCaptor.forClass(LogMarkerShould.class);
        Logger logger = LoggerFactory.getLogger(LogMarkerShould.class);

        Marker marker = MarkerFactory.getDetachedMarker("MARKER");
        marker.add(MarkerFactory.getDetachedMarker("REF_MARKER"));
        logger.info(marker, "This is a test");

        List<LogMarker> refs = new ArrayList<>();
        refs.add(new LogMarker("REF_MARKER", new ArrayList<>()));
        LogMarker expected = new LogMarker("MARKER", refs);

        boolean anyMatch = logCaptor.getLogEvents().stream().anyMatch(event -> event.getMarkers().contains(expected));
        assertThat(anyMatch).isTrue();
    }
}
