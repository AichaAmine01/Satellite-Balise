package method;

import balise.Balise;

public class StaticMethod implements MovingMethod {
    private int x;
    private int y;

    public StaticMethod(int x, int y) {
        this.x = x;
        this.y = y;
    }


    @Override
    public void move(Balise balise) {
        // La balise ne bouge pas
        balise.setX(x);
        balise.setY(y);
    }
    
}
