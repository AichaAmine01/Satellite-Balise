package method;

import balise.Balise;

/**
 * Stratégie de mouvement vertical pour une balise.
 * La balise se déplace uniquement en profondeur (Y), soit en descendant, soit en remontant.
 */
public class VerticalMethod implements MovingMethod {
    private int verticalStep;  // Pas du déplacement vertical
    private int maxDepth;      // Profondeur maximale (limite inférieure)
    private int minDepth;      // Profondeur minimale (limite supérieure)
    private int direction = 1; // Direction : 1 pour descendre, -1 pour remonter
    private boolean changeDirectionAtLimits = true; // Change de direction aux limites

    /**
     * Constructeur pour le mouvement vertical avec limites
     * @param verticalStep Pas du déplacement vertical
     * @param minDepth Profondeur minimale (surface)
     * @param maxDepth Profondeur maximale (fond)
     */
    public VerticalMethod(int verticalStep, int minDepth, int maxDepth) {
        this.verticalStep = verticalStep;
        this.minDepth = minDepth;
        this.maxDepth = maxDepth;
    }

    /**
     * Constructeur simplifié pour le mouvement vertical
     * @param verticalStep Pas du déplacement vertical
     */
    public VerticalMethod(int verticalStep) {
        this(verticalStep, 0, 1000);
    }

    @Override
    public void move(Balise balise) {
        // Calculer la nouvelle position Y en fonction de la direction (1 = descendre, -1 = remonter)
        int newY = balise.getY() + direction * verticalStep;
        
        // Vérifier les limites de profondeur
        if (changeDirectionAtLimits) {
            // Mode yo-yo : inverser la direction aux limites
            if (newY >= maxDepth) {
                newY = maxDepth;          // Limiter à la profondeur maximale
                direction = -1;           // Inverser la direction : remonter
            } else if (newY <= minDepth) {
                newY = minDepth;          // Limiter à la profondeur minimale
                direction = 1;            // Inverser la direction : descendre
            }
        } else {
            // Mode limite simple : rester bloqué à la limite sans changer de direction
            if (newY > maxDepth) {
                newY = maxDepth;
            } else if (newY < minDepth) {
                newY = minDepth;
            }
        }
        
        // Appliquer la nouvelle position Y
        balise.setY(newY);
    }

    public int getVerticalStep() {
        return verticalStep;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public int getMinDepth() {
        return minDepth;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = (direction > 0) ? 1 : -1;
    }

    public boolean isChangeDirectionAtLimits() {
        return changeDirectionAtLimits;
    }

    public void setChangeDirectionAtLimits(boolean changeDirectionAtLimits) {
        this.changeDirectionAtLimits = changeDirectionAtLimits;
    }
}
