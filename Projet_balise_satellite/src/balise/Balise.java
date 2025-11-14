package balise;

import announcer.Announcer;
import method.*;

/**
 * Classe représentant une balise autonome dans l'océan.
 * Une balise se déplace selon une stratégie définie et notifie les observateurs de ses mouvements.
 */
public class Balise {
    private int x, y;
    private int direction;
    private String id;                          // Identificateur unique de la balise
    Announcer announcer;
    private method.MovingMethod movingMethod;

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
    }

    public void setMovingMethod(method.MovingMethod movingMethod) {
        this.movingMethod = movingMethod;
    }

    public void move() {
        if (movingMethod != null) {
            movingMethod.move(this);
            announcer.announce(new BaliseMoveEvent(this));
        }
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

    @Override
    public String toString() {
        return id + " [Position: (" + x + ", " + y + "), Direction: " + direction + "]";
    }

}
