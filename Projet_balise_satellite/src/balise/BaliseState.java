package balise;

/**
 * États possibles d'une balise
 */
public enum BaliseState {
	COLLECTE("Collecte - Descente"),           // Balise descend et collecte des données
	REMONTEE("Remontée - Attente"),            // Balise remonte à la surface pour synchroniser
	SYNCHRONISATION("Synchronisation"),         // Balise transfère ses données vers un satellite
	DESCENTE("Descente - Retour");              // Balise redescend après synchronisation
	
	private final String description;
	
	BaliseState(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
