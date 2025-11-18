package satellite;

import announcer.AbstractEvent;

/**
 * Événement déclenché lorsqu'un satellite se déplace.
 * Cet événement est émis à chaque itération de la boucle d'animation (30ms)
 * pour notifier les observateurs (notamment SatelliteView) du changement de position.
 */
public class SatelliteMoveEvent extends AbstractEvent{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructeur créant un événement de mouvement de satellite.
	 * 
	 * @param source Le satellite qui s'est déplacé
	 */
	public SatelliteMoveEvent(Object source) {
		super(source);
	}

	/**
	 * Transmet l'événement au listener en appelant onSatelliteMove().
	 * 
	 * @param target Le SatelliteListener à notifier
	 */
	@Override
	public void sentTo(Object target) {
		((SatelliteListener) target).onSatelliteMove(this);
	}
}
