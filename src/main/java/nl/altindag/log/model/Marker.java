package nl.altindag.log.model;

import java.util.List;

public final class Marker {

    private final String name;
    private final List<Marker> references;

    public Marker(String name, List<Marker> references) {
        this.name = name;
        this.references = references;
    }

    public String getName() {
        return name;
    }

    public List<Marker> getReferences() {
        return references;
    }

}
