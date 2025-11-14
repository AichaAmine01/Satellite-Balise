package balise;

import java.awt.Color;
import src.nicellipse.component.NiRectangle;

public class BaliseView extends NiRectangle implements BaliseListener {
    private Balise balise;

    public BaliseView(Balise balise) {
        this.balise = balise;
        this.setBackground(Color.RED);
        this.setSize(10, 10);
        this.setLocation(balise.getX(), balise.getY());
    }

    @Override
    public void onBaliseMove(BaliseMoveEvent event) {
        Balise source = (Balise) event.getSource();
        int x = source.getX();
        int y = source.getY();
        this.setLocation(x, y);
    }

    public Balise getBalise() {
        return balise;
    }

}
