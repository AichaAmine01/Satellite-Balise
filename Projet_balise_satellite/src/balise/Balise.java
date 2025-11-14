package balise;

import announcer.Announcer;
import method.*;

public class Balise {
    private int x, y;
    private int direction;
    Announcer announcer;
    private method.MovingMethod movingMethod;

    public Balise(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
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

}
