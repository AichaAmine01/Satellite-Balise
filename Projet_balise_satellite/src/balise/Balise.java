package balise;

import announcer.Announcer;
import method.*;
import satellite.Satellite;

/**
 * Classe représentant une balise autonome dans l'océan.
 * Une balise se déplace selon une stratégie définie et notifie les observateurs de ses mouvements.
 * Elle possède trois états : COLLECTE (descente), REMONTEE (montée) et SYNCHRONISATION (transfert).
 */
public class Balise {
    private int x, y;
    private int direction;
    private String id;                          // Identificateur unique de la balise
    Announcer announcer;
    private method.MovingMethod movingMethod;
    
    // Gestion des états
    private BaliseState state;                  // État actuel de la balise
    private int memory;                         // Mémoire actuelle (données collectées)
    private int maxMemory;                      // Capacité maximale de mémoire
    private int collectSpeed;                   // Vitesse de collecte (données par move())
    private int initialY;                       // Position Y initiale (profondeur maximale)
    private static final int SURFACE_Y = 300;  // Y où se trouve la surface de l'océan
    private int riseSpeed;                      // Vitesse de remontée en pixels par move()
    
    // Gestion de la synchronisation
    private Satellite currentSatellite;         // Satellite actuellement en synchronisation
    private int transferSpeed;                  // Vitesse de transfert (données par move())
    private static final int SYNC_TOLERANCE = 50; // Tolérance horizontale pour la synchro (pixels)

    /**
     * Constructeur simple de la balise
     * @param x Position horizontale initiale
     * @param y Position verticale initiale (profondeur)
     * @param direction Direction du mouvement (1 ou -1)
     */
    public Balise(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.id = "Balise_" + Math.abs(x + y + System.nanoTime());
        this.announcer = new Announcer();
        this.state = BaliseState.COLLECTE;      // État initial : collecte
        this.memory = 0;                        // Mémoire initialement vide
        // Variabilité : chaque balise a des caractéristiques différentes
        this.maxMemory = 150 + (int)(Math.random() * 150);     // Capacité entre 150 et 300
        this.collectSpeed = 1 + (int)(Math.random() * 3);      // Vitesse entre 1 et 3 (plus lent)
        this.riseSpeed = 1 + (int)(Math.random() * 3);         // Vitesse de remontée entre 1 et 3
        this.transferSpeed = 5 + (int)(Math.random() * 10);    // Vitesse de transfert entre 5 et 14
        this.initialY = y;                      // Mémoriser la profondeur initiale
        this.currentSatellite = null;           // Pas de satellite en cours
    }

    /**
     * Constructeur complet de la balise
     * @param x Position horizontale initiale
     * @param y Position verticale initiale (profondeur)
     * @param direction Direction du mouvement
     * @param id Identificateur unique
     */
    public Balise(int x, int y, int direction, String id) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.id = id;
        this.announcer = new Announcer();
        this.state = BaliseState.COLLECTE;      // État initial : collecte
        this.memory = 0;                        // Mémoire initialement vide
        // Variabilité : chaque balise a des caractéristiques différentes
        this.maxMemory = 150 + (int)(Math.random() * 150);     // Capacité entre 150 et 300
        this.collectSpeed = 1 + (int)(Math.random() * 3);      // Vitesse entre 1 et 3 (plus lent)
        this.riseSpeed = 1 + (int)(Math.random() * 3);         // Vitesse de remontée entre 1 et 3
        this.transferSpeed = 5 + (int)(Math.random() * 10);    // Vitesse de transfert entre 5 et 14
        this.initialY = y;                      // Mémoriser la profondeur initiale
        this.currentSatellite = null;           // Pas de satellite en cours
    }

    public void setMovingMethod(method.MovingMethod movingMethod) {
        this.movingMethod = movingMethod;
    }

    public void move() {
        if (state == BaliseState.COLLECTE) {
            // Phase de collecte : la balise se déplace selon sa stratégie
            if (movingMethod != null) {
                movingMethod.move(this);
            }
            // Augmenter la mémoire pendant la collecte (vitesse variable)
            memory += collectSpeed;
            // Si la mémoire est pleine, passer à la remontée
            if (memory >= maxMemory) {
                setState(BaliseState.REMONTEE);
            }
        } else if (state == BaliseState.REMONTEE) {
            // Phase de remontée : la balise monte vers la surface (vitesse variable)
            if (y > SURFACE_Y) {
                y -= riseSpeed;
            } else {
                y = SURFACE_Y;  // Rester à la surface en attente de satellite
            }
        } else if (state == BaliseState.SYNCHRONISATION) {
            // Phase de synchronisation : transfert des données vers le satellite
            if (currentSatellite != null && memory > 0) {
                int dataToTransfer = Math.min(transferSpeed, memory);
                memory -= dataToTransfer;
                currentSatellite.receiveData(dataToTransfer);
                
                // Si toutes les données sont transférées, terminer la synchronisation
                if (memory == 0) {
                    endSynchronisation();
                }
            }
        }
        announcer.announce(new BaliseMoveEvent(this));
    }
    
    /**
     * Tente de démarrer une synchronisation avec un satellite
     * @param satellite Le satellite avec lequel tenter la synchronisation
     * @return true si la synchronisation a démarré, false sinon
     */
    public boolean trySynchronize(Satellite satellite) {
        // Conditions pour la synchronisation :
        // 1. La balise doit être en état REMONTEE et à la surface
        // 2. Le satellite doit être disponible et au-dessus de la balise
        if (state == BaliseState.REMONTEE && y == SURFACE_Y && 
            satellite.isAbove(this.x, this.y, SYNC_TOLERANCE)) {
            startSynchronisation(satellite);
            return true;
        }
        return false;
    }
    
    /**
     * Démarre la synchronisation avec un satellite
     */
    private void startSynchronisation(Satellite satellite) {
        this.currentSatellite = satellite;
        satellite.setDisponible(false);  // Le satellite devient occupé
        setState(BaliseState.SYNCHRONISATION);
        // Émettre l'événement de début de synchronisation
        announcer.announce(new SynchronisationStartEvent(this, satellite));
    }
    
    /**
     * Termine la synchronisation et retourne à la phase de collecte
     */
    private void endSynchronisation() {
        if (currentSatellite != null) {
            // Émettre l'événement de fin de synchronisation
            announcer.announce(new SynchronisationEndEvent(this, currentSatellite));
            currentSatellite.setDisponible(true);  // Le satellite redevient disponible
            currentSatellite = null;
        }
        // Retourner à la profondeur initiale pour recommencer la collecte
        y = initialY;
        setState(BaliseState.COLLECTE);
    }

    public void registerMoveEvent(Object o) {
        this.announcer.register(o, BaliseMoveEvent.class);
    }
    
    public void registerSynchronisationStartEvent(Object o) {
        this.announcer.register(o, SynchronisationStartEvent.class);
    }
    
    public void registerSynchronisationEndEvent(Object o) {
        this.announcer.register(o, SynchronisationEndEvent.class);
    }
    
    public void registerStateChangeEvent(Object o) {
        this.announcer.register(o, BaliseStateChangeEvent.class);
    }

    public void setLocation(int x, int y) { // intiliser le localisation
        this.x = x;
        this.y = y;
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

    public MovingMethod getMovingMethod() {
        return movingMethod;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BaliseState getState() {
        return state;
    }

    public void setState(BaliseState newState) {
        if (this.state != newState) {
            this.state = newState;
            // Émettre un événement de changement d'état
            announcer.announce(new BaliseStateChangeEvent(this));
        }
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(int maxMemory) {
        this.maxMemory = maxMemory;
    }
    
    public Satellite getCurrentSatellite() {
        return currentSatellite;
    }
    
    public boolean isSynchronizing() {
        return state == BaliseState.SYNCHRONISATION && currentSatellite != null;
    }

    @Override
    public String toString() {
        return id + " [Position: (" + x + ", " + y + "), Direction: " + direction + ", État: " + state.getDescription() + ", Mémoire: " + memory + "/" + maxMemory + "]";
    }

}
