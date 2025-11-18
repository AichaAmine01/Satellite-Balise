package satellite;

import announcer.Announcer;

/**
 * Classe représentant un satellite en orbite.
 * Un satellite se déplace horizontalement et peut se synchroniser avec des balises.
 */
public class Satellite {
	private int x, y;
	private int direction;
	private String id;                    // Identifiant unique du satellite
	private boolean disponible;           // Indique si le satellite peut recevoir des données
	private int dataReceived;             // Quantité de données reçues
	private int screenWidth;              // Largeur de l'écran pour la boucle
	Announcer announcer;
	
	public Satellite(int x, int y, int direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.id = "Satellite_" + Math.abs(x + y + System.nanoTime());
		this.disponible = true;           // Initialement disponible
		this.dataReceived = 0;
		this.screenWidth = 800;           // Valeur par défaut
		this.announcer = new Announcer();
	}
	
	public Satellite(int x, int y, int direction, String id) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.id = id;
		this.disponible = true;
		this.dataReceived = 0;
		this.screenWidth = 800;           // Valeur par défaut
		this.announcer = new Announcer();
	}
	
	/**
	 * Définit la largeur de l'écran pour gérer la boucle continue
	 * @param width Largeur de l'écran
	 */
	public void setScreenWidth(int width) {
		this.screenWidth = width;
	}
	
	/**
	 * Déplace le satellite horizontalement avec gestion de la boucle infinie (wrap-around).
	 * Le satellite se déplace à vitesse constante et réapparaît de l'autre côté de l'écran.
	 * 
	 * @param gap Distance de déplacement en pixels (multipliée par la direction)
	 */
	public void move(int gap) {
		// Calculer la nouvelle position : direction = 1 (droite) ou -1 (gauche)
		this.x = this.x + (direction * gap);
		
		// Gestion du wrap-around : créer un effet de boucle infinie
		// Si le satellite sort à droite → réapparaît à gauche
		if (this.x > screenWidth) {
			this.x = 0;
		} 
		// Si le satellite sort à gauche → réapparaît à droite
		else if (this.x < 0) {
			this.x = screenWidth;
		}
		
		// Pattern Observable : Émettre un événement de mouvement
		// Notifie les vues (SatelliteView) pour qu'elles se rafraîchissent
		announcer.announce(new SatelliteMoveEvent(this));
	}

	public void registerMoveEvent(Object o) {
		this.announcer.register(o, SatelliteMoveEvent.class);
	}
	
	/**
	 * Vérifie si le satellite est aligné avec une balise et disponible pour la synchronisation.
	 * 
	 * Deux conditions nécessaires :
	 * 1. Le satellite doit être disponible (pas déjà en train de synchroniser)
	 * 2. La distance horizontale doit être <= tolerance (alignement horizontal)
	 * 
	 * @param baliseX Position X de la balise
	 * @param baliseY Position Y de la balise (non utilisé car satellite en orbite fixe)
	 * @param tolerance Tolérance horizontale en pixels (ex: 10 pixels)
	 * @return true si le satellite peut se synchroniser avec la balise
	 */
	public boolean isAbove(int baliseX, int baliseY, int tolerance) {
		// Vérifier la disponibilité du satellite (pas occupé par une autre balise)
		// ET vérifier l'alignement horizontal (distance absolue <= tolérance)
		return disponible && Math.abs(this.x - baliseX) <= tolerance;
	}
	
	/**
	 * Reçoit des données d'une balise
	 * @param amount Quantité de données à recevoir
	 */
	public void receiveData(int amount) {
		this.dataReceived += amount;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}

	public int getDataReceived() {
		return dataReceived;
	}

	public void setDataReceived(int dataReceived) {
		this.dataReceived = dataReceived;
	}
	
	@Override
	public String toString() {
		return id + " [Position: (" + x + ", " + y + "), Disponible: " + disponible + ", Données: " + dataReceived + "]";
	}
}
