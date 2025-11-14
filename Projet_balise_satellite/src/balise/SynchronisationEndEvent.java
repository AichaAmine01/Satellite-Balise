package balise;

import announcer.AbstractEvent;
import satellite.Satellite;

/**
 * Événement déclenché lorsqu'une balise termine la synchronisation avec un satellite
 */
public class SynchronisationEndEvent extends AbstractEvent {
    private final Satellite satellite;
    
    public SynchronisationEndEvent(Balise balise, Satellite satellite) {
        super(balise);  // La balise est la source de l'événement
        this.satellite = satellite;
    }
    
    public Balise getBalise() {
        return (Balise) getSource();
    }
    
    public Satellite getSatellite() {
        return satellite;
    }
    
    @Override
    public String toString() {
        return "SynchronisationEndEvent [Balise: " + getBalise().getId() + ", Satellite: " + satellite.getId() + "]";
    }
}
