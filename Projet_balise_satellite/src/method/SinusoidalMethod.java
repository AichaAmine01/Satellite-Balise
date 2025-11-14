package method;

import balise.Balise;

/**
 * Stratégie de mouvement sinusoïdal pour une balise.
 * La balise se déplace horizontalement (X) en suivant une courbe sinusoïdale verticale (Y).
 */
public class SinusoidalMethod implements MovingMethod {
    private int horizontalStep;  // Déplacement horizontal par itération
    private int amplitude;        // Amplitude de l'oscillation verticale
    private int frequency;        // Fréquence de l'oscillation
    private int timeStep = 0;     // Compteur du temps
    private Integer initialY = null; // mémoriser la position Y initiale pour éviter l'accumulation

    /**
     * Constructeur pour le mouvement sinusoïdal
     * @param horizontalStep Pas du déplacement horizontal
     * @param amplitude Amplitude maximale de l'oscillation en Y
     * @param frequency Fréquence de l'oscillation
     */
    public SinusoidalMethod(int horizontalStep, int amplitude, int frequency) {
        this.horizontalStep = horizontalStep;
        this.amplitude = amplitude;
        this.frequency = frequency;
    }

    @Override
    public void move(Balise balise) {
        // Mouvement horizontal
        balise.setX(balise.getX() + balise.getDirection() * horizontalStep);
        
        // Mouvement sinusoïdal en Y : Y = Y_initial + amplitude * sin(2π * frequency * t)
        if (initialY == null) {
            initialY = balise.getY();
        }
        double angle = 2 * Math.PI * frequency * timeStep / 100.0;
        int yOffset = (int) (amplitude * Math.sin(angle));
        balise.setY(initialY + yOffset);
        
        timeStep++;
    }

    public int getHorizontalStep() {
        return horizontalStep;
    }

    public int getAmplitude() {
        return amplitude;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getTimeStep() {
        return timeStep;
    }
}
