package method;

import balise.Balise;

public class LinearMethod implements MovingMethod {
    private int gap;

    public LinearMethod(int gap) {
        this.gap = gap;
    }

    @Override
    public void move(Balise balise) {
        balise.setX(balise.getX() + balise.getDirection() * gap);
    }
}
