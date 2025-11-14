package balise;

/**
 * Interface pour les écouteurs d'événements de synchronisation
 */
public interface SynchronisationListener {
    /**
     * Appelé lorsqu'une synchronisation commence
     */
    void onSynchronisationStart(SynchronisationStartEvent event);
    
    /**
     * Appelé lorsqu'une synchronisation se termine
     */
    void onSynchronisationEnd(SynchronisationEndEvent event);
}
