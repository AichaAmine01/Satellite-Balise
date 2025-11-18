package balise;

import announcer.AbstractEvent;
import satellite.Satellite;

/**
 * Événement déclenché lorsqu'une balise commence la synchronisation avec un satellite
 */
public class SynchronisationStartEvent extends AbstractEvent {
    private final Satellite satellite;
    
    public SynchronisationStartEvent(Balise balise, Satellite satellite) {
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
        return "SynchronisationStartEvent [Balise: " + getBalise().getId() + ", Satellite: " + satellite.getId() + "]";
    }
    
    @Override
    public void sentTo(Object target) {
        if (target instanceof SynchronisationListener) {
            ((SynchronisationListener) target).onSynchronisationStart(this);
        }
    }
}
