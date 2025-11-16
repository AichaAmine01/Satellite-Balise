package satellite;

import announcer.Announcer;
import balise.Balise;

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
	private Balise lockedBy;              // Balise actuellement synchronisée (exclusivité)
	private boolean frozen;               // Satellite figé pendant une synchronisation
	Announcer announcer;
	
	public Satellite(int x, int y, int direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.id = "Satellite_" + Math.abs(x + y + System.nanoTime());
		this.disponible = true;           // Initialement disponible
		this.dataReceived = 0;
		this.screenWidth = 800;           // Valeur par défaut
		this.lockedBy = null;
		this.frozen = false;
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
		this.lockedBy = null;
		this.frozen = false;
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
	 * Déplace le satellite et gère la boucle (réapparition de l'autre côté)
	 * @param gap Distance de déplacement
	 */
	public void move(int gap) {
		if (!frozen) {
			this.x = this.x + (direction * gap);
		}
		
		// Gestion de la boucle : si le satellite sort de l'écran, il réapparaît de l'autre côté
		if (this.x > screenWidth) {
			this.x = 0;  // Réapparaît à gauche
		} else if (this.x < 0) {
			this.x = screenWidth;  // Réapparaît à droite
		}
		
		announcer.announce(new SatelliteMoveEvent(this));
	}

	public void registerMoveEvent(Object o) {
		this.announcer.register(o, SatelliteMoveEvent.class);
	}
	
	/**
	 * Vérifie si le satellite est au-dessus d'une balise (dans une zone de synchronisation)
	 * @param baliseX Position X de la balise
	 * @param baliseY Position Y de la balise
	 * @param tolerance Tolérance horizontale pour la synchronisation
	 * @return true si le satellite peut se synchroniser avec la balise
	 */
	public boolean isExactlyAbove(int baliseX, int baliseY) {
		// Synchronisation stricte : le satellite doit être libre et pile au-dessus
		return !isBusy() && this.x == baliseX;
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

	/**
	 * Tente de réserver le satellite pour une balise (exclusivité).
	 * @return true si le verrou est pris par cette balise, false sinon.
	 */
	public synchronized boolean lock(Balise balise) {
		if (this.lockedBy == null) {
			this.lockedBy = balise;
			this.disponible = false;
			return true;
		}
		return this.lockedBy == balise; // idempotent si déjà verrouillé par la même balise
	}

	/**
	 * Libère le satellite si verrouillé par la balise donnée.
	 */
	public synchronized void unlock(Balise balise) {
		if (this.lockedBy == balise) {
			this.lockedBy = null;
			this.disponible = true;
		}
	}

	public synchronized boolean isBusy() {
		return this.lockedBy != null;
	}

	public synchronized Balise getLockedBy() {
		return this.lockedBy;
	}

	public int getDataReceived() {
		return dataReceived;
	}
	public boolean isFrozen() {
		return frozen;
	}

	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}

	public void setDataReceived(int dataReceived) {
		this.dataReceived = dataReceived;
	}
	
	@Override
	public String toString() {
		return id + " [Position: (" + x + ", " + y + "), Disponible: " + disponible + ", Données: " + dataReceived + "]";
	}
}
