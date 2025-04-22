package nl.altindag.log.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
}
