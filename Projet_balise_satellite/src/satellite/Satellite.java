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
	Announcer announcer;
	
	public Satellite(int x, int y, int direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.id = "Satellite_" + Math.abs(x + y + System.nanoTime());
		this.disponible = true;           // Initialement disponible
		this.dataReceived = 0;
		this.announcer = new Announcer();
	}
	
	public Satellite(int x, int y, int direction, String id) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.id = id;
		this.disponible = true;
		this.dataReceived = 0;
		this.announcer = new Announcer();
	}
	
	public void move(int gap) {
		this.x = this.x + (direction * gap);
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
	public boolean isAbove(int baliseX, int baliseY, int tolerance) {
		// Le satellite doit être disponible et proche horizontalement
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
