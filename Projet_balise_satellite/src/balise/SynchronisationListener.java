package balise;

/**
 * Interface pour les écouteurs d'événements de synchronisation.
 * 
 * Les classes qui implémentent cette interface (BaliseView, SatelliteView, SynchronisationLinePanel)
 * sont notifiées au début et à la fin d'une synchronisation entre une balise et un satellite.
 * 
 * Permet de réagir visuellement à la synchronisation :
 * - BaliseView et SatelliteView affichent des messages console
 * - SynchronisationLinePanel dessine/efface une ligne rouge de connexion
 * 
 * @see SynchronisationStartEvent
 * @see SynchronisationEndEvent
 */
public interface SynchronisationListener {
    /**
     * Appelé lorsqu'une synchronisation commence entre une balise et un satellite.
     * 
     * @param event Événement contenant la balise et le satellite impliqués
     */
    void onSynchronisationStart(SynchronisationStartEvent event);
    
    /**
     * Appelé lorsqu'une synchronisation se termine (transfert complet des données).
     * 
     * @param event Événement contenant la balise et le satellite impliqués
     */
    void onSynchronisationEnd(SynchronisationEndEvent event);
}
