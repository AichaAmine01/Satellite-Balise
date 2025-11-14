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
        int newY = balise.getY() + direction * verticalStep;
        
        // Vérifier les limites
        if (changeDirectionAtLimits) {
            if (newY >= maxDepth) {
                newY = maxDepth;
                direction = -1; // Remonter
            } else if (newY <= minDepth) {
                newY = minDepth;
                direction = 1; // Descendre
            }
        } else {
            // Limiter sans changer de direction (rester à la limite)
            if (newY > maxDepth) {
                newY = maxDepth;
            } else if (newY < minDepth) {
                newY = minDepth;
            }
        }
        
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
