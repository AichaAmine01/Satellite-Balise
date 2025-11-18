package satellite;

/**
 * Interface pour écouter les mouvements d'un satellite.
 * Les objets implémentant cette interface (comme SatelliteView) sont notifiés
 * à chaque déplacement du satellite pour mettre à jour l'affichage.
 * 
 * @see SatelliteMoveEvent
 * @see SatelliteView
 */
public interface SatelliteListener {
	/**
	 * Méthode appelée lorsqu'un satellite se déplace.
	 * 
	 * @param event L'événement contenant le satellite qui s'est déplacé
	 */
	public void onSatelliteMove(SatelliteMoveEvent event);
}
