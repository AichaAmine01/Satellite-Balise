package app;

import java.awt.Color;
import java.awt.Dimension;

import balise.Balise;
import balise.BaliseView;
import method.LinearMethod;
import method.StaticMethod;
import Mobi.Mobi;
import Mobi.MobiView;
import src.nicellipse.component.NiSpace;

public class MainStrategy {
    public static void main(String[] args) throws InterruptedException {
        NiSpace space = new NiSpace("StrategyDemo", new Dimension(600, 400));
        space.setBackground(Color.lightGray);

        // Balises
        Balise b1 = new Balise(50, 50, 1);
        BaliseView bv1 = new BaliseView(b1);
        b1.setMovingMethod(new LinearMethod(2)); // moves along X
        b1.registerMoveEvent(bv1);

        Balise b2 = new Balise(200, 120, -1);
        BaliseView bv2 = new BaliseView(b2);
        b2.setMovingMethod(new StaticMethod(200, 120)); // stationary
        b2.registerMoveEvent(bv2);

        // Mobis
        Mobi m1 = new Mobi(20, 200, 1);
        MobiView mv1 = new MobiView(m1);
        m1.registerMoveEvent(mv1);

        Mobi m2 = new Mobi(300, 250, -1);
        MobiView mv2 = new MobiView(m2);
        m2.registerMoveEvent(mv2);

        // Add views to space
        space.add(bv1);
        space.add(bv2);
        space.add(mv1);
        space.add(mv2);

        space.openInWindow();

        // Main loop: move balises and mobis
        while (true) {
            b1.move();
            b2.move();

            m1.move(3);
            m2.move(4);

            Thread.sleep(30);
        }
    }
}
