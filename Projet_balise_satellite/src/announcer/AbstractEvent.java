package announcer;
import java.util.EventObject;

/**
 * Classe de base pour tous les événements du système.
 * Chaque type d'événement hérite de cette classe et implémente la méthode sentTo()
 * pour définir comment il se transmet aux listeners appropriés.
 * 
 * Pattern : Double dispatch - l'événement connaît le type du listener à invoquer.
 * 
 * @see Announcer
 */
public abstract class AbstractEvent extends EventObject {
	
	private static final long serialVersionUID = -3665126000236217922L;

	/**
	 * Constructeur de base pour créer un événement.
	 * 
	 * @param source L'objet source qui a déclenché l'événement
	 */
	public AbstractEvent(Object source) {
		super(source);
	}
	
	/**
	 * Transmet l'événement au listener cible.
	 * Cette méthode doit être implémentée par chaque type d'événement concret
	 * pour caster le target en bon type de listener et appeler la bonne méthode.
	 * 
	 * @param target Le listener qui doit recevoir l'événement
	 */
	public void sentTo(Object target) {}
}
