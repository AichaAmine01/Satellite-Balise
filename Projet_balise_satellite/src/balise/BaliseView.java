package balise;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import src.nicellipse.component.NiRectangle;

public class BaliseView extends NiRectangle implements BaliseListener {
    private final Balise balise;
    private Color color = Color.YELLOW;
    private int w = 30, h = 30;

    public BaliseView(Balise balise) {
        this.balise = balise;
        // initialisation minimale de taille
        this.setBounds(balise.getX(), balise.getY(), w, h);
        this.setOpaque(true);
    }

    @Override
    public void onBaliseMove(BaliseMoveEvent event) {
        Balise source = (Balise) event.getSource();
        int x = source.getX();
        int y = source.getY();
        this.setBounds(x, y, w, h);
        this.revalidate();
        this.repaint();
    }

    public Balise getBalise() {
        return balise;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public void setColor(Color c) {
        this.color = c;
        repaint();
    }

    public Dimension getPreferredSize() {
        return new Dimension(w, h);
    }


}
