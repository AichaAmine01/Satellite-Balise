package method;

import balise.Balise;

/**
 * Stratégie de mouvement horizontal linéaire (Pattern STRATÉGIE).
 * 
 * <p>Déplace la balise horizontalement à vitesse constante.
 * La balise se déplace de gauche à droite (ou droite à gauche selon la direction)
 * et rebondit aux bords de l'écran en inversant sa direction.
 * 
 * <p><b>Algorithme :</b>
 * <pre>
 * nouvellePosX = positionX + (direction * vitesse)
 * si (bord atteint) alors direction = -direction
 * </pre>
 * 
 * @see MovingMethod
 */
public class LinearMethod implements MovingMethod {
    /** Vitesse de déplacement horizontal en pixels par itération */
    private int gap;

    /**
     * Constructeur de la stratégie linéaire.
     * @param gap Vitesse de déplacement (pixels par appel de move())
     */
    public LinearMethod(int gap) {
        this.gap = gap;
    }

    /**
     * Déplace la balise horizontalement à vitesse constante.
     * 
     * @param balise La balise à déplacer
     */
    @Override
    public void move(Balise balise) {
        // Calcul nouvelle position X = position actuelle + (direction * vitesse)
        balise.setX(balise.getX() + balise.getDirection() * gap);
    }
}
