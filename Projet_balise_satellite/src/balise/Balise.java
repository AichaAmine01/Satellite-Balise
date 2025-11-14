package balise;

import announcer.Announcer;
import method.*;

/**
 * Classe représentant une balise autonome dans l'océan.
 * Une balise se déplace selon une stratégie définie et notifie les observateurs de ses mouvements.
 * Elle possède deux états : COLLECTE (descente) et REMONTEE (montée à la surface).
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
    private int initialY;                       // Position Y initiale (profondeur maximale)
    private static final int SURFACE_Y = 300;  // Y où se trouve la surface de l'océan
    private static final int RISE_SPEED = 2;   // Vitesse de remontée en pixels par move()

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
        this.maxMemory = 100;                   // Capacité max de mémoire
        this.initialY = y;                      // Mémoriser la profondeur initiale
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
        this.maxMemory = 100;                   // Capacité max de mémoire
        this.initialY = y;                      // Mémoriser la profondeur initiale
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
            // Augmenter la mémoire pendant la collecte
            memory += 5;
            // Si la mémoire est pleine, passer à la remontée
            if (memory >= maxMemory) {
                setState(BaliseState.REMONTEE);
            }
        } else if (state == BaliseState.REMONTEE) {
            // Phase de remontée : la balise monte vers la surface
            if (y > SURFACE_Y) {
                y -= RISE_SPEED;
            } else {
                y = SURFACE_Y;  // Rester à la surface
            }
        }
        announcer.announce(new BaliseMoveEvent(this));
    }

    public void registerMoveEvent(Object o) {
        this.announcer.register(o, BaliseMoveEvent.class);
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

    @Override
    public String toString() {
        return id + " [Position: (" + x + ", " + y + "), Direction: " + direction + ", État: " + state.getDescription() + ", Mémoire: " + memory + "/" + maxMemory + "]";
    }

}
