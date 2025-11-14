package balise;

/**
 * États possibles d'une balise
 */
public enum BaliseState {
	COLLECTE("Collecte - Descente"),      // Balise descend et collecte des données
	REMONTEE("Remontée - Synchronisation"); // Balise remonte à la surface pour synchroniser
	
	private final String description;
	
	BaliseState(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
