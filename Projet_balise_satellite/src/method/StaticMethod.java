package method;

import balise.Balise;

/**
 * Stratégie de mouvement statique pour une balise.
 * La balise reste immobile à une position fixe définie lors de la construction.
 * Cette stratégie est utile pour les balises en état SYNCHRONISATION ou pour les tests.
 * 
 * @see MovingMethod
 */
public class StaticMethod implements MovingMethod {
    /** Position X fixe de la balise */
    private int x;
    
    /** Position Y fixe de la balise */
    private int y;

    /**
     * Constructeur pour créer une stratégie statique.
     * 
     * @param x Position X où la balise doit rester
     * @param y Position Y où la balise doit rester
     */
    public StaticMethod(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Maintient la balise à sa position fixe.
     * Cette méthode force la balise à rester aux coordonnées définies.
     * 
     * @param balise La balise à positionner
     */
    @Override
    public void move(Balise balise) {
        // Force la position fixe à chaque appel
        balise.setX(x);
        balise.setY(y);
    }
    
}
