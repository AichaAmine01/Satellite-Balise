package balise;

/**
 * Énumération des états possibles d'une balise (Pattern ÉTAT).
 * 
 * Représente les 4 phases du cycle de vie d'une balise océanique autonome :
 * 
 *   COLLECTE : Phase de descente en profondeur avec collecte de données
 *   REMONTEE : Phase de remontée vers la surface en attente d'un satellite
 *   SYNCHRONISATION : Phase de transfert des données vers un satellite disponible
 *   DESCENTE : Phase de retour en profondeur après synchronisation
 */
public enum BaliseState {
	/** État initial : Balise en profondeur, collecte des données océanographiques */
	COLLECTE("Collecte - Descente"),
	
	/** Balise remonte vers la surface, mémoire pleine, en attente d'un satellite */
	REMONTEE("Remontée - Attente"),
	
	/** Balise à la surface, transfère ses données vers un satellite aligné */
	SYNCHRONISATION("Synchronisation"),
	
	/** Balise redescend vers sa profondeur initiale après transfert réussi */
	DESCENTE("Descente - Retour");
	
	/** Description textuelle de l'état pour affichage */
	private final String description;
	
	/**
	 * Constructeur de l'énumération.
	 * @param description Description lisible de l'état
	 */
	BaliseState(String description) {
		this.description = description;
	}
	
	/**
	 * Retourne la description textuelle de l'état.
	 * @return Description de l'état (ex: "Collecte - Descente")
	 */
	public String getDescription() {
		return description;
	}
}
