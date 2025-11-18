package announcer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Gestionnaire central du Pattern Observable (médiateur d'événements).
 * L'Announcer maintient un registre des listeners par type d'événement et distribue
 * les événements aux listeners enregistrés.
 * 
 * Principe : Les objets s'enregistrent pour recevoir des types d'événements spécifiques,
 * puis l'Announcer leur transmet automatiquement tous les événements de ce type.
 * 
 * @see AbstractEvent
 */
public class Announcer {
	/** Index des listeners enregistrés, organisé par type d'événement */
	Map<Class<? extends AbstractEvent>, List<Object>> registrationIndex;
	
	/**
	 * Constructeur créant un Announcer vide.
	 */
	public Announcer() {
		registrationIndex = new HashMap<>();
	}
	
	/**
	 * Enregistre un listener pour recevoir un type d'événement spécifique.
	 * 
	 * Exemple : announcer.register(baliseView, BaliseMoveEvent.class)
	 * → baliseView recevra tous les BaliseMoveEvent diffusés par cet announcer
	 * 
	 * @param o Le listener à enregistrer (doit implémenter l'interface correspondant à l'événement)
	 * @param eventClass Le type d'événement auquel s'abonner (ex: BaliseMoveEvent.class)
	 */
	public void register(Object o, Class<? extends AbstractEvent> eventClass) {
		// Récupérer la liste des listeners pour ce type d'événement
		List<Object> l = registrationIndex.get(eventClass);
		// Si aucun listener n'existe encore pour ce type, créer une nouvelle liste
		if (l == null) {
			l = new ArrayList<Object>();
			registrationIndex.put(eventClass, l);
		}
		// Ajouter le nouveau listener à la liste
		l.add(o);
	}

	/**
	 * Désenregistre un listener d'un type d'événement spécifique.
	 * 
	 * @param o Le listener à désenregistrer
	 * @param eventClass Le type d'événement dont se désabonner
	 */
	public void unregister (Object o, Class<? extends AbstractEvent> eventClass) {
		List<Object> l = registrationIndex.get(eventClass);
		Iterator<Object> itor =  l.iterator();
		while (itor.hasNext()) {
			Object current = itor.next();
			if (o == current) itor.remove();
		}
		// Nettoie l'index si plus aucun listener pour ce type d'événement
		if (l.isEmpty()) {
			registrationIndex.remove(eventClass);
		}
	}
	
	/**
	 * Diffuse un événement à tous les listeners enregistrés pour son type (Pattern Observable).
	 * 
	 * Processus en 3 étapes :
	 * 1. Récupérer la liste des listeners abonnés à ce type d'événement
	 * 2. Copier la liste pour éviter les modifications concurrentes
	 * 3. Appeler anEvent.sentTo(listener) pour chaque listener (Double Dispatch Pattern)
	 * 
	 * Le Double Dispatch permet à l'événement de se transmettre lui-même au listener :
	 * - announce() appelle event.sentTo(listener)
	 * - sentTo() appelle listener.onSpecificEvent(this)
	 * 
	 * @param anEvent L'événement à diffuser (ex: BaliseMoveEvent, SatelliteMoveEvent, etc.)
	 */
	public void announce(AbstractEvent anEvent) {
		// Étape 1 : Identifier le type exact de l'événement (BaliseMoveEvent, etc.)
		Class<?> eventClass = anEvent.getClass();
		// Récupérer la liste des listeners enregistrés pour ce type
		List<Object> l = registrationIndex.get(eventClass);
		if (l == null) return; // Si aucun listener, ne rien faire (optimisation)
		
		// Étape 2 : Copier la liste dans un tableau pour éviter ConcurrentModificationException
		// (au cas où un listener se désenregistrerait pendant la diffusion)
		Object[] registered = l.toArray();
		// Étape 3 : Transmettre l'événement à chaque listener via Double Dispatch
		for (Object current : registered) {
			anEvent.sentTo(current); // L'événement se transmet lui-même au listener
		}
	}
}
